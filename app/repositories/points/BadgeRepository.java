package repositories.points;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.points.Action;
import models.points.Badge;

public class BadgeRepository extends BeanRepository<Long, Badge> {

    private static final String NAME = "name";

    @Inject
    public BadgeRepository() {
        super(Badge.class, Ebean.getDefaultServer());
    }

    
    public Badge findUsing(Action action) {
        return query().where()
                .eq(NAME, action)
                .findOne();
    }
}
