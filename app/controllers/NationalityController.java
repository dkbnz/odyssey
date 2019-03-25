package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Nationality;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Controller to handle CRUD of nationalities,
 * As nationalities is primarily a reference table (static),
 * These actions should only be performed by an admin.
 */
public class NationalityController extends Controller {

    /**
     * Pulls a list of Nationalities from the database and returns it as a JSON list
     *
     * @return Http Result with a json body
     */
    public Result list() {

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode results = mapper.createArrayNode();

        List<Nationality> nationalities = Nationality.find.all();

        for (Nationality nationality : nationalities) {
            results.add(Json.toJson(nationality));
        }

        return ok(results);
    }
}

