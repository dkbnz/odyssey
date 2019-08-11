package repositories.destinations;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.destinations.DestinationType;


/**
 * Handles database interaction for destination types.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class DestinationTypeRepository extends BeanRepository<Long, DestinationType> {

    @Inject
    public DestinationTypeRepository() {
        super(DestinationType.class, Ebean.getDefaultServer());
    }
}
