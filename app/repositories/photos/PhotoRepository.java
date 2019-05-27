package repositories.photos;

import models.photos.Photo;

public class PhotoRepository {

    /**
     * Saves a photo to the database.
     *
     * @param photo the photo to be saved.
     */
    public void save(Photo photo) {
        photo.save();
    }
}
