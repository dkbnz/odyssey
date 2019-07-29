package controllers.treasureHunts;

import com.google.inject.Inject;
import models.Profile;
import models.treasureHunts.TreasureHunt;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repositories.ProfileRepository;
import repositories.treasureHunts.TreasureHuntRepository;
import util.AuthenticationUtil;

import static play.mvc.Results.*;

public class TreasureHuntController {

    private TreasureHuntRepository treasureHuntRepository;
    private ProfileRepository profileRepository;

    @Inject
    public TreasureHuntController(TreasureHuntRepository treasureHuntRepository,
                                              ProfileRepository profileRepository) {
        this.treasureHuntRepository = treasureHuntRepository;
        this.profileRepository = profileRepository;
    }


    public Result fetchAll(Http.Request request) {
        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }

        return ok(Json.toJson(treasureHuntRepository.findAllTreasureHunts()));
    }


    /**
     * Deletes a given treasure hunt from the database. If the user is the owner of the treasure hunt
     * or the user logged in is an admin then the delete will be successful.
     * Otherwise the user will be forbidden.
     *
     * @param request           the request from the front end of the application containing login info
     * @param treasureHuntId    The id of the treasure hunt the user is trying to delete
     *
     * @return                  unauthorized() if the user is not logged in.
     *                          notFound() if the treasure hunt id does not exist in the database.
     *                          forbidden() if the user is not the owner of the treasure hunt or is not an admin.
     *                          ok() if the user is successful in deleting the treasure hunt.
     *                          badRequest() if there is an error with the request
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
}
