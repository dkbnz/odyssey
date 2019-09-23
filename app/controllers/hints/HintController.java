package controllers.hints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.points.AchievementTrackerController;
import models.hints.Hint;
import models.objectives.Objective;
import models.profiles.Profile;
import models.util.ApiError;
import models.util.Errors;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repositories.hints.HintRepository;
import repositories.objectives.ObjectiveRepository;
import repositories.profiles.ProfileRepository;
import util.AuthenticationUtil;
import util.Views;

import javax.inject.Inject;

import static play.mvc.Results.*;

public class HintController {

    private ProfileRepository profileRepository;
    private HintRepository hintRepository;
    private ObjectiveRepository objectiveRepository;
    private AchievementTrackerController achievementTrackerController;
    private ObjectMapper objectMapper;

    /**
     * String constants for json creation.
     */
    private static final String NEW_HINT = "newHint";
    private static final String REWARD = "reward";
    private static final String MESSAGE = "message";
    private static final String NO_HINT_MESSAGE = "No hints available.";

    @Inject
    public HintController(ProfileRepository profileRepository,
                          HintRepository hintRepository,
                          ObjectiveRepository objectiveRepository,
                          AchievementTrackerController achievementTrackerController,
                          ObjectMapper objectMapper) {
        this.profileRepository = profileRepository;
        this.hintRepository = hintRepository;
        this.objectiveRepository = objectiveRepository;
        this.achievementTrackerController = achievementTrackerController;
        this.objectMapper = objectMapper;
    }


    /**
     * Creates a new hint for an objective in a quest.
     *
     * @param request           the Http request containing a Json body of the new hint.
     * @param objectiveId       the id of the objective the hint will be created for.
     * @param userId            the id of the user who will own the hint created.
     * @return                  created() (Http 201) response containing the newly created hint.
     *                          badRequest() (Http 400) response if there is an issue creating a hint from the given
     *                          Json body or when converting to Json for the response.
     *                          unauthorized() (Http 401) response if the user is not logged into the system.
     *                          forbidden() (Http 403) response if the user is the owner or has not solved the objective.
     *                          notFound() (Http 404) response if the objective or the profile does not exist.
     */
    public Result create(Http.Request request, Long objectiveId, Long userId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        Objective objectiveToAddHint = objectiveRepository.findById(objectiveId);
        if (objectiveToAddHint == null) {
            return notFound(ApiError.notFound(Errors.OBJECTIVE_NOT_FOUND));
        }

        Profile hintCreator = profileRepository.findById(userId);
        if (hintCreator == null) {
            return notFound(ApiError.notFound(Errors.PROFILE_NOT_FOUND));
        }

        // Can create a hint if an admin, the owner, or have solved the objective
        if (!(objectiveRepository.hasSolved(loggedInUser, objectiveToAddHint)
                || AuthenticationUtil.validUser(loggedInUser, objectiveToAddHint.getOwner()))) {
            return forbidden(ApiError.forbidden());
        }


        Hint hint;

        try {
            // Attempt to turn json body into a hint object.
            hint = objectMapper.readerWithView(Views.Owner.class)
                    .forType(Hint.class)
                    .readValue(request.body().asJson());
        } catch (Exception e) {
            // Errors with deserialization.
            return badRequest(ApiError.badRequest(Errors.INVALID_JSON_FORMAT));
        }

        objectiveToAddHint.addHint(hint);

        ObjectNode returnJson = objectMapper.createObjectNode();

        // Points for creating a hint.
        returnJson.set(REWARD, achievementTrackerController.rewardAction(hintCreator, hint));

        profileRepository.update(hintCreator);
        objectiveRepository.update(objectiveToAddHint);
        returnJson.set(NEW_HINT, Json.toJson(hint));

        return created(returnJson);
    }


