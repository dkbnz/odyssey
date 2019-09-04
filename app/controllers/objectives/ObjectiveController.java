package controllers.objectives;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import controllers.points.AchievementTrackerController;
import models.util.ApiError;
import models.profiles.Profile;
import models.destinations.Destination;
import models.objectives.Objective;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repositories.profiles.ProfileRepository;
import repositories.destinations.DestinationRepository;
import repositories.objectives.ObjectiveRepository;
import util.AuthenticationUtil;
import util.Views;

import java.util.*;

import static play.mvc.Results.*;

public class ObjectiveController {

    private ObjectiveRepository objectiveRepository;
    private DestinationRepository destinationRepository;
    private ProfileRepository profileRepository;
    private AchievementTrackerController achievementTrackerController;
    private ObjectMapper objectMapper;

    private static final Long GLOBAL_ADMIN_ID = 1L;
    private static final String DESTINATION_ERROR = "Provided Destination not found.";
    private static final String TREASURE_HUNT_NOT_FOUND = "Objective not found.";
    private static final String INVALID_JSON_FORMAT = "Invalid Json format.";

    @Inject
    public ObjectiveController(ObjectiveRepository objectiveRepository,
                               DestinationRepository destinationRepository,
                               ProfileRepository profileRepository,
                               AchievementTrackerController achievementTrackerController,
                               ObjectMapper objectMapper) {
        this.objectiveRepository = objectiveRepository;
        this.destinationRepository = destinationRepository;
        this.profileRepository = profileRepository;
        this.achievementTrackerController = achievementTrackerController;
        this.objectMapper = objectMapper;
    }


    /**
     * Creates and saves a new objective for a user, checking if the user is creating one for themselves or if
     * the user is an admin. It also checks the request for validity.
     *
     * @param request   the Http request containing a Json body of the new objective details.
     * @param userId    the id of the user who will own the created objective.
     * @return          created() (Http 201) response if creation is successful.
     *                  notFound() (Http 404) response if
     *                  forbidden() (Http 403) response if the logged in user is not the target owner or an admin.
     *                  badRequest() (Http 400) response if the request contains any errors.
     *                  unauthorized() (Http 401) response if no one is logged in.
     */
    public Result create(Http.Request request, Long userId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized();
        }

        Profile objectiveOwner = profileRepository.findById(userId);

        if (objectiveOwner == null) {
            return badRequest();
        }

        if (!AuthenticationUtil.validUser(loggedInUser, objectiveOwner)) {
            return forbidden();
        }

        // Create list to hold objective errors
        List<ApiError> objectiveErrors = new ArrayList<>();

        Objective objective;

        try {
            // Attempt to turn json body into a objective object.
            objective = objectMapper.readerWithView(Views.Owner.class)
                    .forType(Objective.class)
                    .readValue(request.body().asJson());
        } catch (Exception e) {
            // Errors with deserialization.
            objectiveErrors.add(new ApiError(INVALID_JSON_FORMAT));
            return badRequest(Json.toJson(objectiveErrors));
        }

        objective.setOwner(objectiveOwner);

        Destination objectiveDestination = objective.getDestination();

        if (objectiveDestination == null || objectiveDestination.getId() == null
                || destinationRepository.findById(objectiveDestination.getId()) == null) {
            objectiveErrors.add(new ApiError(DESTINATION_ERROR));
        } else {
            objective.setDestination(destinationRepository.findById(objectiveDestination.getId()));
        }


        Profile globalAdmin = profileRepository.findById(GLOBAL_ADMIN_ID);

        if (globalAdmin == null) {
            return notFound();
        }

        if (objectiveDestination != null) {
            objectiveDestination.changeOwner(globalAdmin);
        }

        // Validate objective and get any errors
        objectiveErrors.addAll(objective.getErrors());

        if (!objectiveErrors.isEmpty()) {
            return badRequest(Json.toJson(objectiveErrors));
        }

        ObjectNode returnJson = objectMapper.createObjectNode();

        objectiveRepository.save(objective);

        int pointsAdded = achievementTrackerController.rewardAction(objectiveOwner, objective);
        returnJson.put("pointsRewarded", pointsAdded);
        returnJson.set("newObjectiveId", Json.toJson(objective.getId()));

        profileRepository.update(objectiveOwner);
        destinationRepository.update(objectiveDestination);
        profileRepository.update(globalAdmin);

