package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Nationality;
import models.TravellerType;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Controller to handle CRUD of nationalities,
 * As nationalities is primarily a reference table (static),
 * These actions should only be performed by an admin
 */
public class NationalityController extends Controller {

    public Result list() {

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode results = mapper.createArrayNode();
        ObjectNode nationalityobj;

        List<Nationality> nationalities = Nationality.find.all();

        for(Nationality nationality : nationalities){
            results.add(Json.toJson(nationality));
        }

        return ok(results);
    }
}

