package controllers.destinations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.Inject;
import models.TravellerType;
import models.destinations.Destination;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repositories.destinations.DestinationRepository;
import repositories.destinations.TravellerTypeRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static play.mvc.Results.ok;

public class DestinationTravellerTypeController {

    private DestinationRepository destinationRepo;
    private TravellerTypeRepository travellerTypeRepo;

    @Inject
    public DestinationTravellerTypeController(DestinationRepository destinationRepo, TravellerTypeRepository travellerTypeRepo) {
        this.destinationRepo = destinationRepo;
        this.travellerTypeRepo = travellerTypeRepo;
    }

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
     * Method to set the traveller types for a destination
     *
     * @param request
     * @param destinationId
     * @return
     */
    public Result create(Http.Request request, Long destinationId) {

        // Authenticate

        Destination destinationToMutate = destinationRepo.fetch(destinationId);

        // Check for null

        JsonNode jsonBody = request.body().asJson();

        destinationToMutate.setTravellerTypes(
                new HashSet<>(
                        getTravellerTypeFromNode(jsonBody)
                )
        );

        // Decisions have been made, clear proposal sets.
        destinationToMutate.getProposedTravellerTypesAdd().clear();
        destinationToMutate.getProposedTravellerTypesRemove().clear();

        destinationRepo.save(destinationToMutate);
        return ok();
    }

    /**
     * Method to propose traveller types for a destination
     *
     * @param request
     * @param destinationId
     * @return
     */
    public Result propose(Http.Request request, Long destinationId) {
        // Authenticate

        Destination destinationToMutate = destinationRepo.fetch(destinationId);

        // Check for null

        JsonNode jsonBody = request.body().asJson();

        Set<TravellerType> currentTravellerTypes = destinationToMutate.getTravellerTypes();
        Set<TravellerType> proposedTravellerTypes = new HashSet<>(getTravellerTypeFromNode(jsonBody));

        // Proposed to add. Is given set - current set
        Set<TravellerType> proposedAddTravellerTypes = new HashSet<>(proposedTravellerTypes);
        proposedAddTravellerTypes.removeAll(currentTravellerTypes);

        // Proposed to remove. Is current set - given set
        Set<TravellerType> proposedRemoveTravellerTypes = new HashSet<>(currentTravellerTypes);
        proposedRemoveTravellerTypes.removeAll(proposedTravellerTypes);

        destinationToMutate.setProposedTravellerTypesAdd(proposedAddTravellerTypes);
        destinationToMutate.setProposedTravellerTypesRemove(proposedRemoveTravellerTypes);

        destinationRepo.save(destinationToMutate);
        return ok();
    }

}
