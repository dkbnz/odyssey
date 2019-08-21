package repositories.trips;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.destinations.Destination;
import models.trips.TripDestination;

import java.util.List;

/**
 * Handles database interaction for trip destinations.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class TripDestinationRepository extends BeanRepository<Long, TripDestination> {


    @Inject
    public TripDestinationRepository() {
        super(TripDestination.class, Ebean.getDefaultServer());
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
