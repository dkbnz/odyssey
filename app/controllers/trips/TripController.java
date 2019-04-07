package controllers.trips;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.destinations.DestinationController;
import models.destinations.Destination;
import models.trips.Trip;
import models.trips.TripDestination;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TripController extends Controller {

    private static final String AUTHORIZED = "authorized";
    private static final String NAME = "name";
    private static final String TRIPDESTINATIONS = "trip_destinations";
    private static final String NOTSIGNEDIN = "You are not logged in.";


    /**
     * Creates a trips for a user based on information sent in the http request
     * @param request json format of trip information
     * @return
     */
    public Result create(Http.Request request) {
//
//        JsonNode json = request.body().asJson();
//
//        json.get(NAME);


        // CREATE Trip in database ^

        // GET back ID of trip

        // PLACE this ID for each trip destination

        //JSONArray trip_destination_array = new JSONArray(json.get(TRIPDESTINATIONS));

        Trip trip = new Trip();

        trip.setName("TEST TRIP");

        TripDestination dest1 = new TripDestination();

        dest1.setList_order(0);
        dest1.setStartDate(LocalDate.now());
        dest1.setDestination(Destination.find.byId(15040));

        TripDestination dest2 = new TripDestination();

        dest2.setList_order(1);
        dest2.setEndDate(LocalDate.now());
        dest2.setDestination(Destination.find.byId(15042));

        List<TripDestination> myList = new ArrayList<>();
        myList.add(dest1);
        myList.add(dest2);

        trip.setDestinations(myList);

        trip.save();


        return ok("Hello 2");
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
                    return ok(views.html.trip.render(trip));
                })
                .orElseGet(() -> unauthorized(NOTSIGNEDIN));
    }

}
