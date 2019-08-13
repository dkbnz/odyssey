package repositories.photos;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.photos.PersonalPhoto;


/**
 * Handles database interaction for personal photos.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class PersonalPhotoRepository extends BeanRepository<Long, PersonalPhoto> {

    @Inject
    public PersonalPhotoRepository() {
        super(PersonalPhoto.class, Ebean.getDefaultServer());
    }
}
