package repositories.destinations;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.TravellerType;

public class TravellerTypeRepository extends BeanRepository<Long, TravellerType> {

    public TravellerTypeRepository() {
        super(TravellerType.class, Ebean.getServer("default"));
    }

}
