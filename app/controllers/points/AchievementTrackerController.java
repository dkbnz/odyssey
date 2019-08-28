package controllers.points;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import models.destinations.Destination;
import models.objectives.Objective;
import models.points.AchievementTracker;
import models.profiles.Profile;
import models.quests.Quest;
import models.util.ApiError;
import play.libs.Json;
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


    @Inject
    public AchievementTrackerController(AchievementTrackerRepository achievementTrackerRepository,
                                        ProfileRepository profileRepository,
                                        ObjectMapper objectMapper) {
        this.achievementTrackerRepository = achievementTrackerRepository;
        this.profileRepository = profileRepository;
        this.objectMapper = objectMapper;
    }

    public Result completeAction(Http.Request request) {
        return internalServerError();
    }

    public Result fetchPoints(Http.Request request, Long userId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        Profile requestedUser = profileRepository.findById(userId);

        if (requestedUser == null) {
            return notFound(ApiError.notFound());
        }

        AchievementTracker tracker = achievementTrackerRepository.findUsing(requestedUser);

        if (tracker == null) {
            return notFound(ApiError.notFound());
        }

        ObjectNode pointsJson = objectMapper.createObjectNode();
        pointsJson.put("userPoints", tracker.getPoints());

        return ok(pointsJson);
    }

    public boolean completed(Quest quest) {
        return false;
        // TODO Matthew Implement
    }

    public boolean completed(Objective objective) {
        return false;
        // TODO Matthew Implement
    }

    public boolean created(Destination destination) {
        return false;
        // TODO Matthew Implement
    }

}
