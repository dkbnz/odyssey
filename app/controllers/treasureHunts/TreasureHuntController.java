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

    private static final String DESTINATION = "destination";
    private static final String RIDDLE = "riddle";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";

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

        TreasureHunt treasureHunt = createNewTreasureHunt(json, treasureHuntOwner);

        if (treasureHunt == null) {
            return badRequest();
        }

        treasureHuntRepository.save(treasureHunt);
        profileRepository.update(treasureHuntOwner);

        return created();
    }



    private TreasureHunt createNewTreasureHunt(JsonNode json, Profile owner) {
        TreasureHunt treasureHunt = new TreasureHunt();

        Destination destination = destinationRepository.fetch(json.get(DESTINATION).asLong());

        Date startDate = parseDate(json.get(START_DATE).asText());
        Date endDate = parseDate(json.get(END_DATE).asText());

        if (startDate == null || endDate == null) {
            return null;
        }

        treasureHunt.setDestination(destination);
        treasureHunt.setRiddle(json.get(RIDDLE).asText());
        treasureHunt.setStartDate(startDate);
        treasureHunt.setEndDate(endDate);
        treasureHunt.setOwner(owner);

        return treasureHunt;
    }




    private Date parseDate(String dateString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }






    private boolean isValidJson(JsonNode json) {
        String destinationId =  json.get(DESTINATION).asText();
        String riddle =         json.get(RIDDLE).asText();
        String startDate =      json.get(START_DATE).asText();
        String endDate =        json.get(END_DATE).asText();

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

        return destination != null;
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
