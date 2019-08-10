package repositories;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.destinations.Destination;
import models.trips.TripDestination;

import java.util.List;

public class TripDestinationRepository extends BeanRepository<Long, TripDestination> {


    @Inject
    public TripDestinationRepository() {
        super(TripDestination.class, Ebean.getDefaultServer());
    }


    /**
     * Fetches a single TripDestination by the id given.
     *
     * @param tripDestinationId     the id of the TripDestination to be found.
     * @return                      the TripDestination object of the matching TripDestination.
     */
    public TripDestination fetch(Long tripDestinationId) {
        return super.findById(tripDestinationId);
    }


    /**
     * Fetches all the trip destinations that a selected destination is associated with.
     *
     * @param usedDestination   the destination selected to be queried for associated trip destinations.
     * @return                  a list of all the trip destinations found.
     */
    public List<TripDestination> findAllUsing(Destination usedDestination) {
        return query().where().eq("destination", usedDestination).findList();
    }
}
