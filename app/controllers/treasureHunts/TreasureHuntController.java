package controllers.treasureHunts;

import com.google.inject.Inject;
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

        return ok(Json.toJson(treasureHuntRepository.findAll()));
    }
}
