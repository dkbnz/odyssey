package controllers.treasureHunts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import models.ApiError;
import models.Profile;
import models.destinations.Destination;
import models.treasureHunts.TreasureHunt;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repositories.ProfileRepository;
import repositories.destinations.DestinationRepository;
import repositories.treasureHunts.TreasureHuntRepository;
import util.AuthenticationUtil;
import util.Views;

import java.io.IOException;
import java.util.*;

import static play.mvc.Results.*;

public class TreasureHuntController {

    private TreasureHuntRepository treasureHuntRepository;
    private DestinationRepository destinationRepository;
    private ProfileRepository profileRepository;

    private static final int GLOBAL_ADMIN_ID = 1;
    private static final String DESTINATION_ERROR = "Provided Destination not found.";

//    @Inject
//    public TreasureHuntController(TreasureHuntRepository treasureHuntRepository,
////                                  DestinationRepository destinationRepository,
//                                  ProfileRepository profileRepository) {
////        this.treasureHuntRepository = treasureHuntRepository;
////        this.destinationRepository = destinationRepository;
//        this.profileRepository = profileRepository;
//    }


    /**
     * Creates and saves a new treasure hunt for a user, checking if the user is creating one for themselves or if
     * the user is an admin. It also checks the request for validity.
     *
     * @param request   the Http request containing a Json body of the new treasure hunt details.
     * @param userId    the id of the user who will own the created destination.
     * @return          created() Http response if creation is successful.
     *                  forbidden() Http response if the logged in user is not the target owner or an admin.
     *                  badRequest() Http response if the request contains any errors.
     *                  unauthorized() Http response if no one is logged in.
     */
    public Result create(Http.Request request, Long userId) {
        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }

        treasureHuntRepository = new TreasureHuntRepository();
        destinationRepository = new DestinationRepository();
        profileRepository = new ProfileRepository();

        Profile loggedInUser = profileRepository.fetchSingleProfile(loggedInUserId);

        Profile treasureHuntOwner = profileRepository.fetchSingleProfile(userId.intValue());

        if (treasureHuntOwner == null) {
            return badRequest();
        }

        if (!AuthenticationUtil.validUser(loggedInUser, treasureHuntOwner)) {
            return forbidden();
        }

        // Create list to hold treasure hunt errors
        List<ApiError> treasureHuntErrors = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();

        TreasureHunt treasureHunt;

        try {
            // Attempt to turn json body into a treasure hunt object.
            treasureHunt = mapper.readerWithView(Views.Owner.class)
                    .forType(TreasureHunt.class)
                    .readValue(request.body().asJson());
        } catch (Exception e) {
            // Errors with deserialization.
            treasureHuntErrors.add(new ApiError("Invalid Json format"));
            return badRequest(Json.toJson(treasureHuntErrors));
        }

        treasureHunt.setOwner(treasureHuntOwner);

        Destination treasureHuntDestination = treasureHunt.getDestination();

        if(treasureHuntDestination != null && treasureHuntDestination.getId() != null
                && destinationRepository.findById(treasureHuntDestination.getId()) == null) {
            treasureHuntErrors.add(new ApiError(DESTINATION_ERROR));
        }

        treasureHunt.setDestination(treasureHuntDestination);

        Profile globalAdmin = profileRepository.fetchSingleProfile(GLOBAL_ADMIN_ID);

        if (globalAdmin == null) {
            return notFound();
        }

        if (treasureHuntDestination != null) {
            treasureHuntDestination.changeOwner(globalAdmin);
        } else {
            treasureHuntErrors.add(new ApiError(DESTINATION_ERROR));
        }

        // Validate treasure hunt and get any errors
        treasureHuntErrors.addAll(treasureHunt.getErrors());

        if (!treasureHuntErrors.isEmpty()) {
            return badRequest(Json.toJson(treasureHuntErrors));
        }

        treasureHuntRepository.save(treasureHunt);
        profileRepository.update(treasureHuntOwner);
        destinationRepository.update(treasureHuntDestination);
        profileRepository.update(globalAdmin);

