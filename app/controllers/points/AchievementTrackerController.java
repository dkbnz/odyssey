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
import play.libs.Json;
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
     * Adds points to the given profile, depending on the action.
     * Progresses the Overachiever badge
     *
     * @param actingProfile     the profile receiving points
     * @param action            the action being taken
     * @return                  the number of points added, or null if none are added
     */
    private Integer givePoints(Profile actingProfile, Action action) {

        if (actingProfile == null) {
            return null;
        }

        AchievementTracker achievementTracker = actingProfile.getAchievementTracker();  // Get the tracker for the user.
        PointReward reward = pointRewardRepository.findUsing(action);    // Get the reward to add.

        if (reward != null) {
            int value = reward.getValue();

            // Progress the Overachiever badge
            giveBadge(actingProfile, Action.POINTS_GAINED, value);
            return achievementTracker.addPoints(value);
        }

        return null;
    }


    /**
     * Adds badge progress to the user's AchievementTracker when they progress with the badge progress.
     *
     * @param actingProfile         the profile receiving progress.
     * @param action                the action that was carried out.
     * @param progress              the level of progress to be added on.
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



    private JsonNode constructRewardJson(Collection<Badge> badgesAchieved, PointReward pointsAchieved) {

        Collection<PointReward> pointsAchievedCollection = new ArrayList<>();
        pointsAchievedCollection.add(pointsAchieved);

        ObjectNode returnJson = objectMapper.createObjectNode();

        ArrayNode badges = objectMapper.valueToTree(badgesAchieved);
        ArrayNode pointsRewarded = objectMapper.valueToTree(pointsAchievedCollection);

        returnJson.putArray(POINTS_REWARDED).addAll(pointsRewarded);
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

        Badge badgeToGive = giveBadge(actingProfile, Action.TRIP_CREATED, 1);

        if (badgeToGive != null) {
            badgesAchieved.add(badgeToGive);
        }
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
        Collection<Badge> badgesAchieved = new ArrayList<>();

        int reward = givePoints(actingProfile, Action.DESTINATION_CREATED);

        // Check if user has received badge for gaining points
        Badge badge = actingProfile.getAchievementTracker().getRecentlyAchieved();
        if (badge != null) {
            badgesAchieved.add(badge);
        }

        profileRepository.update(actingProfile);    // Update the tracker stored in the database.

        return reward;
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
     * @param questAction       a string indicating if what action was taken for the quest.
     * @param questDistance     a double value storing the total distance between objectives in a quest.
     * @return                  the points rewarded to the user.
     */
    public JsonNode rewardAction(Profile actingProfile, Quest questWorkedOn, String questAction, Double questDistance) {
        PointReward points;
        Collection<Badge> badgesAchieved = new ArrayList<>();
        Action completedAction;
        switch (questAction) {
            case "Riddle Solved":
                completedAction = Action.RIDDLE_SOLVED;
                break;
            case "Quest Completed":
                completedAction = Action.QUEST_COMPLETED;
                break;
            case "Quest Created":
                completedAction = Action.QUEST_CREATED;
                break;
            default:
                completedAction = null;
                break;
        }

        givePoints(actingProfile, completedAction);

        // Check if user has received badge for gaining points
        Badge badge = actingProfile.getAchievementTracker().getRecentlyAchieved();
        if (badge != null) {
            badgesAchieved.add(badge);
        }

        badgesAchieved.add(giveBadge(actingProfile, completedAction, 1));

        // Adds to the Wayfarer (distance badge) progress. Needs to be in this current order.
        if (completedAction == Action.QUEST_COMPLETED && questDistance != null) {
            int roundedValue = (int) Math.ceil(questDistance);
            badgesAchieved.add(giveBadge(actingProfile, Action.DISTANCE_QUEST_COMPLETED, roundedValue));
        }

        points = pointRewardRepository.findUsing(completedAction);
        profileRepository.update(actingProfile);

        return constructRewardJson(badgesAchieved, points);
    }


    /**
     * Adds the given amount of points to the given profile's AchievementTracker.
     *
     * @param actingProfile     the profile receiving points.
     * @param objectiveSolved   the objective which the action was performed on.
     * @return                  the points added rewarded to the profile.
     */
    public JsonNode rewardAction(Profile actingProfile, Objective objectiveSolved, boolean checkedIn) {
        PointReward points;
        Collection<Badge> badgesAchieved = new ArrayList<>();
        Action completedAction = checkedIn ? Action.CHECKED_IN : Action.RIDDLE_SOLVED;

        givePoints(actingProfile, completedAction);

        // Check if user has received badge for gaining points
        Badge badge = actingProfile.getAchievementTracker().getRecentlyAchieved();
        if (badge != null) {
            badgesAchieved.add(badge);
        }

        points = pointRewardRepository.findUsing(completedAction);

        profileRepository.update(actingProfile);    // Update the tracker stored in the database.

        return constructRewardJson(badgesAchieved, points);
    }


    /**
     * Retrieves the requested user's current points value.
     *
     * @param request       the Http request sent.
     * @param userId        the user whose points have been requested.
     * @return              ok() (Http 200) containing the user's points if successfully attained.
     *                      unauthorized() (Http 401) if the one sending the request is not logged in.
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


    /**
     * Requests all badges that are currently stored in the database. This is the badges themselves, not a user's
     * progress towards a badge.
     *
     * @param request   the Http request containing the relevant authentication parameters.
     * @return          ok() (Http 200) containing a Json list of all the badges.
     *                  unauthorized() (Http 401) if the user is not logged in.
     */
    public Result fetchAllBadges(Http.Request request) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        return ok(Json.toJson(badgeRepository.findAll()));
    }


    /**
     * Adds all awards specified by the string awardsToGet to the ArrayNode of awards. This is used whenever a user
     * gets points or badges.
     *
     * @param awardJson     the ArrayNode that will contain all the added awards.
     * @param awardsToAdd   the JsonNode containing all the rewards to be added.
     * @param awardsToGet   the String value that determines which awards (points or badges) to be added to the returned
     *                      ArrayNode.
     * @return              an ArrayNode containing all the rewards to add.
     */
    public ArrayNode addAllAwards(ArrayNode awardJson, JsonNode awardsToAdd, String awardsToGet) {
        for (JsonNode award : awardsToAdd.get(awardsToGet)) {
            awardJson.add(award);
        }
        return awardJson;
    }
}
