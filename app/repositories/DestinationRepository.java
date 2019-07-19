package repositories;

import models.Profile;
import models.destinations.Destination;
import models.photos.PersonalPhoto;
import play.mvc.Result;

import java.util.List;

public class DestinationRepository {

    private static final String PHOTO_FIELD = "photoGallery.photo";

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

    /**
     * Finds all the destinations that contain a given photo.
     *
     * @param photo       the photo.
     * @return            list of destinations containing the photo.
     */
    public List<Destination> fetch(PersonalPhoto photo) {
        return Destination.find.query().where().eq(PHOTO_FIELD, photo).findList();
    }


    /**
     * Transfers the ownership of a destination to the default admin. Will be used when a public destination is used by
     * another user.
     *
     * @param destination the destination to be changed ownership of.
     * @return            notFound() (Http 404) if destination could not found, ok() (Http 200) if successfully updated.
     */
    public void transferDestinationOwnership(Destination destination) {
        Profile defaultAdmin = Profile.find.byId(1);

        destination.changeOwner(defaultAdmin);

        destination.save();
    }

    public void delete(Destination destination) {
        // Clear the destination photos
        destination.clearPhotoGallery();
        destination.update();
        // Delete destination
        destination.delete();
    }
}
