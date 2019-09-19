package controllers.hints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.hints.Hint;
import models.objectives.Objective;
import models.profiles.Profile;
import models.util.ApiError;
import models.util.Errors;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repositories.objectives.HintRepository;
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
    private ObjectMapper objectMapper;

    /**
     * String constants for json creation.
     */
    private static final String NEW_HINT_ID = "newHintId";

    @Inject
    public HintController(ProfileRepository profileRepository,
                          HintRepository hintRepository,
                          ObjectiveRepository objectiveRepository,
                          ObjectMapper objectMapper) {
        this.profileRepository = profileRepository;
        this.hintRepository = hintRepository;
        this.objectiveRepository = objectiveRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Creates a new hint for an objective in a quest.
     *
     * @param request           the Http request containing a Json body of the new hint.
     * @param objectiveId       the Id of the objective the hint will be created for.
     * @param userId            the Id of the user who will own the hint created.
     *
     * @return
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
        List<Objective> solvedObjectives = objectiveRepository.findAllCompletedUsing(hintCreator);
        boolean objectiveSolved = solvedObjectives.contains(objectiveToAddHint);

        if (!(AuthenticationUtil.validUser(hintCreator, objectiveToAddHint.getOwner()) || objectiveSolved)) {
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

        ObjectNode returnJson = objectMapper.createObjectNode();

        hintRepository.save(hint);

        returnJson.set(NEW_HINT_ID, Json.toJson(hint.getId()));

        profileRepository.update(hintCreator);
        objectiveRepository.update(objectiveToAddHint);

        return created(returnJson);
    }


    /**
     * Retrieves all the hints for a given objective.
     *
     * @param request           the Http request containing login information.
     * @param objectiveId       the Id of the objective having its hints retrieved.
     * @return
     */
    public Result fetchAll(Http.Request request, Long objectiveId) {
        return ok();
    }
}
