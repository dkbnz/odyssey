package repositories.destinations;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.destinations.Type;


/**
 * Handles database interaction for destination types.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class DestinationTypeRepository extends BeanRepository<Long, Type> {

    @Inject
    public DestinationTypeRepository() {
        super(Type.class, Ebean.getDefaultServer());
    }
}
