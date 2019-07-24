package repositories.destinations;

import io.ebean.Ebean;
import models.Profile;
import models.destinations.Destination;
import models.photos.PersonalPhoto;

import java.util.List;

public class DestinationRepository {

    private static final int DEFAULT_ADMIN_ID = 1;
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
     * Save the destination object.
     *
     * @param destination       the destination being saved.
     */
    public void save(Destination destination) {
        destination.save();
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
     * Deletes the destination specified.
     *
     * @param destination       the destination to delete from the database.
     */
    public void delete(Destination destination) {
        // Clear the destination photos
        destination.clearPhotoGallery();
        destination.update();
        // Delete destination
        destination.delete();
    }


    public void safeDelete(Destination destination) {
        // Clear the destination photos
        destination.clearPhotoGallery();
        destination.update();
        //Clear the trip destinations
        System.out.println(destination.getTripDestinations().size());
        System.out.println(fetch(destination.getId()).getTripDestinations().size());
        destination.clearTripDestinations();
        destination.update();
        System.out.println(destination.getTripDestinations().size());
        System.out.println(fetch(destination.getId()).getTripDestinations().size());
        // Delete destination
        destination.delete();
    }


    /**
     * Transfers the ownership of a destination to the default admin. Will be used when a public destination is used by
     * another user.
     *
     * @param destination the destination to be changed ownership of.
     */
    public void transferDestinationOwnership(Destination destination) {
        Profile defaultAdmin = Profile.find.byId(DEFAULT_ADMIN_ID);

        destination.changeOwner(defaultAdmin);

        update(destination);
    }


    /**
     * Returns a list of Destinations that are equal, excluding the given Destination
     *
     * @param destination   Destination to search with.
     * @return              List of destinations that are equal.
     */
    public List<Destination> findEqual(Destination destination) {
        return Ebean.find(Destination.class)
                .where()
                .eq("name", destination.getName())
                .eq("type", destination.getType())
                .eq("district", destination.getDistrict())
                .eq("latitude", destination.getLatitude())
                .eq("longitude", destination.getLongitude())
                .eq("country", destination.getCountry())
                .ne("id", destination.getId())
                .findList();
    }
}
