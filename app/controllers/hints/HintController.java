package controllers.hints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.points.AchievementTrackerController;
import models.hints.Hint;
import models.hints.Vote;
import models.objectives.Objective;
import models.profiles.Profile;
import models.util.ApiError;
import models.util.Errors;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repositories.hints.HintRepository;
import repositories.hints.VoteRepository;
import repositories.objectives.ObjectiveRepository;
import repositories.profiles.ProfileRepository;
import util.AuthenticationUtil;
import util.Views;

import javax.inject.Inject;
import java.util.List;

import static play.mvc.Results.*;

public class HintController {

    private ProfileRepository profileRepository;
    private HintRepository hintRepository;
    private ObjectiveRepository objectiveRepository;
    private VoteRepository voteRepository;
    private AchievementTrackerController achievementTrackerController;
    private ObjectMapper objectMapper;

    /**
     * String constants for json reading and creation.
     */
    private static final String NEW_HINT = "newHint";
    private static final String REWARD = "reward";
    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_SIZE = "pageSize";
    private static final String VOTE = "vote";

    /**
     * The maximum number of hints allowed per page.
     */
    private static final int MAX_PAGE_SIZE = 100;

    @Inject
    public HintController(ProfileRepository profileRepository,
                          HintRepository hintRepository,
                          ObjectiveRepository objectiveRepository,
                          VoteRepository voteRepository,
                          AchievementTrackerController achievementTrackerController,
                          ObjectMapper objectMapper) {
        this.profileRepository = profileRepository;
        this.hintRepository = hintRepository;
        this.objectiveRepository = objectiveRepository;
        this.voteRepository = voteRepository;
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

        hint.setCreator(hintCreator);

        objectiveToAddHint.addHint(hint);

        ObjectNode returnJson = objectMapper.createObjectNode();

        // Points for creating a hint.
        returnJson.set(REWARD, achievementTrackerController.rewardHintCreate(hintCreator));

        profileRepository.update(hintCreator);
        objectiveRepository.update(objectiveToAddHint);
        returnJson.set(NEW_HINT, Json.toJson(hint));

        return created(returnJson);
    }


    /**
     * Retrieves all the hints for a given objective.
     * Includes pagination if provided in the query string.
     *
     * @param request           the Http request containing login information.
     * @param objectiveId       the id of the objective having its hints retrieved.
     * @param userId            the id of the user requesting the list of hints.
     * @return                  ok() (Http 200) response containing retrieved hints.
     *                          badRequest() (Http 400) response if there is an issue converting to Json, or if the
     *                          provided page size or number cannot be converted to a valid integer.
     *                          unauthorized() (Http 401) response if the user is not logged into the system.
     *                          forbidden() (Http 403) response if the user is not allowed to retrieve.
     *                          notFound() (Http 404) response if the objective doesn't exist.
     */
    public Result fetchAll(Http.Request request, Long objectiveId, Long userId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        Profile requestedUser = profileRepository.findById(userId);
        if (requestedUser == null) {
            return notFound(ApiError.notFound(Errors.PROFILE_NOT_FOUND));
        }

        Objective targetObjective = objectiveRepository.findById(objectiveId);
        if (targetObjective == null) {
            return notFound(ApiError.notFound(Errors.OBJECTIVE_NOT_FOUND));
        }

        // Can fetch hints if admin, or requestedUser has solved the objective
        if (!AuthenticationUtil.validUser(loggedInUser, requestedUser)) {
            return forbidden(ApiError.forbidden());
        }

        // Check if the user has completed the objective, is an admin, or owner of the objective.
        if (!(objectiveRepository.hasSolved(loggedInUser, targetObjective)
                || AuthenticationUtil.validUser(loggedInUser, targetObjective.getOwner()))) {
            return forbidden(ApiError.forbidden());
        }

        int pageNumber = 0;
        String pageNumberRequested = request.getQueryString(PAGE_NUMBER);
        if (pageNumberRequested != null && !pageNumberRequested.isEmpty()) {
            try {
                pageNumber = Integer.parseInt(pageNumberRequested);
            } catch (NumberFormatException e) {
                return badRequest(ApiError.badRequest(Errors.INVALID_PAGE_NUMBER_REQUESTED));
            }
        }

        int pageSize = MAX_PAGE_SIZE;
        String pageSizeRequested = request.getQueryString(PAGE_SIZE);

        if (pageSizeRequested != null && !pageSizeRequested.isEmpty()) {
            try {
                pageSize = Integer.parseInt(pageSizeRequested);
                // Restrict the page size to be no larger than the maximum page size.
                pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
            } catch (NumberFormatException e) {
                return badRequest(ApiError.badRequest(Errors.INVALID_PAGE_SIZE_REQUESTED));
            }
        }

        List<Hint> hints = hintRepository.findAllUsing(targetObjective, pageSize, pageNumber);
        ArrayNode voteHints = objectMapper.createArrayNode();
        for (Hint hint : hints) {
            Vote vote = voteRepository.findUsing(requestedUser, hint);
            ObjectNode hintObject = objectMapper.valueToTree(hint);
            hintObject.set(VOTE, Json.toJson(vote));
            voteHints.add(hintObject);
        }

        return ok(voteHints);
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
     *                          forbidden() (Http 403) response if the user is the owner or has not solved the
     *                          objective.
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
            ArrayNode returnJson = objectMapper.createArrayNode();
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
