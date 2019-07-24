package controllers.destinations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.Inject;
import models.Profile;
import models.TravellerType;
import models.destinations.Destination;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repositories.ProfileRepository;
import repositories.destinations.DestinationRepository;
import repositories.destinations.TravellerTypeRepository;
import util.AuthenticationUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static play.mvc.Results.*;

public class DestinationTravellerTypeController {

    private DestinationRepository destinationRepo;
    private TravellerTypeRepository travellerTypeRepo;
    private ProfileRepository profileRepo;

    private static final String AUTHORIZED = "authorized";
    private static final String NOT_SIGNED_IN = "You are not logged in.";

    @Inject
    public DestinationTravellerTypeController(DestinationRepository destinationRepo,
                                              TravellerTypeRepository travellerTypeRepo,
                                              ProfileRepository profileRepo) {
        this.destinationRepo = destinationRepo;
        this.travellerTypeRepo = travellerTypeRepo;
        this.profileRepo = profileRepo;
    }


    /**
     * Takes a JsonNode that is an array of traveller types. Will then iterate
     * over the array and produce a list of traveller types.
     *
     * @param travellerTypesNode    a JsonNode containing a list of serialized traveller types
     * @return                      a list of traveller types
     */
    private List<TravellerType> getTravellerTypeFromNode(JsonNode travellerTypesNode) {
        List<TravellerType> travellerTypes = new ArrayList<>();

        if (travellerTypesNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) travellerTypesNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                travellerTypes.add(Json.fromJson(arrayNode.get(i), TravellerType.class));
            }
        }

        return travellerTypes;
    }


    /**
     * Method to set the traveller types for a destination.
     * Request body is expected to be a jsonArray of traveller types.
     * If method executes successfully, given traveller types will be set as
     * recommended traveller types for a destination.
     * All proposed traveller types will be cleared.
     *
     * @param request           Http request containing a Json body of traveller types
     * @param destinationId     id of the destination for which the traveller types are set
     * @return                  notFound() (Http 404) if destination could not found, ok() (Http 200) if successfully updated.
     */
    public Result create(Http.Request request, Long destinationId) {

        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }

        Destination destinationToMutate = destinationRepo.fetch(destinationId);

        if (destinationToMutate == null) {
            return notFound();
        }

        Profile loggedInUser = profileRepo.fetchSingleProfile(loggedInUserId);

        if (!AuthenticationUtil.validUser(loggedInUser, destinationToMutate.getOwner())) {
            return forbidden();
        }

        JsonNode jsonBody = request.body().asJson();

        Set<TravellerType> travellerTypesToSet = new HashSet<>(
                getTravellerTypeFromNode(jsonBody)
        );

        // Prevent the user from adding traveller types that do not already exist
        travellerTypesToSet.retainAll(
                travellerTypeRepo.findAll()
        );

        destinationToMutate.setTravellerTypes(
                travellerTypesToSet
        );

        destinationRepo.save(destinationToMutate);
        return ok();
    }


    /**
     * Method to propose traveller types for a destination.
     * Request body is expected to be a jsonArray of traveller types.
     * If method executes successfully, given traveller types will be split between
     * proposed to add or proposed to remove depending on their presence in the current set.
     *
     * @param request           Http request containing a Json body of traveller types
     * @param destinationId     id of the destination for which the traveller types are proposed
     * @return                  notFound() (Http 404) if destination could not found, ok() (Http 200) if successfully updated.
     */
    public Result propose(Http.Request request, Long destinationId) {

        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }

        Destination destinationToMutate = destinationRepo.fetch(destinationId);

        if (destinationToMutate == null) {
            return notFound();
        }

        JsonNode jsonBody = request.body().asJson();

        if(jsonBody == null || !jsonBody.isArray()) {
            return badRequest();
        }

        Set<TravellerType> currentTravellerTypes = destinationToMutate.getTravellerTypes();
        Set<TravellerType> proposedTravellerTypes = new HashSet<>(getTravellerTypeFromNode(jsonBody));

        // Prevent the user from proposing traveller types that do not already exist
        proposedTravellerTypes.retainAll(
                travellerTypeRepo.findAll()
        );

        // Proposed to add = proposed set - current set
        Set<TravellerType> proposedAddTravellerTypes = new HashSet<>(proposedTravellerTypes);
        proposedAddTravellerTypes.removeAll(currentTravellerTypes);

        // Proposed to remove = current set - proposed set
        Set<TravellerType> proposedRemoveTravellerTypes = new HashSet<>(currentTravellerTypes);
        proposedRemoveTravellerTypes.removeAll(proposedTravellerTypes);

        // Set both of the proposed sets
        destinationToMutate.setProposedTravellerTypesAdd(proposedAddTravellerTypes);
        destinationToMutate.setProposedTravellerTypesRemove(proposedRemoveTravellerTypes);

        destinationRepo.save(destinationToMutate);
        return ok();
    }


    /**
     * Gets all the destinations that have traveller type proposals that are not currently accepted or rejected.
     *
     * @param request the request from the front end that contains login info.
     * @return a json result of all the destinations with proposals.
     */
    public Result fetchProposedDestinations(Http.Request request) {

        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }

        Profile loggedInUser = profileRepo.fetchSingleProfile(loggedInUserId);

        if (!loggedInUser.getIsAdmin()) {
            return forbidden();
        }

        return ok(Json.toJson(destinationRepo.fetchProposed()));
    }

}
