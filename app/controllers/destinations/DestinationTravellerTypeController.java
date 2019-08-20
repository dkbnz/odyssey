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

    private DestinationRepository destinationRepository;
    private TravellerTypeRepository travellerTypeRepository;
    private ProfileRepository profileRepository;

    @Inject
    public DestinationTravellerTypeController(DestinationRepository destinationRepository,
                                              TravellerTypeRepository travellerTypeRepository,
                                              ProfileRepository profileRepository) {
        this.destinationRepository = destinationRepository;
        this.travellerTypeRepository = travellerTypeRepository;
        this.profileRepository = profileRepository;
    }


    /**
     * Takes a JsonNode that is an array of traveller types. Will then iterate
     * over the array and produce a list of traveller types.
     *
     * @param travellerTypesNode    a JsonNode containing a list of serialized traveller types.
     * @return                      a list of traveller types.
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
     * @param request           an Http request containing a Json body of traveller types.
     * @param destinationId     id of the destination for which the traveller types are set.
     * @return                  ok() (Http 200) if successfully created.
     *                          unauthorised() (Http 401) if the user is not logged in.
     *                          notFound() (Http 404) if destination could not found.
     *                          forbidden() (Http 403) if the logged in user cannot access the resource.
     */
    public Result addTravellerTypes(Http.Request request, Long destinationId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized();
        }

        Destination destinationToMutate = destinationRepository.findById(destinationId);

        if (destinationToMutate == null) {
            return notFound();
        }


        if (!AuthenticationUtil.validUser(loggedInUser, destinationToMutate.getOwner())) {
            return forbidden();
        }

        JsonNode jsonBody = request.body().asJson();

        Set<TravellerType> travellerTypesToSet = new HashSet<>(
                getTravellerTypeFromNode(jsonBody)
        );

        // Prevent the user from adding traveller types that do not already exist
        travellerTypesToSet.retainAll(
                travellerTypeRepository.findAll()
        );

        destinationToMutate.setTravellerTypes(
                travellerTypesToSet
        );

        destinationRepository.save(destinationToMutate);
        return ok(Json.toJson(destinationToMutate));
    }


    /**
     * Method to propose traveller types for a destination.
     * Request body is expected to be a jsonArray of traveller types.
     * If method executes successfully, given traveller types will be split between
     * proposed to add or proposed to remove depending on their presence in the current set.
     *
     * @param request           an Http request containing a Json body of traveller types.
     * @param destinationId     id of the destination for which the traveller types are proposed.
     * @return                  ok() (Http 200) if successfully proposed.
     *                          unauthorised() (Http 401) if the user is not logged in.
     *                          notFound() (Http 404) if destination could not found.
     *                          badRequest() (Http 400) if the Json body is formatted incorrectly or not provided.
     */
    public Result propose(Http.Request request, Long destinationId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized();
        }

        Destination destinationToMutate = destinationRepository.findById(destinationId);

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
                travellerTypeRepository.findAll()
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

        destinationRepository.save(destinationToMutate);
        return ok(Json.toJson(destinationToMutate));
    }


    /**
     * Gets all the destinations that have traveller type proposals that are not currently accepted or rejected.
     *
     * @param request           the request from the front end that contains login info.
     * @return                  ok() (Http 200) containing all destinations with proposals.
     *                          unauthorised() (Http 401) if the user is not logged in.
     *                          forbidden() (Http 403) if the logged in user cannot access the resource.
     */
    public Result fetchProposedDestinations(Http.Request request) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized();
        }

        if (!loggedInUser.isAdmin()) {
            return forbidden();
        }

        return ok(Json.toJson(destinationRepository.fetchProposed()));
    }
}