        return created(returnJson);
    }
    

    /**
     * Edits the objective specified by the given id. Changed values are stored in the request body. Validates
     * request body.
     *
     * @param request           the request from the front end of the application containing login information.
     * @param objectiveId       the id of the objective the user is trying to edit.
     * @return                  unauthorized() (Http 401) if the user is not logged in.
     *                          notFound() (Http 404) if the objective id does not exist in the database.
     *                          forbidden() (Http 403) if the user is not the owner of the objective or is not an
     *                          admin.
     *                          ok() (Http 200) if the user is successful in deleting the objective.
     *                          badRequest() (Http 400) if there is an error with the request.
     */
    public Result edit(Http.Request request, Long objectiveId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized();
        }

        Objective objective = objectiveRepository.findById(objectiveId);

        if (objective == null) {
            return notFound(ApiError.notFound());
        }

        Profile objectiveOwner = objective.getOwner();

        if (!AuthenticationUtil.validUser(loggedInUser, objectiveOwner)) {
            return forbidden(ApiError.forbidden());
        }

        // Create list to hold objective errors
        List<ApiError> objectiveErrors = new ArrayList<>();

        try {
            // Attempt to turn json body into a objective object.
            objective = objectMapper.readerWithView(Views.Owner.class)
                    .forType(Objective.class)
                    .readValue(request.body().asJson());
        } catch (Exception e) {
            // Errors with deserialization.
            objectiveErrors.add(new ApiError(INVALID_JSON_FORMAT));
            return badRequest(Json.toJson(objectiveErrors));
        }

        objective.setOwner(objectiveOwner);

        Destination objectiveDestination = objective.getDestination();

        if(objectiveDestination != null && objectiveDestination.getId() != null
                && destinationRepository.findById(objectiveDestination.getId()) == null) {
            objectiveErrors.add(new ApiError(DESTINATION_ERROR));
        }

        // Validate objective and get any errors
        objectiveErrors.addAll(objective.getErrors());

        if (!objectiveErrors.isEmpty()) {
            return badRequest(Json.toJson(objectiveErrors));
        }

        objective.setId(objectiveId);


        objectiveRepository.update(objective);
        return ok();
    }


    /**
     * Deletes a given objective from the database. If the user is the owner of the objective
     * or the user logged in is an admin then the delete will be successful.
     * Otherwise the user will be forbidden.
     *
     * @param request           the request from the front end of the application containing login information.
     * @param objectiveId       the id of the objective the user is trying to delete.
     * @return                  unauthorized() (Http 401) if the user is not logged in.
     *                          notFound() (Http 404) if the objective id does not exist in the database.
     *                          forbidden() (Http 403) if the user is not the owner of the objective or is not an
     *                          admin.
     *                          ok() (Http 200) if the user is successful in deleting the objective.
     *                          badRequest() (Http 400) if there is an error with the request.
     */
    public Result delete(Http.Request request, Long objectiveId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized();
        }

        Objective objective = objectiveRepository.findById(objectiveId);

        if (objective == null) {
            return notFound(TREASURE_HUNT_NOT_FOUND);
        }

        Profile objectiveOwner = objective.getOwner();

        if (!AuthenticationUtil.validUser(loggedInUser, objectiveOwner)) {
            return forbidden();
        }

        if (objectiveOwner != null) {
            objectiveOwner.removeObjective(objective);
            objectiveRepository.delete(objective);
            profileRepository.update(objectiveOwner);
            return ok();
        }
        return badRequest();
    }


    /**
     * Retrieves all the objectives stored in the database (if they have the correct dates).
     *
     * @param request   the request from the front end of the application containing login information.
     * @return          ok() (Http 200) containing a Json body of the retrieved objectives.
     *                  badRequest() (Http 400) response containing an ApiError for an invalid Json body.
     *                  unauthorized() (Http 401) response containing an ApiError if the user is not logged in.
     */
    public Result fetchAll(Http.Request request) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        List<Objective> objectivesQuery = objectiveRepository.findAll();

        String result;

        try {
            result = objectMapper
                    .writerWithView(Views.Public.class)
                    .writeValueAsString(objectivesQuery);
        } catch (JsonProcessingException e) {
            return badRequest(ApiError.invalidJson());
        }

        return ok(result);
    }


    /**
     * Retrieves all the objectives stored in the database (if they have the correct dates).
     *
     * @param request   the request from the front end of the application containing login information.
     * @return          ok() (Http 200) containing a Json body of the retrieved objectives.
     *                  notFound() (Http 404) response containing an ApiError for retrieval failure.
     *      *           forbidden() (Http 401) response containing an ApiError for disallowed retrieval.
     *      *           unauthorized() (Http 401) response containing an ApiError if the user is not logged in.
     *
     */
    public Result fetchByOwner(Http.Request request, Long ownerId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        Profile requestedUser = profileRepository.findById(ownerId);

        if (requestedUser == null) {
            return notFound(ApiError.notFound());
        }

        if (!AuthenticationUtil.validUser(loggedInUser, requestedUser)) {
            return forbidden(ApiError.forbidden());
        }

        return ok(Json.toJson(requestedUser.getMyObjectives()));
    }
}
