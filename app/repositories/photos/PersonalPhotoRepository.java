package repositories.photos;

import models.Profile;
import models.photos.PersonalPhoto;

public class PersonalPhotoRepository {
    /**
     * Fetches a single personal photo by the photo id number.
     *
     * @param photoId   the id number of the photo.
     * @return          the PersonalPhoto object given by the personal photo.
     */
    public PersonalPhoto fetch(Long photoId) {
        return PersonalPhoto.getFind().byId(photoId.intValue());
    }

    /**
     * Deletes a photo associated with a profile, doesn't delete the uploaded photo.
     *
     * @param photoOwner    the Profile object of the owner of the photo.
     * @param photo         the PersonalPhoto object of the photo.
     */
    public void delete(Profile photoOwner, PersonalPhoto photo) {
        photoOwner.removePhotoFromGallery(photo);
        photo.delete();
        photoOwner.update();
    }
}
