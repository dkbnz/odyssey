package repositories.hints;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.hints.Hint;

/**
 * Handles database interaction for hints.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class HintRepository extends BeanRepository<Long, Hint> {

    @Inject
    public HintRepository() {
        super(Hint.class, Ebean.getDefaultServer());
    }
}
