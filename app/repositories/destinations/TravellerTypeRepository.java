package repositories.destinations;

import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.TravellerType;

public class TravellerTypeRepository extends BeanRepository<Long, TravellerType> {

    public TravellerTypeRepository() {
        super(TravellerType.class, Ebean.getServer("default"));
    }

}
