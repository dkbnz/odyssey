package controllers.objectives;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import models.ApiError;
import models.Profile;
import models.destinations.Destination;
import models.objectives.Objective;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repositories.ProfileRepository;
import repositories.destinations.DestinationRepository;
import repositories.objectives.ObjectiveRepository;
import util.AuthenticationUtil;
import util.Views;

import java.io.IOException;
import java.util.*;

import static play.mvc.Results.*;

public class ObjectiveController {

    private ObjectiveRepository objectiveRepository;
    private DestinationRepository destinationRepository;
    private ProfileRepository profileRepository;

    private static final Long GLOBAL_ADMIN_ID = 1L;
    private static final String DESTINATION_ERROR = "Provided Destination not found.";
    private static final String TREASURE_HUNT_NOT_FOUND = "Objective not found.";

    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";


    /** Radius of earth in Km's */
    private static final Double RADIUS_OF_EARTH = 6378.1370;

    @Inject
    public ObjectiveController(ObjectiveRepository objectiveRepository,
                                  DestinationRepository destinationRepository,
                                  ProfileRepository profileRepository) {
        this.objectiveRepository = objectiveRepository;
        this.destinationRepository = destinationRepository;
        this.profileRepository = profileRepository;
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

        ObjectMapper mapper = new ObjectMapper();

        Objective objective;

        try {
            // Attempt to turn json body into a objective object.
            objective = mapper.readerWithView(Views.Owner.class)
                    .forType(Objective.class)
                    .readValue(request.body().asJson());
        } catch (Exception e) {
            // Errors with deserialization.
            objectiveErrors.add(new ApiError("Invalid Json format"));
            return badRequest(Json.toJson(objectiveErrors));
        }

        objective.setOwner(objectiveOwner);

        Destination objectiveDestination = objective.getDestination();

        if(objectiveDestination != null && objectiveDestination.getId() != null
                && destinationRepository.findById(objectiveDestination.getId()) == null) {
            objectiveErrors.add(new ApiError(DESTINATION_ERROR));
        }

        objective.setDestination(objectiveDestination);

        Profile globalAdmin = profileRepository.findById(GLOBAL_ADMIN_ID);

        if (globalAdmin == null) {
            return notFound();
        }

        if (objectiveDestination != null) {
            objectiveDestination.changeOwner(globalAdmin);
        } else {
            objectiveErrors.add(new ApiError(DESTINATION_ERROR));
        }

        // Validate objective and get any errors
        objectiveErrors.addAll(objective.getErrors());

        if (!objectiveErrors.isEmpty()) {
            return badRequest(Json.toJson(objectiveErrors));
        }

        objectiveRepository.save(objective);
        profileRepository.update(objectiveOwner);
        destinationRepository.update(objectiveDestination);
        profileRepository.update(globalAdmin);

        return created(Json.toJson(objective.getId()));
    }


    /**
     * Retrieves the destination solution to the objective.
     * @param request           the request from the front end of the application containing login information.
     * @param objectiveId       the id of the objective for which the destination is needed.
     * @return                  the destination solution for the objective.
     */
    public Result fetchDestination(Http.Request request, Long objectiveId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized();
        }

        Objective objective = objectiveRepository.findById(objectiveId);

        if (objective == null) {
            return notFound(TREASURE_HUNT_NOT_FOUND);
        }

        Destination destinationResult = objective.getDestination();
        if (destinationResult == null) {
            return notFound();
        }

        return ok(Json.toJson(destinationResult));
    }


