package controllers.points;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import models.points.AchievementTracker;
import models.points.PointReward;
import models.profiles.Profile;
import models.util.ApiError;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.points.AchievementTrackerRepository;
import repositories.points.PointRewardRepository;
import repositories.profiles.ProfileRepository;
import util.AuthenticationUtil;

public class AchievementTrackerController extends Controller {

    private AchievementTrackerRepository achievementTrackerRepository;
    private ProfileRepository profileRepository;
    private PointRewardRepository pointRewardRepository;
    private ObjectMapper objectMapper;


    @Inject
    public AchievementTrackerController(AchievementTrackerRepository achievementTrackerRepository,
                                        ProfileRepository profileRepository,
                                        PointRewardRepository pointRewardRepository,
                                        ObjectMapper objectMapper) {
        this.achievementTrackerRepository = achievementTrackerRepository;
        this.profileRepository = profileRepository;
        this.pointRewardRepository = pointRewardRepository;
        this.objectMapper = objectMapper;
    }


    /**
     * Retrieves the achievement tracker for the given profile. If the profile currently does not have a tracker,
     * then one is created for the profile.
     * @param owner the profile who's achievement tracker is to be retrieved.
     * @return the profile's achievement tracker.
     */
    private AchievementTracker retrieveTracker(Profile owner) {
        AchievementTracker achievementTracker = achievementTrackerRepository.findUsing(owner);

        // As every profile should have a tracker, if they do not have one currently, make it.
        if (achievementTracker == null) {
            achievementTracker = new AchievementTracker();

            achievementTracker.setOwner(owner);
            achievementTrackerRepository.save(achievementTracker);

            owner.setAchievementTracker(achievementTracker);
            profileRepository.update(owner);
        }
        return achievementTracker;
    }


    /**
     * Adds the given amount of points to the given profile's AchievementTracker.
     * @param actingProfile the profile receiving points.
     * @param rewardName the name of action that the user performed to gain points.
     * @return the points added rewarded to the profile.
     */
    public int rewardAction(Profile actingProfile, String rewardName) {
        AchievementTracker achievementTracker = retrieveTracker(actingProfile);  // Get the tracker for the user.

        PointReward reward = pointRewardRepository.fetchPointReward(rewardName);    // Get the reward to add.
        achievementTracker.addPoints(reward.getValue());
        achievementTrackerRepository.update(achievementTracker);    // Update the tracker stored in the database.

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

        AchievementTracker tracker = retrieveTracker(requestedUser);

        ObjectNode pointsJson = objectMapper.createObjectNode();
        pointsJson.put("userPoints", tracker.getPoints());

        return ok(pointsJson);
    }
}
