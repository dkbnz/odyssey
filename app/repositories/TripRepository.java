package repositories;

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
}
