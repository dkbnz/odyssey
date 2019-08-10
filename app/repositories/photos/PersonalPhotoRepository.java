package repositories.photos;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.Profile;
import models.photos.PersonalPhoto;

public class PersonalPhotoRepository extends BeanRepository<Long, PersonalPhoto> {


    @Inject
    public PersonalPhotoRepository() {
        super(PersonalPhoto.class, Ebean.getDefaultServer());
    }

    /**
     * Fetches a single personal photo by the photo id number.
     *
     * @param photoId   the id number of the photo.
     * @return          the PersonalPhoto object given by the personal photo.
     */
    public PersonalPhoto fetch(Long photoId) {
        return super.findById(photoId);
    }


    /**
     * Updates the privacy of the of the specified photo to either public or private.
     *
     * @param photoOwner the Profile object of the owner of the photo.
     * @param photo      the PersonalPhoto object of the photo.
     * @param isPublic   the new Boolean value of the photos public or private value.
     */
    public void updatePrivacy(Profile photoOwner, PersonalPhoto photo, String isPublic) {
        photo.setPublic(Boolean.parseBoolean(isPublic));
        photo.update();
        photoOwner.update();
    }
}
