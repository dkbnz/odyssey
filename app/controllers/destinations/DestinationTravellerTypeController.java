package controllers.destinations;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.TravellerType;
import models.destinations.Destination;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repositories.destinations.DestinationRepository;
import repositories.destinations.TravellerTypeRepository;

import java.util.HashSet;
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

    /**
     *
     * @param request
     * @param destinationId
     * @return
     */
    public Result create(Http.Request request, Long destinationId) {

        Destination destinationToMutate = destinationRepo.fetch(destinationId);

        // Check for null
        Set<TravellerType> travellerTypesToSet = new HashSet<>();

        JsonNode body = request.body().asJson();

        while (body.iterator().hasNext()) {
            System.out.println(body.iterator().hasNext());
            body.iterator().next();
            travellerTypesToSet.add(Json.fromJson(body.iterator().next(), TravellerType.class));
            body.iterator().remove();
        }

        destinationToMutate.setTravellerTypes(travellerTypesToSet);

        System.out.println(destinationToMutate.getTravellerTypes());
        destinationRepo.save(destinationToMutate);
        destinationToMutate = destinationRepo.fetch(destinationId);
        System.out.println(destinationToMutate.getTravellerTypes());
        return ok();
    }

    /**
     *
     * @param request
     * @param destinationId
     * @param travellerTypeId
     * @return
     */
    public Result destroy(Http.Request request, Long destinationId, Long travellerTypeId) {
        Destination destinationToMutate = destinationRepo.fetch(destinationId);
        TravellerType travellerTypeToAdd = travellerTypeRepo.findById(travellerTypeId);
        destinationToMutate.removeTravellerType(travellerTypeToAdd);
        destinationRepo.save(destinationToMutate);
        return ok();
    }

    /**
     *
     * @param request
     * @param destinationId
     * @param travellerTypeId
     * @return
     */
    public Result createAddRequest(Http.Request request, Long destinationId, Long travellerTypeId) {
        Destination destinationToMutate = destinationRepo.fetch(destinationId);
        TravellerType travellerTypeToAdd = travellerTypeRepo.findById(travellerTypeId);
        destinationToMutate.addProposedTravellerTypeAdd(travellerTypeToAdd);
        destinationRepo.save(destinationToMutate);
        return ok();
    }

    /**
     *
     * @param request
     * @param destinationId
     * @param travellerTypeId
     * @return
     */
    public Result createRemoveRequest(Http.Request request, Long destinationId, Long travellerTypeId) {
        Destination destinationToMutate = destinationRepo.fetch(destinationId);
        TravellerType travellerTypeToAdd = travellerTypeRepo.findById(travellerTypeId);
        destinationToMutate.addProposedTravellerTypeRemove(travellerTypeToAdd);
        destinationRepo.save(destinationToMutate);
        return ok();
    }

}
