package repositories;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.Nationality;

public class NationalityRepository extends BeanRepository<Long, Nationality> {

    @Inject
    public NationalityRepository() {
        super(Nationality.class, Ebean.getDefaultServer());
    }
}
