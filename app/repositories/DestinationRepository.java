package repositories;

import models.destinations.Destination;
import models.photos.PersonalPhoto;

public class DestinationRepository {

    /**
     * Add a photo object to a destination object, before updating the destination in our database.
     *
     * @param destinationId     the Id of the destination being retrieved.
     * @param photo             the photo being added to the retrieved destination.
     */
    public boolean addPhoto(Long destinationId, PersonalPhoto photo) {
        Destination destination = Destination.find.byId(destinationId.intValue());
        if (destination == null) {
            return false;
        } else {
            destination.addPhotoToGallery(photo);
            destination.update();
            return true;
        }
    }

}
