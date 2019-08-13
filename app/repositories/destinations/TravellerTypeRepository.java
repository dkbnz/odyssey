package repositories.destinations;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.TravellerType;


/**
 * Handles database interaction for traveller types.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class TravellerTypeRepository extends BeanRepository<Long, TravellerType> {

    @Inject
    public TravellerTypeRepository() {
        super(TravellerType.class, Ebean.getDefaultServer());
    }
}
