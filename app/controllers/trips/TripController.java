package controllers.trips;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.destinations.DestinationController;
import models.destinations.Destination;
import models.trips.Trip;
import models.trips.TripDestination;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TripController extends Controller {

    private static final String AUTHORIZED = "authorized";
    private static final String NAME = "name";
    private static final String TRIPDESTINATIONS = "trip_destinations";
    private static final String STARTDATE = "start_date";
    private static final String ENDDATE = "end_date";
    private static final String DESTINATIONID = "destination_id";
    private static final String NOTSIGNEDIN = "You are not logged in.";


    /**
     * Creates a trips for a user based on information sent in the http request
     * @param request json format of trip information
     * @return
     */
    public Result create(Http.Request request) throws IOException {

        JsonNode json = request.body().asJson();

        Trip trip = new Trip();

        trip.setName(json.get(NAME).asText());

        int order = 0;
        List<TripDestination> myDestinationList = new ArrayList<>();


        ObjectMapper mapper = new ObjectMapper();

        JsonNode tripDestinations = mapper.readTree(json.get(TRIPDESTINATIONS).asText());

//
//        ObjectMapper map = new ObjectMapper();
//        ObjectNode node = mapper.createObjectNode();
//
//        node.put("username", "TestUser123");
//        node.put("password", "TestPass");
//        node.put("first_name", "Test");
//        node.put("middle_name", "");
//        node.put("last_name", "Dummy");
//        node.put("date_of_birth", "2000-01-01");
//        node.put("gender", "other");
//
//        //json.putArray();


        System.out.println(tripDestinations);
        for (JsonNode tripDestination : tripDestinations) {
            TripDestination destination = new TripDestination();
            destination.setList_order(order++);
            destination.setDestination(Destination.find.byId(Integer.parseInt(tripDestination.get(DESTINATIONID).asText())));
            destination.setStartDate(LocalDate.parse(tripDestination.get(STARTDATE).asText()));
            destination.setEndDate(LocalDate.parse(tripDestination.get(ENDDATE).asText()));
            myDestinationList.add(destination);
        }

        trip.setDestinations(myDestinationList);
        trip.save();

        return ok();
    };


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

}
