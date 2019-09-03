package controllers.points;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import models.destinations.Destination;
import models.objectives.Objective;
import models.points.AchievementTracker;
import models.points.Action;
import models.points.PointReward;
import models.profiles.Profile;
import models.util.ApiError;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.points.PointRewardRepository;
import repositories.profiles.ProfileRepository;
import util.AuthenticationUtil;

public class AchievementTrackerController extends Controller {
    private ProfileRepository profileRepository;
    private PointRewardRepository pointRewardRepository;
    private ObjectMapper objectMapper;


    @Inject
    public AchievementTrackerController(ProfileRepository profileRepository,
                                        PointRewardRepository pointRewardRepository,
                                        ObjectMapper objectMapper) {
        this.profileRepository = profileRepository;
        this.pointRewardRepository = pointRewardRepository;
        this.objectMapper = objectMapper;
    }


    /**
     * Adds the given amount of points to the given profile's AchievementTracker.
     * @param actingProfile the profile receiving points.
     * @param destinationCreated the destination that was created.
     * @return the points added rewarded to the profile.
     */
    public int rewardAction(Profile actingProfile, Destination destinationCreated) {
        AchievementTracker achievementTracker = actingProfile.getAchievementTracker();  // Get the tracker for the user.

        PointReward reward = pointRewardRepository.findUsing(Action.DESTINATION_CREATED);    // Get the reward to add.
        achievementTracker.addPoints(reward.getValue());
        profileRepository.update(actingProfile);    // Update the tracker stored in the database.

        return reward.getValue();
    }


    /**
     * Adds the given amount of points to the given profile's AchievementTracker.
     * @param actingProfile the profile receiving points.
     * @param objectiveSolved the objective which the action was performed on.
     * @return the points added rewarded to the profile.
     */
    public int rewardAction(Profile actingProfile, Objective objectiveSolved, boolean checkedIn) {
        Action completedAction = checkedIn ? Action.CHECKED_IN : Action.RIDDLE_SOLVED;

        AchievementTracker achievementTracker = actingProfile.getAchievementTracker();  // Get the tracker for the user.

        PointReward reward = pointRewardRepository.findUsing(completedAction);    // Get the reward to add.

        achievementTracker.addPoints(reward.getValue());
        profileRepository.update(actingProfile);    // Update the tracker stored in the database.

        return reward.getValue();
    }

    /**
     * Retrieves the requested user's current points value.
     * @param request       the http request sent.
     * @param userId        the user whose points have been requested.
     * @return              ok() (Http 200) containing the user's points if successfully attained.
     *                      unauthorised() (Http 401) if the one sending the request is not logged in.
     *                      notFound() (Http 404) if the requested user is not found.
     *
     */
    public Result fetchPoints(Http.Request request, Long userId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        Profile requestedUser = profileRepository.findById(userId);

        if (requestedUser == null) {
            return notFound(ApiError.notFound());
        }

        AchievementTracker tracker = requestedUser.getAchievementTracker();

        ObjectNode pointsJson = objectMapper.createObjectNode();
        pointsJson.put("userPoints", tracker.getPoints());

        return ok(pointsJson);
    }
}