//    /**
//     * Compares the guessed destination against the actual objective destination.
//     *
//     * @param request           the request from the front end of the application containing login information
//     *                          and the destination guess.
//     * @param objectiveId    the id of the objective being guessed.
//     * @return                  a boolean value of whether the guess is correct.
//     */
//    public Result checkGuess(Http.Request request, Long objectiveId) {
//        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
//        if (loggedInUserId == null) {
//            return unauthorized();
//        }
//
//        Objective objective = objectiveRepository.findById(objectiveId);
//
//        if (objective == null) {
//            return notFound();
//        }
//
//        Profile loggedInUser = profileRepository.fetchSingleProfile(loggedInUserId);
//        Profile objectiveOwner = objective.getOwner();
//
//        if(loggedInUser.equals(objectiveOwner)){
//            return forbidden();
//        }
//
//        if(objective.getSolvedProfiles().contains(loggedInUser)){
//            return forbidden();
//        }
//
//        JsonNode json = request.body().asJson();
//        Long destinationId;
//
//        if (json.has("destination_id")) {
//            destinationId = json.get("destination_id").asLong();
//        } else {
//            return badRequest();
//        }
//
//        Destination destinationGuess = destinationRepository.findById(destinationId);
//
//
//        if (destinationGuess == null) {
//            return notFound();
//        }
//
//        Boolean result = (destinationGuess.equals(objective.getDestination()));
//
//        if(result){
//            objective.addSolvedProfile(loggedInUser);
//            objectiveRepository.update(objective);
//        }
//
//        return ok(Json.toJson(result));
//    }
//
//
//    /**
//     * Checks whether the given user has solved the given objective.
//     *
//     * @param request           the request from the front end of the application containing login information.
//     * @param objectiveId    the objective id to check that the user has solved.
//     * @return                  ok with true or false if the user has solved the objective.
//     */
//    public Result hasSolved(Http.Request request, Long objectiveId, Long userId) {
//        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
//        if (loggedInUserId == null) {
//            return unauthorized();
//        }
//
//        Objective objective = objectiveRepository.findById(objectiveId);
//
//
//        if (objective == null) {
//            return notFound();
//        }
//
//        Profile profileToCheck  = profileRepository.fetchSingleProfile(userId.intValue());
//
//
//        Boolean result = checkSolved(objective, profileToCheck);
//
//        return ok(Json.toJson(result));
//    }
//
//
//    /**
//     * Helper function to return if a given profile is inside the objectives list of solved profiles.
//     *
//     * @param objective      the objective to get the list of solved profile from.
//     * @param profile           the profile to check is inside the list of solved profiles.
//     * @return                  true if the profile is in the list of solved
//     *                          false if the profile is not in the list of solved.
//     */
//    private Boolean checkSolved(Objective objective, Profile profile) {
//        return objective.getSolvedProfiles().contains(profile);
//    }
//
//
//    /**
//     * Calculates the distance between two points from the objective destination and latitude and longitude values.
//     *
//     * @param objective      the objective the user is trying to check in for.
//     * @param latitude          the users current latitude.
//     * @param longitude         the users current longitude.
//     * @return                  true if within the given radius of the destination for the objective.
//     *                          false if not within the given radius of the destination for the objective.
//     */
//    private Boolean inLocationOfObjective(Objective objective,Long latitude,Long longitude) {
////        Double objectiveLatituide = objective.getDestination().getLatitude();
////        Double objectiveLongitude = objective.getDestination().getLongitude();
////        Double latitude1 = latitude.doubleValue();
////        Double changeInLatitude = (latitude - objectiveLatituide * Math.PI/180);
////        Double changeInLongitude = (longitude - objectiveLongitude * Math.PI/180);
////
////        Double halfChordLength = (Math.sin(changeInLatitude/2))^2 + Math.cos(objectiveLatituide) * Math.cos(latitude1) * (Math.sin(changeInLongitude/2))^2
//        return true;
//    }
//
//
//    /**
//     * Checking in a user for a solved objective and will verify if the given latitude and longitude in the request
//     * are within the given objective's destinations radius
//     *
//     * @param request               holds the users login information and the latitude and longitude values.
//     * @param objectiveId        the objective id that the user is trying to check in for.
//     * @return                      unauthorized() if the user is not logged in.
//     *                              notFound() if the objective or profile is not found.
//     *                              forbidden() if the user hasn't soled the objective yet.
//     *                              ok(true/false) true or false based on if successful check in.
//     */
//    public Result checkIn(Http.Request request, Long objectiveId) {
//        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
//        if (loggedInUserId == null) {
//            return unauthorized();
//        }
//
//        Objective objective = objectiveRepository.findById(objectiveId);
//
//        if (objective == null) {
//            return notFound();
//        }
//
//        Profile profile  = profileRepository.fetchSingleProfile(loggedInUserId);
//
//        if (profile == null) {
//            return notFound();
//        }
//
//        if (!checkSolved(objective, profile)) {
//            return forbidden();
//        }
//
//        JsonNode json = request.body().asJson();
//        Long latitude = json.get(LATITUDE).asLong();
//        Long longitude = json.get(LONGITUDE).asLong();
//
//        return ok(Json.toJson(inLocationOfObjective(objective, latitude, longitude)));
//
//    }


    /**
     * Edits the objective specified by the given id. Changed values are stored in the request body. Validates
     * request body.
     *
     * @param request           the request from the front end of the application containing login information.
     * @param objectiveId    the id of the objective the user is trying to edit.
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
            return notFound(TREASURE_HUNT_NOT_FOUND);
        }

        Profile objectiveOwner = objective.getOwner();

        if (!AuthenticationUtil.validUser(loggedInUser, objectiveOwner)) {
            return forbidden();
        }

        // Create list to hold objective errors
        List<ApiError> objectiveErrors = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();

        try {
            // Attempt to turn json body into a objective object.
            objective = mapper.readerWithView(Views.Owner.class)
                    .forType(Objective.class)
                    .readValue(request.body().asJson());
        } catch (Exception e) {
            // Errors with deserialization.
            objectiveErrors.add(new ApiError("Invalid Json format"));
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
     * @param objectiveId    the id of the objective the user is trying to delete.
     *
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

        ObjectMapper mapper = new ObjectMapper();
        String result;

        try {
            result = mapper
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
