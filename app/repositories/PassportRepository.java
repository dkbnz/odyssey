package repositories;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.Passport;

public class PassportRepository extends BeanRepository<Long, Passport> {

    @Inject
    public PassportRepository() {
        super(Passport.class, Ebean.getDefaultServer());
    }

}
