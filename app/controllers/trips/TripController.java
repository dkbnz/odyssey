package controllers.trips;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
    private static final String TRIPDESTINATIONS = "trip_destinations";
    private static final String STARTDATE = "start_date";
    private static final String ENDDATE = "end_date";
    private static final String DESTINATIONID = "destination_id";
    private static final String NOTSIGNEDIN = "You are not logged in.";


    /**
     * Creates a trips for a user based on information sent in the http request
     * @param request json format of trip information
     * @return OK response for successful creation, or badRequest.
     */
    public Result create(Http.Request request) {

        JsonNode json = request.body().asJson();


        // Check if the request contains a trip name and an array of destinations.
        if (!(json.has(NAME) && json.has(TRIPDESTINATIONS))) {
            return badRequest();
        }


        // Check if the array of destinations in the request contains at least two trips.
        if (!(json.get(TRIPDESTINATIONS) == null || json.get(TRIPDESTINATIONS).size() >= 2)) {
            return badRequest();
        }


        // Create a trip object and give it the name extracted from the request.
        Trip trip = new Trip();
        trip.setName(json.get(NAME).asText());


        // Create an empty List for TripDestination objects to be populated from the request.
        List<TripDestination> destinationList = new ArrayList<>();


        // Create a json node for the destinations contained in the trip to use for iteration.
        ArrayNode tripDestinations = (ArrayNode) json.get(TRIPDESTINATIONS);


        // Check if the array can be processed so the main loop can be run.
        if (tripDestinations.isArray()) {

            int order = 0;

            // Parse JSON to create and append trip destinations using iterator.
            Iterator<JsonNode> iterator = tripDestinations.elements();
            while (iterator.hasNext()) {
                // Set the current node having its contents extracted.
                JsonNode destinationJson = iterator.next();

                // Parse the values contained in the current node of the array
                Integer parsedDestinationId = Integer.parseInt(destinationJson.get(DESTINATIONID).asText());
                Destination parsedDestination = Destination.find.byId(parsedDestinationId);
                LocalDate parsedStartDate = LocalDate.parse(destinationJson.get(STARTDATE).asText());
                LocalDate parsedEndDate = LocalDate.parse(destinationJson.get(ENDDATE).asText());

                // Create a new TripDestination object and set the values to be those parsed.
                TripDestination newTripDestination = new TripDestination();
                newTripDestination.setListOrder(order++);
                newTripDestination.setDestination(parsedDestination);
                newTripDestination.setStartDate(parsedStartDate);
                newTripDestination.setEndDate(parsedEndDate);

                // Add created destination to the list of trip destinations.
                destinationList.add(newTripDestination);
            }

        } else {
            return badRequest();
        }

        // Set the trip destinations to be the array of TripDestination parsed, save the trip, and return OK.
        trip.setDestinations(destinationList);
        trip.save();
        return ok();
    }












    /**
     * Fetches a single trip from the database for a logged in user.
     * TODO: provide returned OK Results a view to render single trips on.
     *
     * @param request HTTP request from client
     * @return an OK response rendering the fetched trip onto the page, or an unauthorised message.
     */
    public Result fetch(Http.Request request) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(tripId -> {
                    Trip trip = Trip.find.byId(Integer.valueOf(tripId));
                    return ok(AUTHORIZED);//views.html.trip.render(trip));
                })
                .orElseGet(() -> unauthorized(NOTSIGNEDIN));
    }


//    {
//        "trip_name": "ExampleName",
//            "trip_destinations": [
//        {
//            "destination_id": "15040",
//                "start_date": "2019-09-20",
//                "end_date": "2019-10-20"
//        },
//        {
//            "destination_id": "15042",
//                "start_date": "2019-10-20",
//                "end_date": "2019-11-14"
//        }
//  ]
//    }



}
