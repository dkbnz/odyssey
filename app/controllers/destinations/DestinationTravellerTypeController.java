package controllers.destinations;

import com.google.inject.Inject;
import models.TravellerType;
import models.destinations.Destination;
import play.mvc.Http;
import play.mvc.Result;
import repositories.destinations.DestinationRepository;
import repositories.destinations.TravellerTypeRepository;

import static play.mvc.Results.ok;

public class DestinationTravellerTypeController {

    private DestinationRepository destinationRepo;
    private TravellerTypeRepository travellerTypeRepo;

    @Inject
    public DestinationTravellerTypeController(DestinationRepository destinationRepo, TravellerTypeRepository travellerTypeRepo) {
        this.destinationRepo = destinationRepo;
        this.travellerTypeRepo = travellerTypeRepo;
    }

    public Result create(Http.Request request, Long destinationId, Long travellerTypeId) {
        Destination destinationToMutate = destinationRepo.fetch(destinationId);
        TravellerType travellerTypeToAdd = travellerTypeRepo.findById(travellerTypeId);
        destinationToMutate.addTravellerType(travellerTypeToAdd);
        System.out.println(destinationToMutate.getTravellerTypes());
        destinationRepo.save(destinationToMutate);
        return ok();
    }

    public Result destroy(Http.Request request, Long destinationId, Long travellerTypeId) {
        Destination destinationToMutate = destinationRepo.fetch(destinationId);
        TravellerType travellerTypeToAdd = travellerTypeRepo.findById(travellerTypeId);
        destinationToMutate.removeTravellerType(travellerTypeToAdd);
        destinationRepo.save(destinationToMutate);
        return ok();
    }

    public Result createRequest(Http.Request request, Long destinationId, Long travellerTypeId) {
        Destination destinationToMutate = destinationRepo.fetch(destinationId);
        TravellerType travellerTypeToAdd = travellerTypeRepo.findById(travellerTypeId);
        destinationToMutate.addProposedTravellerType(travellerTypeToAdd);
        destinationRepo.save(destinationToMutate);
        return ok();
    }

}
