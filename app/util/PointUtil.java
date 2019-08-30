package util;

import io.ebean.Ebean;
import models.points.AchievementTracker;
import models.profiles.Profile;

public final class PointUtil {
    /**
     * Finds the rank of a given profile based on the points in their AchievementTracker
     *
     * @param profileToFind the profile to find the rank of.
     * @return  an integer representing the rank of a profile.
     */
    public static int getRank(Profile profileToFind) {
        // TODO: Doug, find the right place to put this.
        // Needs to be called from the AchievementTracker model as allows toJson to evaluate rank on the fly
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