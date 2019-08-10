package repositories.destinations;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.TravellerType;

public class TravellerTypeRepository extends BeanRepository<Long, TravellerType> {

    @Inject
    public TravellerTypeRepository() {
        super(TravellerType.class, Ebean.getDefaultServer());
    }
}
