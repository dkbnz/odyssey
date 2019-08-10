package controllers.trips;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Profile;
import models.destinations.Destination;
import models.trips.Trip;
import models.trips.TripDestination;
import repositories.destinations.DestinationRepository;
import repositories.ProfileRepository;
import repositories.TripRepository;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import util.AuthenticationUtil;

import com.google.inject.Inject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TripController extends Controller {

    private static final String AUTHORIZED = "authorized";
    private static final String NAME = "trip_name";
    private static final String TRIP_DESTINATIONS = "trip_destinations";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";
    private static final String DESTINATION_ID = "destination_id";
    private static final String TRIP_ID = "trip_id";
    private static final int MINIMUM_TRIP_DESTINATIONS = 2;
    private static final int DEFAULT_ADMIN_ID = 1;

    private TripRepository tripRepository;
    private ProfileRepository profileRepository;
    private DestinationRepository destinationRepository;


    @Inject
    public TripController(TripRepository tripRepository,
                          ProfileRepository profileRepository,
                          DestinationRepository destinationRepository) {
        this.tripRepository = tripRepository;
        this.profileRepository = profileRepository;
        this.destinationRepository = destinationRepository;
    }


    /**
     * Creates a trips for a user based on information sent in the Http Request body. A trip will have a trip name,
     * and at least two destinations.
     *
     * @param request           Http Request containing Json Body.
     * @param affectedUserId    The user id of the user who will own the new trip.
     * @return                  created() (Http 201) response for successful trip creation, or badRequest() (Http 400).
     */
    public Result create(Http.Request request, Long affectedUserId) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {

                    Profile loggedInUser = profileRepository.findById(Long.valueOf(userId));
                    Profile affectedProfile = profileRepository.findById(affectedUserId);

                    if (loggedInUser == null) {
                        return unauthorized();
                    }

                    // If user is admin, or if they are editing their own profile then allow them to edit.
                    if (!AuthenticationUtil.validUser(loggedInUser, affectedProfile)) {
                        return forbidden();
                    }

                    if (affectedProfile == null) {
                        return badRequest();
                    }

                    JsonNode json = request.body().asJson();

                    if (!isValidTrip(json)) {
                        return badRequest();
                    }

                    // Create a trip object and give it the name extracted from the request.
                    Trip trip = new Trip();
                    trip.setName(json.get(NAME).asText());

                    // Create a json node for the destinations contained in the trip to use for iteration.
                    ArrayNode tripDestinations = (ArrayNode) json.get(TRIP_DESTINATIONS);

                    // Create an empty List for TripDestination objects to be populated from the request.
                    List<TripDestination> destinationList = parseTripDestinations(tripDestinations);

                    // Set the trip destinations to be the array of TripDestination parsed, save the trip, and return 201.
                    if (!destinationList.isEmpty() && isValidDateOrder(destinationList)) {
                        trip.setDestinations(destinationList);
                        affectedProfile.addTrip(trip);
                        profileRepository.save(affectedProfile);
                        for (TripDestination tripDestination: destinationList) {
                            determineDestinationOwnershipTransfer(affectedProfile, tripDestination);
                        }
                        tripRepository.save(trip);
                        return created(Json.toJson(trip.getId()));
                    } else {
                        return badRequest();
                    }

                })
                .orElseGet(() -> unauthorized());
    }


    /**
     * Method for looking at the contents of the main Json body for a trip in a request.
     * NOTE: Does not examine array contents.
     *
     * @param json      the Json body of a request received.
     * @return          false if Json doesn't contain a name or an array of destinations with at least two nodes, else
     *                  returns true.
     */
    private boolean isValidTrip(JsonNode json) {

        // Check if the request contains a trip name and an array of destinations.
        if (!(json.has(NAME) && json.has(TRIP_DESTINATIONS))) {
            return false;
        }

        // Check that the trip name is not empty
        if (json.get(NAME).asText().length() <= 0) {
            return false;
        }

        // Check if the array of destinations in the request contains at least two destinations.
        if (!(json.get(TRIP_DESTINATIONS) == null || json.get(TRIP_DESTINATIONS).size() >= MINIMUM_TRIP_DESTINATIONS)) {
            return false;
        }
        return true;
    }


    /**
     * Updates a single trip for selected user's profile.
     *
     * @param request   Http Request containing Json Body of the selected trip to modify.
     * @param tripId    the id of the trip being modified.
     * @return          ok() (Http 200) if the trip has been successfully modified. If the trip is not valid, returns a
     *                  badRequest() (Http 400). If the user is not logged in, returns a unauthorized()
     *                  (Http 401).
     */
    public Result edit(Http.Request request, Long tripId) {
        Long loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }
        Profile loggedInUser = profileRepository.findById(loggedInUserId);
        if (loggedInUser == null) {
            return unauthorized();
        }
        // Retrieve the individual trip being deleted by its id.
        Trip trip = tripRepository.findById(tripId);

        if (trip == null) {
            return notFound();
        }

        // Retrieve the profile having its trip removed from the trip id.
        Long ownerId = tripRepository.fetchTripOwner(tripId);
        if (ownerId == null) {
            return badRequest();
        }

        Profile tripOwner = profileRepository.findById(ownerId);

        if (!AuthenticationUtil.validUser(loggedInUser, tripOwner)) {
            return forbidden();
        }

        JsonNode json = request.body().asJson();

        if (!isValidTrip(json)) {
            return badRequest();
        }


        String name = json.get(NAME).asText();
        trip.setName(name);

        tripRepository.removeTripDestinations(trip);

        // Create a json node for the destinations contained in the trip to use for iteration.
        ArrayNode tripDestinations = (ArrayNode) json.get(TRIP_DESTINATIONS);

        // Create an empty List for TripDestination objects to be populated from the request.
        List<TripDestination> destinationList = parseTripDestinations(tripDestinations);

        if (!destinationList.isEmpty() && isValidDateOrder(destinationList)) {
            tripRepository.updateTrip(tripOwner, trip, destinationList);
            return ok();
        } else {
            return badRequest();
        }
    }


    /**
     * Parse the ArrayNode from a valid request's Json body to create a list of TripDestination objects
     * This method is used when creating a trip and when editing a trip.
     *
     * @param tripDestinations      the array of trip destinations.
     * @return                      an array of destinations.
     */
    private List<TripDestination> parseTripDestinations(ArrayNode tripDestinations) {

        List<TripDestination> result = new ArrayList<>();
        List<TripDestination> badResult = new ArrayList<>();

        // Simple integer for incrementing the list_order attribute for trip destinations.
        int order = 0;
        long previousDestination = -1;

        // Parse JSON to create and append trip destinations using an iterator.
        Iterator<JsonNode> iterator = tripDestinations.elements();
        while (iterator.hasNext()) {
            // Set the current node having its contents extracted.
            JsonNode destinationJson = iterator.next();
            Long id = destinationJson.get(DESTINATION_ID).asLong();

            // Check if current node has a destination ID, and it corresponds with a destination in our database.
            if (destinationJson.get(DESTINATION_ID) != null
                    && destinationJson.get(DESTINATION_ID).asLong() != previousDestination
                    && destinationRepository.findById(id) != null
            ) {
                // Checks the dates are done correctly
                if (!isValidDates(destinationJson.get(START_DATE).asText(), destinationJson.get(END_DATE).asText())) {
                    return badResult;
                }
                // Parse the values contained in the current node of the array
                Long parsedDestinationId = destinationJson.get(DESTINATION_ID).asLong();

                LocalDate parsedStartDate = null;
                if (!(destinationJson.get(START_DATE).asText().equals("null")
                        || destinationJson.get(START_DATE).asText().equals(""))) {
                    parsedStartDate = LocalDate.parse(destinationJson.get(START_DATE).asText());
                }
                LocalDate parsedEndDate = null;
                if (!(destinationJson.get(END_DATE).asText().equals("null")
                        || destinationJson.get(END_DATE).asText().equals(""))) {
                    parsedEndDate = LocalDate.parse(destinationJson.get(END_DATE).asText());
                }
                Destination parsedDestination = destinationRepository.findById(parsedDestinationId);

                // Create a new TripDestination object and set the values to be those parsed.
                TripDestination newTripDestination = new TripDestination();
                newTripDestination.setDestination(parsedDestination);
                newTripDestination.setStartDate(parsedStartDate);
                newTripDestination.setEndDate(parsedEndDate);
                newTripDestination.setListOrder(order++);

                // Add created destination to the list of trip destinations.
                result.add(newTripDestination);
                previousDestination = id;
            } else {
                return badResult;
            }
        }
        return result;
    }


    /**
     * Checks the start and end dates to make sure that the start date does not happen after the end date but if either
     * is null this does not apply.
     *
     * @param startDate     starting date as string.
     * @param endDate       ending date as string.
     * @return              true if the dates are valid (blank, null, or start date occurs before or at the same time as
     *                      end date), otherwise returns false.
     */
    private boolean isValidDates(String startDate, String endDate) {
        if (startDate.equals("") || startDate.equals("null")) {
            return true;
        } else if (endDate.equals("") || endDate.equals("null")) {
            return true;
        } else {
            return (LocalDate.parse(startDate).isBefore(LocalDate.parse(endDate))
                    || LocalDate.parse(startDate).equals(LocalDate.parse(endDate)));
        }
    }


    /**
     * Checks if all of the start/end dates within a trip are in valid order, to be called after saving a reorder.
     *
     * @param tripDestinations  array of all the destinations in the trip in the new order.
     * @return                  true if all the dates of destinations within a trip are in chronological order,
     *                          false otherwise.
     */
    private boolean isValidDateOrder(List<TripDestination> tripDestinations) {
        // Adds all dates within the list of trip destinations to an array if they aren't null
        List<LocalDate> allDates = new ArrayList<LocalDate>() {};
        for (int i = 0; i < tripDestinations.size(); i++) {
            LocalDate startDate = tripDestinations.get(i).getStartDate();
            LocalDate endDate = tripDestinations.get(i).getEndDate();
            if (startDate != null) {
                allDates.add(startDate);
            }
            if (endDate != null) {
                allDates.add(endDate);
            }
        }
        // Iterate through from item 1 and 2 to n-1 and n. return false if any pair is not in order
        for (int j = 0; j < (allDates.size() - 1); j++) {
            if (!(allDates.get(j) == null || (allDates.get(j + 1) == null))) {
                if ((allDates.get(j).isAfter(allDates.get(j + 1)))) {
                    if (!(allDates.get(j).equals(allDates.get(j + 1)))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * Fetches a single trip from the database for a logged in user.
     *
     * @param request   Http request from client
     * @return          an ok() (Http 200) response rendering the fetched trip onto the page. If requested trip not
     *                  valid then returns a badRequest() (Http 400). If the trip is not found then returns notFound()
     *                  (Http 404).
     */
    public Result fetch(Http.Request request) {

        JsonNode json = request.body().asJson();

        // The id taken from the json node, initialised as null for validation purposes.
        long parsedTripId;

        // Retrieving the id of the trip being fetched
        try {
            parsedTripId = Integer.parseInt(json.get(TRIP_ID).asText());
        } catch (NumberFormatException e) {
            return badRequest();
        }

        // Retrieving the trip which corresponds to the id parsed.
        Trip returnedTrip = tripRepository.findById(parsedTripId);

        // Verifying the existence of the trip being retrieved.
        if (returnedTrip == null) {
            return notFound();
        }

        return ok(Json.toJson(returnedTrip));
    }


    /**
     * Determines if any of the destinations in a trip need to have their ownership changed. This is if the destination
     * is public, not owned by the global admin and the affected profile is not the owner of the public destination.
     *
     * @param affectedProfile   the profile that is having the trip added to.
     * @param tripDestination   the destination that is stored in the trip.
     */
    private Result determineDestinationOwnershipTransfer(Profile affectedProfile, TripDestination tripDestination) {
        Destination destination = tripDestination.getDestination();
        Profile owner = destination.getOwner();

        // Destination is not owned by global admin, it is public, and the user is not the owner of the destination.
        if (owner == null || owner.getId() != DEFAULT_ADMIN_ID && destination.getPublic()
                && !affectedProfile.getId().equals(owner.getId())) {
            destinationRepository.transferToAdmin(destination);
            return ok("Destination ownership changed");
        }

        return ok("Destination ownership doesn't need to be changed");
    }


    /**
     * Fetches all the trips for a specified user.
     *
     * @param id    the id of the user requested.
     * @return      the list of trips as a Json.
     */
    public Result fetchAll(Long id) {
        List<Trip> trips = tripRepository.fetchAllTrips(id);
        return ok(Json.toJson(trips));
    }


    /**
     * Deletes a trip from the user currently logged in.
     *
     * @param request   Http request from the client, from which the current user profile can be obtained.
     * @param tripId    the id of the trip being deleted from a profile
     * @return          If no profile or no trip is found, returns notFound() (Http 404).
     *                  If the trip id is not associated with any profile, returns badRequest() (Http 400).
     *                  If the user is not logged in, returns unauthorized() (Http 401).
     *                  If the user is not the trip owner or an admin, returns unauthorized() (Http 401).
     *                  Otherwise, if trip is successfully deleted, returns ok() (Http 200).
     */
    public Result destroy(Http.Request request, Long tripId) {

        Long loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }
        Profile loggedInUser = profileRepository.findById(loggedInUserId);
        if (loggedInUser == null) {
            return unauthorized();
        }

        // Retrieve the individual trip being deleted by its id.
        Trip trip = tripRepository.findById(tripId);

        if (trip == null) {
            return notFound();
        }

        // Retrieve the profile having its trip removed from the trip id.
        Long ownerId = tripRepository.fetchTripOwner(tripId);
        if (ownerId == null) {
            return badRequest();
        }
        Profile tripOwner = profileRepository.findById(ownerId);

        if (!AuthenticationUtil.validUser(loggedInUser, tripOwner)) {
            return forbidden();
        }

        // Repository method handling the database and object manipulation.
        tripRepository.deleteTripFromProfile(tripOwner, trip);
        // Deletion successful.
        return ok();
    }
}
