package repositories.photos;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.photos.PersonalPhoto;

public class PersonalPhotoRepository extends BeanRepository<Long, PersonalPhoto> {

    @Inject
    public PersonalPhotoRepository() {
        super(PersonalPhoto.class, Ebean.getDefaultServer());
    }
}
