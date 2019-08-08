package repositories.photos;

import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.photos.Photo;

public class PhotoRepository extends BeanRepository<Long, Photo> {

    public PhotoRepository() {
        super(Photo.class, Ebean.getServer("default"));
    }

    /**
     * Saves a photo to the database.
     *
     * @param photo the photo to be saved.
     */
    public void save(Photo photo) {
        photo.save();
    }
}
