package controllers.points;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import models.destinations.Destination;
import models.objectives.Objective;
import models.points.AchievementTracker;
import models.points.gaincommands.CompleteObjectiveCommand;
import models.profiles.Profile;
import models.quests.Quest;
import models.util.ApiError;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.points.AchievementTrackerRepository;
import repositories.profiles.ProfileRepository;
import util.AuthenticationUtil;

public class AchievementTrackerController extends Controller {

    private static final String AUTHORISED = "authorized";
    private static final int DEFAULT_ADMIN_ID = 1;

    private AchievementTrackerRepository achievementTrackerRepository;
    private ProfileRepository profileRepository;
    private ObjectMapper objectMapper;

    private CompleteObjectiveCommand completionCommand;


    @Inject
    public AchievementTrackerController(AchievementTrackerRepository achievementTrackerRepository,
                                        ProfileRepository profileRepository,
                                        ObjectMapper objectMapper) {
        this.achievementTrackerRepository = achievementTrackerRepository;
        this.profileRepository = profileRepository;
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
     * @param pointsToAdd the amount of points being added.
     */
    public void completeAction(Profile actingProfile, int pointsToAdd) {
        AchievementTracker achievementTracker = retrieveTracker(actingProfile);
        achievementTracker.addPoints(pointsToAdd);
        achievementTrackerRepository.update(achievementTracker);


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

    /**
     * Invoker for the completion commands. Takes the profile that completed some activity and returns the points gained.
     * @return the number of points gained. Null if something goes wrong.
     */
    public Integer executeCompletionCommand() {
        if (completionCommand == null) {
            return null;
        }
        return completionCommand.execute();
    }

    public void setCompletionCommand(CompleteObjectiveCommand completionCommand) {
        this.completionCommand = completionCommand;
    }
}
