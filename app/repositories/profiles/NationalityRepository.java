package repositories.profiles;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.profiles.Nationality;


/**
 * Handles database interaction for nationalities.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class NationalityRepository extends BeanRepository<Long, Nationality> {

    @Inject
    public NationalityRepository() {
        super(Nationality.class, Ebean.getDefaultServer());
    }
}
