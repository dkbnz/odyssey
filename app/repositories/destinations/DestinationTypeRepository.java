package repositories.destinations;

import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.destinations.DestinationType;

public class DestinationTypeRepository extends BeanRepository<Long, DestinationType> {

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
        return DestinationType.find.byId(typeId.intValue());
    }
}
