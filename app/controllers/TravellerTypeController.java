package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import models.TravellerType;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.destinations.TravellerTypeRepository;

import java.util.List;

/**
 * Controller to handle CRUD of traveller types.
 */
public class TravellerTypeController extends Controller {


    private TravellerTypeRepository travellerTypeRepository;

    @Inject
    public TravellerTypeController(TravellerTypeRepository travellerTypeRepository) {
        this.travellerTypeRepository = travellerTypeRepository;
    }

    /**
     * Pulls a list of TravellerTypes from the database and returns it as a Json list.
     *
     * @return ok() (Http 200) with the result with a Json body.
     */
    public Result list() {

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode results = mapper.createArrayNode();
        ObjectNode travTypeobj;

        List<TravellerType> travTypes = travellerTypeRepository.findAll();


        for (TravellerType travtype : travTypes) {
            travTypeobj = (ObjectNode) Json.toJson(travtype);
            travTypeobj.remove("profiles");
            results.add(travTypeobj);
        }

        return ok(results);
    }

}