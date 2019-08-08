package repositories.destinations;

import io.ebean.BeanRepository;
import io.ebean.Ebean;
import io.ebean.Expression;
import io.ebean.ExpressionList;
import models.Profile;
import models.destinations.Destination;
import models.photos.PersonalPhoto;
import models.treasureHunts.TreasureHunt;
import repositories.ProfileRepository;

import com.google.inject.Inject;
import java.util.List;


public class DestinationRepository extends BeanRepository<Long, Destination> {

    private static final Long DEFAULT_ADMIN_ID = 1L;
    private static final String PHOTO_FIELD = "photoGallery.photo";

    private ProfileRepository profileRepository;

    @Inject
    public DestinationRepository(ProfileRepository profileRepository) {
        super(Destination.class, Ebean.getDefaultServer());
        this.profileRepository = profileRepository;
    }

    /**
     * Update the destination object.
     *
     * @param destination       the destination being updated.
     */
    @Override
    public void update(Destination destination) {
        super.update(destination);
    }

    /**
     * Retrieve a Destination by its Id.
     *
     * @param destinationId     the Id of the destination being retrieved.
     * @return                  the destination object corresponding with the destinationId.
     */
    public Destination fetch(Long destinationId) {
        return super.findById(destinationId);
    }


    /**
     * Finds all the destinations that contain a given photo.
     *
     * @param photo       the photo.
     * @return            list of destinations containing the photo.
     */
    public List<Destination> fetch(PersonalPhoto photo) {
        return query().where().eq(PHOTO_FIELD, photo).findList();
    }


    /**
     * Finds all the destinations that have proposed traveller types.
     *
     * @return      list of destinations that have proposed traveller types.
     */
    public List<Destination> fetchProposed() {
        return query()
            .select("destination")
            .where()
            .disjunction()
            .isNotEmpty("proposedTravellerTypesAdd")
            .isNotEmpty("proposedTravellerTypesRemove")
            .endJunction()
            .findList();
    }


    /**
     * Deletes the destination specified.
     *
     * @param destination       the destination to delete from the database.
     */
    @Override
    public boolean delete(Destination destination) {
        // Clear the destination photos
        destination.clearAllTravellerTypeSets();
        destination.clearPhotoGallery();
        super.update(destination);
        // Delete destination
        return super.delete(destination);
    }


    /**
     * Transfers the ownership of a destination to the default admin. Will be used when a public destination is used by
     * another user.
     *
     * @param destination   the destination to be changed ownership of.
     */
    public void transferDestinationOwnership(Destination destination) {
        Profile defaultAdmin = profileRepository.findById(DEFAULT_ADMIN_ID);
        Profile oldOwner = destination.getOwner();

        oldOwner.removeDestination(destination);
        defaultAdmin.addDestination(destination);

        destination.changeOwner(defaultAdmin);

        super.update(destination);
        profileRepository.update(oldOwner);
        profileRepository.update(defaultAdmin);

    }


    /**
     * Returns a list of Destinations that are equal, excluding the given Destination.
     *
     * @param destination   destination to search with.
     * @return              list of destinations that are equal.
     */
    public List<Destination> findEqual(Destination destination) {
        return query()
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

    public ExpressionList<Destination> getExpressionList() {
        return query().where();
    }
}
