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

    /**
     * Retrieves a destination type by its ID.
     *
     * @param typeId    the ID of the desired destination type.
     * @return          the destination type matching the ID.
     */
    public DestinationType fetch(Long typeId) {
        return super.findById(typeId);
    }
}