        return created(Json.toJson(treasureHunt.getId()));
    }


    /**
     * Retrieves the destination solution to the treasure hunt.
     * @param request           the request from the front end of the application containing login information.
     * @param treasureHuntId    the id of the treasure hunt for which the destination is needed.
     * @return                  the destination solution for the treasure hunt.
     */
    public Result fetchDestination(Http.Request request, Long treasureHuntId) {
        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }

        TreasureHunt treasureHunt = treasureHuntRepository.findById(treasureHuntId);

        if (treasureHunt == null) {
            return notFound();
        }

        Destination destinationResult = treasureHunt.getDestination();
        if (destinationResult == null) {
            return notFound();
        }

        return ok(Json.toJson(destinationResult));
    }


    /**
     * Edits the treasure hunt specified by the given id. Changed values are stored in the request body. Validates
     * request body.
     *
     * @param request           the request from the front end of the application containing login information.
     * @param treasureHuntId    the id of the treasure hunt the user is trying to edit.
     * @return                  unauthorized() (Http 401) if the user is not logged in.
     *                          notFound() (Http 404) if the treasure hunt id does not exist in the database.
     *                          forbidden() (Http 403) if the user is not the owner of the treasure hunt or is not an
     *                          admin.
     *                          ok() (Http 200) if the user is successful in deleting the treasure hunt.
     *                          badRequest() (Http 400) if there is an error with the request.
     */
    public Result edit(Http.Request request, Long treasureHuntId) {
        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }

        TreasureHunt treasureHunt = treasureHuntRepository.findById(treasureHuntId);

        if (treasureHunt == null) {
            return notFound();
        }

        Profile treasureHuntOwner = treasureHunt.getOwner();
        Profile loggedInUser = profileRepository.fetchSingleProfile(loggedInUserId);

        if (!AuthenticationUtil.validUser(loggedInUser, treasureHuntOwner)) {
            return forbidden();
        }

        // Create list to hold treasure hunt errors
        List<ApiError> treasureHuntErrors = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();

        try {
            // Attempt to turn json body into a treasure hunt object.
            treasureHunt = mapper.readerWithView(Views.Owner.class)
                    .forType(TreasureHunt.class)
                    .readValue(request.body().asJson());
        } catch (Exception e) {
            // Errors with deserialization.
            treasureHuntErrors.add(new ApiError("Invalid Json format"));
            return badRequest(Json.toJson(treasureHuntErrors));
        }

        treasureHunt.setOwner(treasureHuntOwner);

        Destination treasureHuntDestination = treasureHunt.getDestination();

        if(treasureHuntDestination != null && treasureHuntDestination.getId() != null
                && destinationRepository.findById(treasureHuntDestination.getId()) == null) {
            treasureHuntErrors.add(new ApiError(DESTINATION_ERROR));
        }

        // Validate treasure hunt and get any errors
        treasureHuntErrors.addAll(treasureHunt.getErrors());

        if (!treasureHuntErrors.isEmpty()) {
            return badRequest(Json.toJson(treasureHuntErrors));
        }

        treasureHunt.setId(treasureHuntId);


        treasureHuntRepository.update(treasureHunt);
        return ok();
    }


    /**
     * Deletes a given treasure hunt from the database. If the user is the owner of the treasure hunt
     * or the user logged in is an admin then the delete will be successful.
     * Otherwise the user will be forbidden.
     *
     * @param request           the request from the front end of the application containing login information.
     * @param treasureHuntId    the id of the treasure hunt the user is trying to delete.
     *
     * @return                  unauthorized() (Http 401) if the user is not logged in.
     *                          notFound() (Http 404) if the treasure hunt id does not exist in the database.
     *                          forbidden() (Http 403) if the user is not the owner of the treasure hunt or is not an
     *                          admin.
     *                          ok() (Http 200) if the user is successful in deleting the treasure hunt.
     *                          badRequest() (Http 400) if there is an error with the request.
     */
    public Result delete(Http.Request request, Long treasureHuntId) {
        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);

        if (loggedInUserId == null) {
            return unauthorized();
        }

        TreasureHunt treasureHunt = treasureHuntRepository.findById(treasureHuntId);

        if (treasureHunt == null) {
            return notFound();
        }

        Profile treasureHuntOwner = treasureHunt.getOwner();
        Profile loggedInUser = profileRepository.fetchSingleProfile(loggedInUserId);

        if (!AuthenticationUtil.validUser(loggedInUser, treasureHuntOwner)) {
            return forbidden();
        }

        if (treasureHuntOwner != null) {
            treasureHuntOwner.removeTreasureHunt(treasureHunt);
            treasureHuntRepository.delete(treasureHunt);
            profileRepository.update(treasureHuntOwner);
            return ok();
        }
        return badRequest();
    }


    /**
     * Retrieves all the treasure hunts stored in the database (if they have the correct dates).
     *
     * @param request   the request from the front end of the application containing login information.
     * @return          ok() (Http 200) containing a Json body of the retrieved treasure hunts.
     *                  unauthorized() (Http 401) if the user is not logged in.
     */
    public Result fetchAll(Http.Request request) throws IOException {
        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }

        List<TreasureHunt> treasureHuntsQuery = treasureHuntRepository.findAll();

        Calendar now = Calendar.getInstance();
        List<TreasureHunt> treasureHunts = new ArrayList<>();

        for (TreasureHunt treasureHunt: treasureHuntsQuery) {
            if ((treasureHunt.getStartDate().before(now.getTime())
                    || treasureHunt.getStartDate().compareTo(now.getTime()) == 0)
                    && (treasureHunt.getEndDate().after(now.getTime())
                    || treasureHunt.getEndDate().compareTo(now.getTime()) == 0)) {
                treasureHunts.add(treasureHunt);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper
                .writerWithView(Views.Public.class)
                .writeValueAsString(treasureHunts);

        return ok(result);
    }



    /**
     * Retrieves all the treasure hunts stored in the database (if they have the correct dates).
     *
     * @param request   the request from the front end of the application containing login information.
     * @return          ok() (Http 200) containing a Json body of the retrieved treasure hunts.
     *                  unauthorized() (Http 401) if the user is not logged in.
     *                  forbidden() (Http 403) if hte user is not allowed to access the specified user's treasure hunts.
     */
    public Result fetchByOwner(Http.Request request, Long ownerId) {
        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }

        Profile requestedUser = profileRepository.fetchSingleProfile(ownerId.intValue());
        Profile loggedInUser = profileRepository.fetchSingleProfile(loggedInUserId);

        if (!AuthenticationUtil.validUser(loggedInUser, requestedUser)) {
            return forbidden();
        }

        return ok(Json.toJson(requestedUser.getMyTreasureHunts()));
    }
}
