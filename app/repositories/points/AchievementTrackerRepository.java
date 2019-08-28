package repositories.points;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.points.AchievementTracker;
import models.profiles.Profile;

public class AchievementTrackerRepository extends BeanRepository<Long, AchievementTracker> {

    /**
     * The owner field name in the database.
     */
    private static final String OWNER_QUERY = "owner";

    @Inject
    public AchievementTrackerRepository() {
        super(AchievementTracker.class, Ebean.getDefaultServer());
    }


    /**
     * Find the Achievement Tracker associated with the given profile.
     * @param requestedProfile the profile as to whose achievement tracker we want to view.
     * @return the Achievement Tracker for the profile.
     */
    public AchievementTracker findUsing(Profile requestedProfile) {
        return query().where()
                .eq(OWNER_QUERY, requestedProfile)
                .findOne();
    }

}
