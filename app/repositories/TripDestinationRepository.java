package repositories;


import io.ebean.Ebean;
import models.destinations.Destination;
import models.trips.TripDestination;
import java.util.Set;

public class TripDestinationRepository {

    /**
     * fetches one tripDestination thats contain the given destination for each unique trip id
     *
     * @param destination   the destination being searched for
     * @return              the set of tripDestinations found
     */
    public Set<TripDestination> fetchTripsContainingDestination(Destination destination) {
        return Ebean.find(TripDestination.class)
                .select("trip")
                .where()
                .eq("destination", destination)
                .setDistinct(true)
                .findSet();
    }
}
