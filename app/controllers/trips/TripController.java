package controllers.trips;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Profile;
import models.destinations.Destination;
import models.trips.Trip;
import models.trips.TripDestination;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

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
    private static final String ID = "id";
    private static final String NOT_LOGGED_IN = "You are not logged in.";
    private static final int MINIMUM_TRIP_DESTINATIONS = 2;


    /**
     * Creates a trips for a user based on information sent in the http request
     * @param request json format of trip information
     * @return OK response for successful creation, or badRequest.
     * TODO: Link created trips to a profile as per the database schema
     */
    public Result create(Http.Request request) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {
                    // User is logged in
                    Profile userProfile = Profile.find.byId(Integer.valueOf(userId));

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
                    if (destinationList != null) {
                        trip.setDestinations(destinationList);
                        trip.save();
                        userProfile.addTrip(trip);
                        return ok();
                    } else {
                        return badRequest();
                    }

                })
                .orElseGet(() -> unauthorized());
    }





    /**
     * Method for looking at the contents of the main json body for a trip in a request.
     * NOTE: Does not examine array contents.
     * @param json the json body of a request received
     * @return false json doesn't contain a name or an array of destinations with at least two nodes, or else return true.
     */
    public boolean isValidTrip(JsonNode json) {

        // Check if the request contains a trip name and an array of destinations.
        if (!(json.has(NAME) && json.has(TRIP_DESTINATIONS))) {
            return false;
        }

        // Check if the array of destinations in the request contains at least two trips.
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

            // Trip being modified
            Trip trip = Trip.find.byId(id.intValue());


            JsonNode json = request.body().asJson();

            if (!isValidTrip(json)) {
                return badRequest();
            }

            String name = json.get(NAME).asText();
            trip.setName(name);


            // Create a json node for the destinations contained in the trip to use for iteration.
            ArrayNode tripDestinations = (ArrayNode) json.get(TRIP_DESTINATIONS);


            // Create an empty List for TripDestination objects to be populated from the request.
            List<TripDestination> destinationList = parseTripDestinations(tripDestinations);

            if (destinationList != null) {
                trip.setDestinations(destinationList);
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
     * @param tripDestinations
     * @return an array of destinations
     */
    public List<TripDestination> parseTripDestinations(ArrayNode tripDestinations) {

        List<TripDestination> result = new ArrayList<>();

        // Simple integer for incrementing the list_order attribute for trip destinations.
        int order = 0;
        long previousDestination = -1;

        // Parse JSON to create and append trip destinations using an iterator.
        Iterator<JsonNode> iterator = tripDestinations.elements();
        while (iterator.hasNext()) {
            // Set the current node having its contents extracted.
            JsonNode destinationJson = iterator.next();

            // Check if current node has a destination ID, and it corresponds with a destination in our database.
            if (destinationJson.get(DESTINATION_ID) != null && destinationJson.get(DESTINATION_ID).asLong() != previousDestination &&
                    Destination.find.query()
                            .where()
                            .like(ID, DESTINATION_ID)
                            .findOne() != null) {

                // Parse the values contained in the current node of the array
                Integer parsedDestinationId = Integer.parseInt(destinationJson.get(DESTINATION_ID).asText());
                LocalDate parsedStartDate = LocalDate.parse(destinationJson.get(START_DATE).asText());
                LocalDate parsedEndDate = LocalDate.parse(destinationJson.get(END_DATE).asText());
                Destination parsedDestination = Destination.find.byId(parsedDestinationId);

                // Create a new TripDestination object and set the values to be those parsed.
                TripDestination newTripDestination = new TripDestination();
                newTripDestination.setDestination(parsedDestination);
                newTripDestination.setStartDate(parsedStartDate);
                newTripDestination.setEndDate(parsedEndDate);
                newTripDestination.setListOrder(order++);

                // Add created destination to the list of trip destinations.
                result.add(newTripDestination);
                previousDestination = newTripDestination.getId();
            } else {
                return null;
            }
        }

        return  result;
    }



    /**
     * Fetches a single trip from the database for a logged in user.
     * TODO: provide returned OK Results a view to render single trips on.
     *
     * @param request HTTP request from client
     * @return an OK response rendering the fetched trip onto the page, or an unauthorised message.
     */
    public Result fetch(Http.Request request) {

        JsonNode json = request.body().asJson();

        Integer parsedTripId;
        try {
            parsedTripId = Integer.parseInt(json.get(TRIP_ID).asText());
        } catch (NumberFormatException e) {
            return badRequest();
        }

        Trip returnedTrip;

        String id = String.valueOf(parsedTripId);
        if (Trip.find.query()
                .where()
                .like(id, TRIP_ID)
                .findOne() != null) {
            returnedTrip = Trip.find.byId(parsedTripId);
        } else {
            return notFound();
        }

        return ok();
    }
}
