package models.points.gaincommands;

import com.google.inject.Inject;
import controllers.points.AchievementTrackerController;
import models.profiles.Profile;

public class SolveRiddleCommand implements CompleteObjectiveCommand {

    /**
     * The number of points
     */
    private static final int POINT_REWARD = 5;
    private Profile profileActing;



    private AchievementTrackerController achievementTrackerController;

    @Inject
    public SolveRiddleCommand(AchievementTrackerController achievementTrackerController, Profile profileActing) {
        this.achievementTrackerController = achievementTrackerController;
        this.profileActing = profileActing;
    }


    /**
     * Calls the AchievementTrackerController's completeAction method to award a profile points. The profile that performed the action
     * and the amount of points that action is worth are both passed through.
     * @return whether the result from the complete action operation.
     */
    @Override
    public Integer execute() {
        achievementTrackerController.completeAction(profileActing, POINT_REWARD);
        return POINT_REWARD;
    }

}
