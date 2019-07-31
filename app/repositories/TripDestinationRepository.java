package repositories;

import io.ebean.Ebean;
import models.destinations.Destination;
import models.trips.TripDestination;

import java.util.List;

public class TripDestinationRepository {

    /**
     * Fetches one tripDestination thats contain the given destination for each unique trip id.
     *
     * @param destination   the destination being searched for.
     * @return              the set of tripDestinations found.
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
     * Fetches a single TripDestination by the id given.
     *
     * @param tripDestinationId     the id of the TripDestination to be found.
     * @return                      the TripDestination object of the matching TripDestination.
     */
    public TripDestination fetch(Long tripDestinationId) {
        return TripDestination.find.byId(tripDestinationId.intValue());
    }


    /**
     * Save the TripDestination object.
     *
     * @param tripDestination       the TripDestination being saved.
     */
    public void save(TripDestination tripDestination) {
        tripDestination.save();
    }


    /**
     * Deletes the trip destination.
     *
     * @param tripDestination       the TripDestination to be deleted.
     */
    public void delete(TripDestination tripDestination) {
        tripDestination.delete();
    }


    /**
     * Updates the TripDestination object.
     *
     * @param tripDestination       the TripDestination being updated.
     */
    public void update(TripDestination tripDestination) {
        tripDestination.update();
    }
}
