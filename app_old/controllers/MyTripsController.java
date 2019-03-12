package controllers;

import models.Destination;
import play.mvc.Controller;
import play.mvc.Result;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to for the My Trips page functionality
 */
public class MyTripsController extends Controller {


    List<Destination> tripEU = new ArrayList<>();
    List<Destination> tripUK = new ArrayList<>();


    /**
     * Add destination objects to lists of trips for displaying in table. This method is used as is similar to how a
     * database will produce data to display.
     */
    public MyTripsController() {
        Destination newDestination1 = new Destination(
                "Duomo di Milano", "Monument", "Milan", 45.463214, 9.191980, "Italy");
        Destination newDestination2 = new Destination(
                "Colosseum", "Monument", "Rome", 41.890350, 12.492217, "Italy");
        Destination newDestination3 = new Destination(
                "Pompeii", "Suburb", "Naples", 40.749313, 14.484798, "Italy");
        Destination newDestination4 = new Destination(
                "London Eye", "Activity", "London", 51.503311, -0.119618, "England");
        Destination newDestination5 = new Destination(
                "Roman Baths", "Activity", "Bath", 51.381155, -2.359610, "England");
        Destination newDestination6 = new Destination(
                "O'Connell Monument", "Monument", "Dublin", 53.347698, -6.259283, "Ireland");
        tripEU.add(newDestination1);
        tripEU.add(newDestination2);
        tripEU.add(newDestination3);
        tripUK.add(newDestination4);
        tripUK.add(newDestination5);
        tripUK.add(newDestination6);
    }
    /**
     * Handle default path requests, parses list of trips to trips.scala.html.
     */
    public Result trips() {
        return ok(views.html.trips.render(tripEU, tripUK));
    }
}
