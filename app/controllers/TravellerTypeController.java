package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.TravellerType;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Controller to handle CRUD of nationalities,
 * As nationalities is primarily a reference table (static),
 * These actions should only be performed by an admin.
 */
public class TravellerTypeController extends Controller {

    /**
     * Pulls a list of TravellerTypes from the database and returns it as a JSON list
     *
     * @return Http Result with a json body
     */
    public Result list() {

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode results = mapper.createArrayNode();
        ObjectNode travTypeobj;

        List<TravellerType> travTypes = TravellerType.find.all();


        for (TravellerType travtype : travTypes) {
            travTypeobj = (ObjectNode) Json.toJson(travtype);
            travTypeobj.remove("profiles");
            results.add(travTypeobj);
        }

        return ok(results);
    }

}