package repositories.profiles;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import models.profiles.Nationality;


/**
 * Handles database interaction for nationalities.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class NationalityRepository extends BeanRepository<Long, Nationality> {

    public ExpressionList<Nationality> getExpressionList() {
        return query().where();
    }


    @Inject
    public NationalityRepository() {
        super(Nationality.class, Ebean.getDefaultServer());
    }
}
