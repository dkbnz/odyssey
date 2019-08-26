package repositories.points;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import models.points.AchievementTracker;

public class AchievementTrackerRepository extends BeanRepository<Long, AchievementTracker> {

    private static final String PROFILE_ID = "profile_id";
    private static final String ACHIEVEMENT_TRACKER_ID = "id";

    @Inject
    public AchievementTrackerRepository() {
        super(AchievementTracker.class, Ebean.getDefaultServer());
    }

    public ExpressionList<AchievementTracker> getExpressionlist() {
        return query().where();
    }

    public Long fetchAchievementTrackerOwner(Long achievementTrackerId) {
        //return query().select()
        return null;
        // TODO Matthew Implement
    }

}
