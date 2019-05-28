package repositories;

import models.destinations.Destination;
import models.photos.PersonalPhoto;

import java.util.List;

public class DestinationRepository {

    /**
     * Add a photo object to a destination object, before updating the destination in our database.
     *
     * @param destinationId     the Id of the destination being updated.
     * @param photo             the photo being added to the retrieved destination.
     * @return                  true if the photo is successfully added to the destination, or false otherwise.
     */
    public boolean photoSuccessfullyAdded(Long destinationId, PersonalPhoto photo) {
        Destination destination = Destination.find.byId(destinationId.intValue());
        if (destination == null) {
            return false;
        } else {
            destination.addPhotoToGallery(photo);
            destination.update();
            return true;
        }
    }


    /**
     * Remove a photo from a destination object, before updating the destination in our database.
     *
     * @param destinationId     the Id of the destination being updated.
     * @param photo             the photo being removed from the retrieved destination.
     * @return                  true if the photo is successfully removed from the destination, or false otherwise.
     */
    public boolean photoSuccessfullyRemoved(Long destinationId, PersonalPhoto photo) {
        Destination destination = Destination.find.byId(destinationId.intValue());
        if (destination == null) {
            return false;
        } else {
            destination.removePhotoFromGallery(photo);
            destination.update();
            return true;
        }
    }


    /**
     * Retrieve the gallery of photos from a destination.
     *
     * @param destinationId     the Id of the destination having its gallery retrieved.
     * @return                  a list of PersonalPhoto objects, which is the destination's photo gallery.
     */
    public List<PersonalPhoto> getDestinationPhotos(Long destinationId) {
        Destination destination = Destination.find.byId(destinationId.intValue());
        return destination.getPhotoGallery();
    }

}
