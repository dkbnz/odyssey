package repositories;

import io.ebean.ExpressionList;
import models.destinations.Destination;
import models.trips.Trip;
import models.trips.TripDestination;
import java.util.List;


public class TripRepository {

    /**
     * Removes the existing TripDestinations from a trip being edited.
     * Is used before parsing the request body in edit which will contain all the updated trips, "cleaning the slate".
     * @param trip the trip having its destinations deleted before editing
     */
    public void removeTripDestinations(Trip trip) {

        // Just get its list of destinations.
        List<TripDestination> destinations = trip.getDestinations();

        // Go through and delete each one from the database.
        for (TripDestination destination : destinations) {
            destination.delete();
        }

    }

    /**
     * Finds all the trips with a specified user id
     * @param id the profile id
     * @return the list of trips
     */
    public List<Trip> fetchAllTrips(Long id) {

        List<Trip> trips;

        // Creates a list of trips from a query based on profile id
        ExpressionList<Trip> expressionList = Trip.find.query().where();
        expressionList.eq("profile_id", id);
        trips = expressionList.findList();

        return trips;
    }
}
