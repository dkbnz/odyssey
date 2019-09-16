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
import repositories.points.AchievementTrackerRepository;
import repositories.points.BadgeRepository;
import repositories.points.PointRewardRepository;
import repositories.profiles.ProfileRepository;
import util.AuthenticationUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AchievementTrackerController extends Controller {
    private static final String USER_POINTS = "userPoints";
    private static final String POINTS_REWARDED = "pointsRewarded";
    private static final String BADGES_ACHIEVED = "badgesAchieved";
    private static final Integer STARTING_STREAK_NUMBER = 1;
    private static final Integer LOST_STREAK = 1;
    private static final String CLIENT_DATE_FIELD = "clientDate";
    private static final String CURRENT_STREAK = "currentStreak";
    private static final String REWARD = "reward";

    private static final int SINGLE_COUNTRY = 1;
    private static final int INCREMENT_ONE = 1;

    private static final int ADVENTURER_THRESHOLD = 10;


    private ProfileRepository profileRepository;
    private PointRewardRepository pointRewardRepository;
    private BadgeRepository badgeRepository;
    private ObjectMapper objectMapper;


    @Inject
    public AchievementTrackerController(ProfileRepository profileRepository,
                                        PointRewardRepository pointRewardRepository,
                                        AchievementTrackerRepository achievementTrackerRepository,
                                        BadgeRepository badgeRepository,
                                        ObjectMapper objectMapper) {
        this.profileRepository = profileRepository;
        this.pointRewardRepository = pointRewardRepository;
        this.badgeRepository = badgeRepository;
        this.objectMapper = objectMapper;
    }


    /**
     * Adds points to the given profile. The point value is determined by the action completed.
     * Progresses the Overachiever badge.
     *
     * @param actingProfile     the profile receiving points
     * @param action            the action being taken
     * @return                  the number of points added, or null if none are added
     */
    private PointReward givePoints(Profile actingProfile, Action action) {

        if (actingProfile == null) {
            return null;
        }

        AchievementTracker achievementTracker = actingProfile.getAchievementTracker();  // Get the tracker for the user.
        PointReward reward = pointRewardRepository.findUsing(action);    // Get the reward to add.

        if (reward != null) {
            int pointsValue = reward.getValue();
            achievementTracker.addPoints(pointsValue);

            // Progress the Overachiever badge
            progressBadge(actingProfile, Action.POINTS_GAINED, pointsValue);
            return reward;
        }

        return null;
    }


    /**
     * Adds badge progress to the user's AchievementTracker when they complete an action.
     *
     * @param actingProfile         the profile receiving progress.
     * @param action                the action that was carried out.
     * @param progress              the level of progress to be added on.
     * @return                      the progress added to the profile for the specified badge.
     */
    private Badge progressBadge(Profile actingProfile, Action action, int progress) {

        AchievementTracker achievementTracker = actingProfile.getAchievementTracker();  // Get the tracker for the user.

        Badge badge = badgeRepository.findUsing(action);

        if (badge != null) {
            achievementTracker.addBadgeProgress(badge, progress);
            return achievementTracker.getRecentlyAchieved();
        }

        return null;
    }


    /**
     * Checks if a user has received a badge for gaining points.
     * If they have, adds the badge to the collection of badges achieved.
     *
     * @param actingProfile     the profile that performed the action.
     * @param badgesAchieved    the collection of badges achieved.
     */
    private void updatePointsBadge(Profile actingProfile, Collection<Badge> badgesAchieved) {
        Badge badge = actingProfile.getAchievementTracker().getRecentlyAchieved();
        if (badge != null) {
            badgesAchieved.add(badge);
        }
    }


    /**
     * Builds an object node that contains the collection of given badges achieved and the given value of points achieved.
     *
     * @param badgesAchieved        a collection of badges achieved.
     * @param pointsAchieved        the number of points achieved.
     * @return                      an object node containing the badges and points achieved to return to the front end.
     */
    private ObjectNode constructRewardJson(Collection<Badge> badgesAchieved, PointReward pointsAchieved) {

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
     * Rewards the user for logging in on consecutive days.
     *
     * @param actingProfile         the profile receiving points.
     * @return                      Json node of the reward result.
     */
    private ObjectNode rewardAction(Profile actingProfile) {

        int currentStreak = actingProfile.getAchievementTracker().getCurrentStreak();

        Collection<Badge> badgesAchieved = new ArrayList<>();

        Set<Badge> badges = actingProfile.getAchievementTracker().getBadges();

        Badge foundBadge = null;

        for (Badge badge : badges) {
            if (badge.getActionToAchieve().equals(Action.LOGIN_STREAK)) {
                foundBadge = badge;
                break;
            }
        }


        if(foundBadge == null || foundBadge.getProgress() < currentStreak) {
            badgesAchieved.add(progressBadge(actingProfile, Action.LOGIN_STREAK, INCREMENT_ONE));
        }

        profileRepository.update(actingProfile);    // Update the tracker stored in the database.
        return constructRewardJson(badgesAchieved, null);
    }


    /**
     * Rewards the user for creating a trip.
     * Adds progress towards the trip creation badge.
     *
     * @param actingProfile         the profile that performed the action.
     * @param tripCreated           the trip that was created.
     * @return                      Json node of the reward result.
     */
    public JsonNode rewardAction(Profile actingProfile, Trip tripCreated) {
        Collection<Badge> badgesAchieved = new ArrayList<>();

        // Progress towards badge
        Badge badgeToGive = progressBadge(actingProfile, Action.TRIP_CREATED, INCREMENT_ONE);

        if (badgeToGive != null) {
            badgesAchieved.add(badgeToGive);
        }

        profileRepository.update(actingProfile);    // Update the tracker stored in the database.

        return constructRewardJson(badgesAchieved, null);
    }


    /**
     * Rewards the user for creating a destination.
     * Adds points to the user's AchievementTracker and adds progress towards the destination creation badge.
     *
     * @param actingProfile         the profile that performed the action.
     * @param destinationCreated    the destination that was created.
     * @return                      Json node of the reward result.
     */
    public JsonNode rewardAction(Profile actingProfile, Destination destinationCreated) {
        Collection<Badge> badgesAchieved = new ArrayList<>();

        // Award points
        PointReward points = givePoints(actingProfile, Action.DESTINATION_CREATED);
        updatePointsBadge(actingProfile, badgesAchieved);

        // Progress towards badge
        Badge badgeToProgress = progressBadge(actingProfile, Action.DESTINATION_CREATED, 1);
        if (badgeToProgress != null) {
            badgesAchieved.add(badgeToProgress);
        }

        profileRepository.update(actingProfile);    // Update the tracker stored in the database.

        return constructRewardJson(badgesAchieved, points);
    }


    /**
     * Rewards the user for checking into an objective.
     * Adds points to the given profile's AchievementTracker based on the completed action.
     *
     * @param actingProfile         the profile receiving points.
     * @param objectiveCheckedIn    the objective which the action was performed on.
     * @return                      Json node of the reward result.
     */
    public JsonNode rewardAction(Profile actingProfile, Objective objectiveCheckedIn) {
        Collection<Badge> badgesAchieved = new ArrayList<>();

        // Award points
        PointReward points = givePoints(actingProfile, Action.CHECKED_IN);
        updatePointsBadge(actingProfile, badgesAchieved);

        profileRepository.update(actingProfile);    // Update the tracker stored in the database.

        return constructRewardJson(badgesAchieved, points);
    }


    /**
     * Rewards the acting profile with points based on their action on the questWorkedOn. If completed is false, then
     * the user created the quest, otherwise they completed the quest.
     *
     * @param actingProfile     the profile that completed the action.
     * @param questWorkedOn     the quest that was either created or completed
     * @param completedAction   an action indicating the operation performed on the quest.
     * @return                  Json node of the reward result.
     */
    public JsonNode rewardAction(Profile actingProfile, Quest questWorkedOn, Action completedAction) {
        Collection<Badge> badgesAchieved = new ArrayList<>();

        // Award points
        PointReward points = givePoints(actingProfile, completedAction);
        updatePointsBadge(actingProfile, badgesAchieved);

        // Progress towards badge
        badgesAchieved.add(progressBadge(actingProfile, completedAction, INCREMENT_ONE));

        // Adds to the Wayfarer (distance badge) progress. Needs to be in this current order.
        if (completedAction == Action.QUEST_COMPLETED) {

            // Calculate the distance of the completed quest and add the progress to the relevant badge.
            badgesAchieved.add(progressBadge(actingProfile,
                    Action.DISTANCE_QUEST_COMPLETED,
                    calculateTotalQuestDistance(questWorkedOn)));

            // Check if the quest is international.
            if (questWorkedOn.getObjectiveCountries().size() > SINGLE_COUNTRY) {
                badgesAchieved.add(progressBadge(actingProfile,
                        Action.INTERNATIONAL_QUEST_COMPLETED, INCREMENT_ONE));
            }

            // Check if quest contains enough objectives to be an adventurer.
            if (questWorkedOn.getObjectives().size() >= ADVENTURER_THRESHOLD) {
                badgesAchieved.add(progressBadge(actingProfile,
                        Action.LARGE_QUEST_COMPLETED, INCREMENT_ONE));
            }

        }

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


    /**
     * Calculates the total distance between each objective for a given Quest.
     *
     * @param quest           the Quest to calculate the distance of.
     * @return                an int containing the total distance in meters between each objective in the quest.
     */
    private int calculateTotalQuestDistance(Quest quest) {
        double totalDistance = 0;

        List<Objective> questObjectives = quest.getObjectives();
        // Calculate the total distance between each objective in the quest.
        for (int i = 1; i < questObjectives.size(); i++) {
            totalDistance +=
                    calculateDistance(questObjectives.get(i-1).getDestination().getLatitude(),
                            questObjectives.get(i).getDestination().getLatitude(),
                            questObjectives.get(i-1).getDestination().getLongitude(),
                            questObjectives.get(i).getDestination().getLongitude());
        }
        return (int) Math.ceil(totalDistance);
    }


    /**
     * Calculates the distance between two destinations locations represented by latitude and longitude values.
     * This is used to determine the quest's total distance, so the user can obtain the Wayfarer badge.
     *
     * @param latitude1     the first destination's latitude value.
     * @param latitude2     the second destination's latitude value.
     * @param longitude1    the first destination's longitude value.
     * @param longitude2    the second destination's longitude value.
     * @return              a double containing the distance between the two points.
     */
    private double calculateDistance(double latitude1, double latitude2, double longitude1, double longitude2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(latitude2 - latitude1);
        double lonDistance = Math.toRadians(longitude2 - longitude1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }


    /**
     * Function called from the routes to update the currently logged in session's users streak
     *
     * @param request       contains the profile information and the client date information in the json body
     * @return              ok if the request is fine and their is a user and the json format is correct
     *                      badRequest if the request has invalid json
     *                      unauthorized if there is no user in the database for the given session
     */
    public Result updateLastSeen(Http.Request request) {

        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);

        if (loggedInUser != null) {
            // User is logged in and attempts to get their client timezone data
            JsonNode lastSeenJson = request.body().asJson();

            // Check if a body was given and has required fields
            if (lastSeenJson == null ||
                    (!lastSeenJson.has(CLIENT_DATE_FIELD))) {
                // If JSON Object contains no time or time offset key, return bad request
                // Prevents null pointer exceptions when trying to get the values below.
                return badRequest(ApiError.invalidJson());
            } else {
                String clientDate = lastSeenJson.get(CLIENT_DATE_FIELD).asText();

                JsonNode responseJson = checkStreakIncrement(loggedInUser, clientDate);

                if (responseJson != null) {
                    return ok(responseJson);
                } else {
                    return badRequest(ApiError.invalidJson());
                }

            }
        } else {
            return unauthorized(ApiError.unauthorized());
        }

    }


    /**
     * Checks the users client date with their last seen date in the database. If the date is one day more than the last
     * seen date then the user will increment their current streak and send the check to see if they should increment
     * their streaker badge. If the users current date is any day after their last seen date plus one then they will
     * lose their current streak.
     *
     * @param profile               the profile your wanting to check the streaks date for
     * @param clientDateString      the clients date string sent from the front end of the application
     * @return                      jsonNode containing the badge if this process was able to be achieved
     *                              jsonNode null if the process got caught with a parsing exception and so should
     *                              return 400 to the front end application
     */
    private JsonNode checkStreakIncrement(Profile profile, String clientDateString) {

        Date clientDate;

        ObjectNode responseJson = objectMapper.createObjectNode();

        try {
            clientDate = new SimpleDateFormat("yyyy-MM-dd").parse(clientDateString);
        } catch (ParseException e) {
            return null;
        }

        Date lastSeenDate = profile.getLastSeenDate();

        if (lastSeenDate == null) {
            profile.getAchievementTracker().setCurrentStreak(STARTING_STREAK_NUMBER);
            responseJson.set(REWARD, this.rewardAction(profile));
            responseJson.put(CURRENT_STREAK, profile.getAchievementTracker().getCurrentStreak());

        } else {
            Date incrementDate = addOneDay(lastSeenDate);

            if (clientDate.equals(incrementDate)) {
                // User has been seen on the next day to their previous seen date
                // And so their streaks have incremented
                profile.getAchievementTracker().addToCurrentStreak();


                responseJson.set(REWARD, this.rewardAction(profile));
                responseJson.put(CURRENT_STREAK, profile.getAchievementTracker().getCurrentStreak());
            } else if (clientDate.after(incrementDate)) {
                // User has lost their streak
                profile.getAchievementTracker().setCurrentStreak(LOST_STREAK);
            }
        }

        profile.setLastSeenDate(clientDate);

        profileRepository.update(profile);

        return responseJson;
    }


    /**
     * Adds one day to the given date and returns.
     *
     * @param date          the date that you want to add 1 date to.
     * @return              a new date that is 1 day later than the date given.
     */
    private Date addOneDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, INCREMENT_ONE);
        return cal.getTime();
    }
}
