package repositories.points;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.points.Action;
import models.points.PointReward;

public class PointRewardRepository extends BeanRepository<Long, PointReward> {

    private static final String NAME = "name";

    @Inject
    public PointRewardRepository() {
        super(PointReward.class, Ebean.getDefaultServer());
    }

    /**
     * Fetches the point reward correlating the given
     * @param action the action that the points are being awarded for.
     * @return the PointReward object relating to that action.
     */
    public PointReward findUsing(Action action) {
        return query().where()
                .eq(NAME, action)
                .findOne();
    }
}
