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
        return ok(Json.toJson(travellerTypeRepository.findAll()));
    }

}