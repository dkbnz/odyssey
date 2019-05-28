package repositories;

import models.destinations.Destination;

public class DestinationRepository {

    /**
     * Update the destination object.
     *
     * @param destination       the destination being updated.
     */
    public void update(Destination destination) {
        destination.update();
    }


    /**
     * Retrieve a Destination by its Id.
     *
     * @param destinationId     the Id of the destination being retrieved.
     * @return                  the destination object corresponding with the destinationId.
     */
    public Destination fetch(Long destinationId) {
        return Destination.find.byId(destinationId.intValue());
    }
}
