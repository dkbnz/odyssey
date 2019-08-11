package repositories.photos;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.photos.Photo;


/**
 * Handles database interaction for photos.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class PhotoRepository extends BeanRepository<Long, Photo> {

    @Inject
    public PhotoRepository() {
        super(Photo.class, Ebean.getDefaultServer());
    }
}
