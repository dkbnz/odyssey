package controllers.treasureHunts;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static play.mvc.Results.*;

public class TreasureHuntController {

    private static final String RIDDLE = "riddle";

    private TreasureHuntRepository treasureHuntRepository;
    private DestinationRepository destinationRepository;
    private ProfileRepository profileRepository;

    @Inject
    public TreasureHuntController(TreasureHuntRepository treasureHuntRepository,
                                              DestinationRepository destinationRepository,
                                              ProfileRepository profileRepository) {
        this.treasureHuntRepository = treasureHuntRepository;
        this.destinationRepository = destinationRepository;
        this.profileRepository = profileRepository;
    }


    public Result fetchAll(Http.Request request) {
        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }

        return ok(Json.toJson(treasureHuntRepository.findAllTreasureHunts()));
    }



    public Result create(Http.Request request, Long userId) {
        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }

        JsonNode json = request.body().asJson();

        Profile loggedInUser = profileRepository.fetchSingleProfile(loggedInUserId);

        Profile treasureHuntOwner = profileRepository.fetchSingleProfile(userId.intValue());

        if (treasureHuntOwner == null) {
            return badRequest();
        }

        if (!AuthenticationUtil.validUser(loggedInUser, treasureHuntOwner)) {
            return forbidden();
        }

        if (!isValidJson(json)) {
            return badRequest();
        }




        TreasureHunt treasureHunt = new TreasureHunt();

        Destination destination = destinationRepository.fetch(json.get("destination").asLong());

        Date startDate;
        Date endDate;

        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(json.get("start_date").asText());
        } catch (ParseException e) {
            return badRequest();
        }
        try {
            endDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(json.get("end_date").asText());
        } catch (ParseException e) {
            return badRequest();
        }


        treasureHunt.setDestination(destination);
        treasureHunt.setRiddle(json.get(RIDDLE).asText());
        treasureHunt.setStartDate(startDate);
        treasureHunt.setEndDate(endDate);
        treasureHunt.setOwner(treasureHuntOwner);

        treasureHuntRepository.save(treasureHunt);
        profileRepository.update(treasureHuntOwner);

        return created();
    }







    private boolean isValidJson(JsonNode json) {
        String destinationId =  json.get("destination").asText();
        String riddle =         json.get(RIDDLE).asText();
        String startDate =      json.get("start_date").asText();
        String endDate =        json.get("end_date").asText();

        if (destinationId.length()    == 0
                || riddle.length()    == 0
                || startDate.length() == 0
                || endDate.length()   == 0) {
            return false;
        }

        Long parsedDestinationId;

        try {
            parsedDestinationId = Long.parseLong(destinationId);
        } catch (NumberFormatException e) {
            return false;
        }

        Destination destination = destinationRepository.fetch(parsedDestinationId);

        if (destination == null) {
            return false;
        }

        return true;
    }

}
