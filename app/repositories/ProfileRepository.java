package repositories;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import models.Profile;

public class ProfileRepository extends BeanRepository<Long, Profile> {

    @Inject
    public ProfileRepository() {
        super(Profile.class, Ebean.getDefaultServer());
    }

    public ExpressionList<Profile> getExpressionList() {
        return query().where();
    }
}