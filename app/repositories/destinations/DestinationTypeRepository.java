package repositories.destinations;

import models.destinations.DestinationType;

public class DestinationTypeRepository {

    /**
     * Retrieves a destination type by its ID.
     *
     * @param typeId the ID of the desired destination type.
     * @return the destination type matching the ID.
     */
    public DestinationType fetch(Long typeId) {
        return DestinationType.find.byId(typeId.intValue());
    }
}
