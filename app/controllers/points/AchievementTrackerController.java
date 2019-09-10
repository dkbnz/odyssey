package controllers.points;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import models.destinations.Destination;
import models.objectives.Objective;
import models.points.AchievementTracker;
import models.points.Action;
import models.points.Badge;
import models.points.PointReward;
import models.profiles.Profile;
import models.quests.Quest;
import models.trips.Trip;
import models.util.ApiError;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.points.BadgeRepository;
import repositories.points.PointRewardRepository;
import repositories.profiles.ProfileRepository;
import util.AuthenticationUtil;

import java.util.*;

public class AchievementTrackerController extends Controller {
    private static final String USER_POINTS = "userPoints";
    private static final String POINTS_REWARDED = "pointsRewarded";
    private static final String BADGES_ACHIEVED = "badgesAchieved";

    private ProfileRepository profileRepository;
    private PointRewardRepository pointRewardRepository;
    private BadgeRepository badgeRepository;
    private ObjectMapper objectMapper;


    @Inject
    public AchievementTrackerController(ProfileRepository profileRepository,
                                        PointRewardRepository pointRewardRepository,
                                        BadgeRepository badgeRepository,
                                        ObjectMapper objectMapper) {
        this.profileRepository = profileRepository;
        this.pointRewardRepository = pointRewardRepository;
        this.badgeRepository = badgeRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * For points
     *
     * @param actingProfile
     * @param action
     * @return
     */
    private Integer givePoints(Profile actingProfile, Action action) {

        AchievementTracker achievementTracker = actingProfile.getAchievementTracker();  // Get the tracker for the user.
        PointReward reward = pointRewardRepository.findUsing(action);    // Get the reward to add.

        if (reward != null) {
            return achievementTracker.addPoints(reward.getValue());
        }

        return null;
    }


    /**
     * Adds badge progress to the user's AchievementTracker when they progress with the badge progress.
     *
     * @param actingProfile         the profile receiving progress.
     * @param action                the action that was carried out.
     * @return                      the progress added to the profile for the specified badge.
     */
    private Badge giveBadge(Profile actingProfile, Action action, int progress) {

        AchievementTracker achievementTracker = actingProfile.getAchievementTracker();  // Get the tracker for the user.

        Badge badge = badgeRepository.findUsing(action);

        if (badge != null) {
            achievementTracker.addBadgeProgress(badge, progress);
            return achievementTracker.getRecentlyAchieved();
        }

        return null;
    }



    private JsonNode constructRewardJson(Collection<Badge> badgesAchieved, Integer pointsAchieved) {

        ObjectNode returnJson = objectMapper.createObjectNode();

        if(pointsAchieved != null) {
            returnJson.put(POINTS_REWARDED, pointsAchieved);
        }

        ArrayNode badges = objectMapper.valueToTree(badgesAchieved);

        returnJson.putArray(BADGES_ACHIEVED).addAll(badges);

        return returnJson;
    }


    /**
     * Rewards the user for creating a trip.
     *
     * @param actingProfile         the profile receiving points.
     * @param tripCreated           the trip that was created.
     * @return                      Json node of the reward result.
     */
    public JsonNode rewardAction(Profile actingProfile, Trip tripCreated) {
        Collection<Badge> badgesAchieved = new ArrayList<>();
        badgesAchieved.add(giveBadge(actingProfile, Action.TRIP_CREATED, 1));
        profileRepository.update(actingProfile);    // Update the tracker stored in the database.
        return constructRewardJson(badgesAchieved, null);
    }


    /**
     * Adds points to the user's AchievementTracker for creating a destination.
     *
     * @param actingProfile         the profile receiving points.
     * @param destinationCreated    the destination that was created.
     * @return                      the points added rewarded to the profile.
     */
    public int rewardAction(Profile actingProfile, Destination destinationCreated) {
        AchievementTracker achievementTracker = actingProfile.getAchievementTracker();  // Get the tracker for the user.

        PointReward reward = pointRewardRepository.findUsing(Action.DESTINATION_CREATED);    // Get the reward to add.
        achievementTracker.addPoints(reward.getValue());
        profileRepository.update(actingProfile);    // Update the tracker stored in the database.

        return reward.getValue();
    }


    /**
     * Adds points to the user's AchievementTracker for creating an objective.
     *
     * @param actingProfile     the profile that performed the action.
     * @param objectiveCreated  the objective that was created.
     * @return                  the number of points that the user was rewarded.
     */
    public int rewardAction(Profile actingProfile, Objective objectiveCreated) {
        AchievementTracker achievementTracker = actingProfile.getAchievementTracker();  // Get the tracker for the user.

        PointReward reward = pointRewardRepository.findUsing(Action.OBJECTIVE_CREATED);    // Get the reward to add.
        achievementTracker.addPoints(reward.getValue());
        profileRepository.update(actingProfile);    // Update the tracker stored in the database.

        return reward.getValue();
    }


    /**
     * Rewards the acting profile with points based on their action on the questWorkedOn. If completed is false, then
     * the user created the quest, otherwise they completed the quest.
     *
     * @param actingProfile     the profile that completed the action.
     * @param questWorkedOn     the quest that was either created or completed
     * @param completed         a boolean indicating if the action was completing the quest.
     * @return                  the points rewarded to the user.
     */
    public int rewardAction(Profile actingProfile, Quest questWorkedOn, boolean completed) {
        AchievementTracker achievementTracker = actingProfile.getAchievementTracker();
        PointReward reward;
        if (completed) {
            reward = pointRewardRepository.findUsing(Action.QUEST_COMPLETED);
        } else {
            reward = pointRewardRepository.findUsing(Action.QUEST_CREATED);
        }
        achievementTracker.addPoints(reward.getValue());
        profileRepository.update(actingProfile);
        return reward.getValue();
    }


    /**
     * Adds the given amount of points to the given profile's AchievementTracker.
     *
     * @param actingProfile     the profile receiving points.
     * @param objectiveSolved   the objective which the action was performed on.
     * @return                  the points added rewarded to the profile.
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
     *
     * @param request       the Http request sent.
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
        pointsJson.put(USER_POINTS, tracker.getPoints());

        return ok(pointsJson);
    }
}
