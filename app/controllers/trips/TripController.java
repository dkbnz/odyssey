package controllers.trips;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Profile;
import models.destinations.Destination;
import models.trips.Trip;
import models.trips.TripDestination;
import repositories.TripRepository;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TripController extends Controller {

    private final String AUTHORIZED = "authorized";
    private final String NAME = "trip_name";
    private final String TRIP_DESTINATIONS = "trip_destinations";
    private final String START_DATE = "start_date";
    private final String END_DATE = "end_date";
    private final String DESTINATION_ID = "destination_id";
    private final String TRIP_ID = "trip_id";
    private final int MINIMUM_TRIP_DESTINATIONS = 2;
    private TripRepository repository = new TripRepository();


    /**
     * Creates a trips for a user based on information sent in the http request
     * @param request json format of trip information
     * @return OK response for successful creation, or badRequest.
     */
    public Result create(Http.Request request) {

        String userId = request.session().getOptional(AUTHORIZED).orElseGet(null);

        if (userId != null) {

            // User is logged in
            Profile profile = Profile.find.byId(Integer.valueOf(userId));

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
            // Set the trip destinations to be the array of TripDestination parsed, save the trip, and return OK.

            boolean isValidDates = isValidDateOrder(destinationList); ///Please confirm with someone else

            if (destinationList != null && isValidDates) {
                trip.setDestinations(destinationList);
                profile.addTrip(trip);
                profile.save();
                return created();
            } else {
                return badRequest();
            }
        } else {
            return unauthorized();
        }
    }


    /**
     * Method for looking at the contents of the main json body for a trip in a request.
     * NOTE: Does not examine array contents.
     * @param json the json body of a request received
     * @return false json doesn't contain a name or an array of destinations with at least two nodes, or else return true.
     */
    private boolean isValidTrip(JsonNode json) {

        // Check if the request contains a trip name and an array of destinations.
        if (!(json.has(NAME) && json.has(TRIP_DESTINATIONS))) {
            return false;
        }

        // Check if the array of destinations in the request contains at least two destinations.
        if (!(json.get(TRIP_DESTINATIONS) == null || json.get(TRIP_DESTINATIONS).size() >= MINIMUM_TRIP_DESTINATIONS)) {
            return false;
        }

        return true;
    }




    /**
     * Updates a single trip for profile.
     * @param request the request containing the new set of values for the trip to modify
     * @param id the id of the trip being modified
     * @return
     */
    public Result edit(Http.Request request, Long id) {

        String userId = request.session().getOptional(AUTHORIZED).orElseGet(null);

        if (userId != null ) {

            // Current profile
            Profile profile = Profile.find.byId(Integer.valueOf(userId));

            JsonNode json = request.body().asJson();

            if (!isValidTrip(json)) {
                return badRequest();
            }

            // Trip being modified
            Trip trip = Trip.find.byId(id.intValue());

            String name = json.get(NAME).asText();
            trip.setName(name);

            repository.removeTripDestinations(trip);

            // Create a json node for the destinations contained in the trip to use for iteration.
            ArrayNode tripDestinations = (ArrayNode) json.get(TRIP_DESTINATIONS);

            // Create an empty List for TripDestination objects to be populated from the request.
            List<TripDestination> destinationList = parseTripDestinations(tripDestinations);

            if (destinationList != null && isValidDateOrder(destinationList)) {
                trip.setDestinations(destinationList);
                trip.update();
                profile.update();
            } else {
                return badRequest();
            }
        } else {
            return unauthorized();
        }
        return ok();
    }


    /**
     * Parse the ArrayNode from a valid request's json body to create a list of TripDestination objects
     * This method is used when creating a trip and when editing a trip
     * @param tripDestinations the array of trip destinations
     * @return an array of destinations
     */
    private List<TripDestination> parseTripDestinations(ArrayNode tripDestinations) {

        List<TripDestination> result = new ArrayList<>();

        // Simple integer for incrementing the list_order attribute for trip destinations.
        int order = 0;
        long previousDestination = -1;

        // Parse JSON to create and append trip destinations using an iterator.
        Iterator<JsonNode> iterator = tripDestinations.elements();
        while (iterator.hasNext()) {
            // Set the current node having its contents extracted.
            JsonNode destinationJson = iterator.next();
            int id = destinationJson.get(DESTINATION_ID).asInt();

            // Check if current node has a destination ID, and it corresponds with a destination in our database.
            if (destinationJson.get(DESTINATION_ID) != null
                    && destinationJson.get(DESTINATION_ID).asLong() != previousDestination
                    && Destination.find.byId(id) != null
            ) {
                // Checks the dates are done correctly
                if (!isValidDates(destinationJson.get(START_DATE).asText(), destinationJson.get(END_DATE).asText())) {
                    return null;
                }
                // Parse the values contained in the current node of the array
                Integer parsedDestinationId = Integer.parseInt(destinationJson.get(DESTINATION_ID).asText());
                LocalDate parsedStartDate = null;
                if (!(destinationJson.get(START_DATE).asText().equals("null") || destinationJson.get(START_DATE).asText().equals(""))) {
                    parsedStartDate = LocalDate.parse(destinationJson.get(START_DATE).asText());
                }
                LocalDate parsedEndDate = null;
                if (!(destinationJson.get(END_DATE).asText().equals("null") || destinationJson.get(END_DATE).asText().equals(""))) {
                    parsedEndDate = LocalDate.parse(destinationJson.get(END_DATE).asText());
                }
                Destination parsedDestination = Destination.find.byId(parsedDestinationId);

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
                return null;
            }
        }

        return result;
    }

    /**
     * Checks the start and end dates to make sure that the start date does not happen after the end date but if either is null this does not apply
     * @param startDate starting date as string
     * @param endDate ending date as string
     * @return true if valid and false if invalid
     */
    private boolean isValidDates(String startDate, String endDate) {
        if (startDate.equals("") || startDate.equals("null")) {
            return true;
        }else if (endDate.equals("") || endDate.equals("null")){
            return true;
        } else {
            return (LocalDate.parse(startDate).isBefore(LocalDate.parse(endDate)) || LocalDate.parse(startDate).equals(LocalDate.parse(endDate)));
        }
    }

    /**
     * Checks if all of the start/end dates within a trip are in valid order, to be called after saving a reorder
     * @param tripDestinations array of all the destinations in the trip in the new order
     * @return true if all the dates of destinations within a trip are in chronologinal order, false otherwise
     */
    private boolean isValidDateOrder(List<TripDestination> tripDestinations) {
        //adds all dates within the list of tripdestinations to an array if they aren't null
        List<LocalDate> alldates = new ArrayList<LocalDate>() {};
        for(int i = 0; i < tripDestinations.size(); i++) {
            LocalDate destStart = tripDestinations.get(i).getStartDate();
            LocalDate destEnd = tripDestinations.get(i).getEndDate();
            if(destStart != null){
                alldates.add(destStart);
            }
            if(destEnd != null){
                alldates.add(destEnd);
            }
        }
        //iterate through from item 1 and 2 to n-1 and n. return false if any pair is not in order
        for(int j=0; j<(alldates.size()-1); j++){
            if (!(alldates.get(j) == null || (alldates.get(j+1) == null))) {
                if((alldates.get(j).isAfter(alldates.get(j+1)))){
                    if (!(alldates.get(j).equals(alldates.get(j+1)))) {
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
     * @param request HTTP request from client
     * @return an OK response rendering the fetched trip onto the page, or an unauthorised message.
     */
    public Result fetch(Http.Request request) {

        JsonNode json = request.body().asJson();

        // The id taken from the json node, initialised as null for validation purposes.
        Integer parsedTripId;

        // Retrieving the id of the trip being fetched
        try {
            parsedTripId = Integer.parseInt(json.get(TRIP_ID).asText());
        } catch (NumberFormatException e) {
            return badRequest();
        }

        // Retrieving the trip which corresponds to the id parsed.
        Trip returnedTrip = Trip.find.byId(parsedTripId);

        // Verifying the existence of the trip being retrieved.
        if (returnedTrip == null) {
            return notFound();
        }

        return ok(Json.toJson(returnedTrip));
    }


    /**
     * Fetches all the trips for a specified user
     * @param id the id of the user requested
     * @return the list of trips as a JSON
     */
    public Result fetchAll(Long id) {
        List<Trip> trips = repository.fetchAllTrips(id);
        return ok(Json.toJson(trips));
    }


    /**
     * Deletes a trip from the user currently logged in.
     * @param request Http request from the client, from which the current user profile can be obtained.
     * @param id The id of the trip being deleted from a profile
     * @return
     */
    public Result destroy(Http.Request request, Long id) {

        String userId = request.session().getOptional(AUTHORIZED).orElseGet(null);

        // Check if a user is logged in.
        if (userId != null) {

            // Retrieve the profile having its trip removed.
            Profile profile = Profile.find.byId(Integer.valueOf(userId));

            // Retrieve the individual trip being deleted by its id.
            Trip trip = Trip.find.byId(id.intValue());

            // Check for query success.
            if (profile == null || trip == null) {
                return notFound();
            }

            boolean tripInProfile = false;
            for (Trip tempTrip : profile.getTrips()) {
                if (tempTrip.getId() == id) {
                    tripInProfile = true;
                }
            }
            if (!tripInProfile) {
                return forbidden();
            }

            // Repository method handling the database and object manipulation.
            repository.deleteTripFromProfile(profile, trip);

        } else {
            return unauthorized();
        }

        // Deletion successful.
        return ok();

    }
}
