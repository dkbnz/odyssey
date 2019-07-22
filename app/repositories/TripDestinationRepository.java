package repositories;


import io.ebean.Ebean;
import models.destinations.Destination;
import models.trips.TripDestination;

import java.util.List;

public class TripDestinationRepository {

    /**
     * Fetches one tripDestination thats contain the given destination for each unique trip id
     *
     * @param destination   the destination being searched for
     * @return              the set of tripDestinations found
     */
    public List<TripDestination> fetchTripsContainingDestination(Destination destination) {

        return Ebean.find(TripDestination.class)
                .select("trip")
                .where()
                .eq("destination", destination)
                .setDistinct(true)
                .findList();
    }

    /**
     * Save the TripDestination object.
     *
     * @param tripDestination       the TripDestination being saved.
     */
    public void save(TripDestination tripDestination) {
        tripDestination.save();
    }
}
