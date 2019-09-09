package repositories.points;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.points.AchievementTracker;
import models.profiles.Profile;


/**
 * Handles database interaction for AchievementTrackers.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class AchievementTrackerRepository extends BeanRepository<Long, AchievementTracker> {

    private static final String POINTS = "points";

    @Inject
    public AchievementTrackerRepository() {
        super(AchievementTracker.class, Ebean.getDefaultServer());
    }


    /**
     * Returns the number of points from a given rank
     * @param rank the rank to find the points for.
     * @return the number of points relating to that rank. If the given rank is not found, return 0
     */
    public int getPointsFromRank(int rank) {
        AchievementTracker achievementTracker = query()
                .setDistinct(true) // Merge rows that have identical points
                .select(POINTS) // Only selecting points field in the achievement tracker
                .where()
                .orderBy()
                .desc(POINTS) // Order by points in ascending order
                .setFirstRow(rank - 1) // Set the first row to be returned to be rank - 1 (due to zero indexing)
                .setMaxRows(1) // We only want one row, no need for others
                .findOne();

        if (achievementTracker == null) {
            return 0;
        }

        return achievementTracker.getPoints();
    }


    /**
     * Finds the rank of a given profile based on the points in their AchievementTracker
     *
     * @param profileToFind the profile to find the rank of.
     * @return  an integer representing the rank of a profile.
     */
    public static int getRank(Profile profileToFind) {
        return Ebean.getDefaultServer()
                .find(AchievementTracker.class)
                .setDistinct(true)
                .select("points")
                .where()
                .ge("points", profileToFind
                        .getAchievementTracker()
                        .getPoints())
                .findCount();
    }
}