    /**
     * Retrieves all the hints for a given objective.
     *
     * @param request           the Http request containing login information.
     * @param objectiveId       the id of the objective having its hints retrieved.
     * @return                  ok() (Http 200) response containing retrieved hints.
     *                          badRequest() (Http 400) response if there is an issue converting to Json.
     *                          unauthorized() (Http 401) response if the user is not logged into the system.
     *                          forbidden() (Http 403) response if the user is not allowed to retrieve.
     *                          notFound() (Http 404) response if the objective doesn't exist.
     */
    public Result fetchAll(Http.Request request, Long objectiveId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        Objective targetObjective = objectiveRepository.findById(objectiveId);
        if (targetObjective == null) {
            return notFound(ApiError.notFound(Errors.OBJECTIVE_NOT_FOUND));
        }

        // Check if the user has completed the objective, is an admin or owner of the objective.
        if (!(objectiveRepository.hasSolved(loggedInUser, targetObjective)
                || AuthenticationUtil.validUser(loggedInUser, targetObjective.getOwner()))) {
            return forbidden(ApiError.forbidden());
        }

        String result;

        try {
            // Convert hints to a Json string
            result = objectMapper
                    .writerWithView(Views.Public.class)
                    .writeValueAsString(targetObjective.getHints());
        } catch (JsonProcessingException e) {
            // Issues with serialisation
            return badRequest(ApiError.invalidJson());
        }

        return ok(result);
    }


    /**
     * Retrieves a new, unseen hint for a given objective for the user.
     *
     * @param request           the Http request containing login information.
     * @param objectiveId       the id of the objective that the hint is for.
     * @param userId            the user that is requesting the hint.
     * @return                  ok() (Http 200) response containing retrieved hint.
     *                          badRequest() (Http 400) response if there is an issue converting to Json.
     *                          unauthorized() (Http 401) response if the user is not logged into the system.
     *                          forbidden() (Http 403) response if the user is the owner or has not solved the objective.
     *                          notFound() (Http 404) response if the objective or profile doesn't exist, or no hint is
     *                          found for the objective.
     */
    public Result fetchNew(Http.Request request, Long objectiveId, Long userId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        Objective targetObjective = objectiveRepository.findById(objectiveId);
        if (targetObjective == null) {
            return notFound(ApiError.notFound(Errors.OBJECTIVE_NOT_FOUND));
        }

        Profile hintUser = profileRepository.findById(userId);
        if (hintUser == null) {
            return notFound(ApiError.notFound(Errors.PROFILE_NOT_FOUND));
        }

        // Can request a hint if an admin, or have not solved the objective
        if (hintUser.equals(targetObjective.getOwner())) {
            return forbidden(ApiError.forbidden(Errors.HINT_OBJECTIVE_OWNER));
        }
        if (objectiveRepository.hasSolved(hintUser, targetObjective)) {
            return forbidden(ApiError.forbidden(Errors.HINT_OBJECTIVE_SOLVED));
        }

        Hint newHint = hintRepository.findAHint(targetObjective, hintUser);

        if (newHint == null) {
            ObjectNode returnJson = objectMapper.createObjectNode();
            returnJson.put(MESSAGE, NO_HINT_MESSAGE);
            return ok(returnJson);
        }

        return ok(Json.toJson(newHint));
    }


    /**
     * Retrieves all hints for a given objective that the user has seen.
     *
     * @param request           the Http request containing login information.
     * @param objectiveId       the id of the objective that the hints are for.
     * @param userId            the user that is requesting the hints.
     * @return                  ok() (Http 200) response containing retrieved hints.
     *                          badRequest() (Http 400) response if there is an issue converting to Json.
     *                          unauthorized() (Http 401) response if the user is not logged into the system.
     *                          notFound() (Http 404) response if the objective or profile doesn't exist.
     */
    public Result fetchSeen(Http.Request request, Long objectiveId, Long userId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        Objective targetObjective = objectiveRepository.findById(objectiveId);
        if (targetObjective == null) {
            return notFound(ApiError.notFound(Errors.OBJECTIVE_NOT_FOUND));
        }

        Profile hintUser = profileRepository.findById(userId);
        if (hintUser == null) {
            return notFound(ApiError.notFound(Errors.PROFILE_NOT_FOUND));
        }

        return ok(Json.toJson(hintRepository.findSeenHints(targetObjective, hintUser)));
    }
}
