package repositories.destinations;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.destinations.DestinationType;

public class DestinationTypeRepository extends BeanRepository<Long, DestinationType> {

    @Inject
    public DestinationTypeRepository() {
        super(DestinationType.class, Ebean.getDefaultServer());
    }
}
