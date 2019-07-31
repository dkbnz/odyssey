package controllers;

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
     * Pulls a list of Nationalities from the database and returns it as a Json list.
     *
     * @return ok() (Http 200) with the result as a Json body.
     */
    public Result list() {

        List<Nationality> nationalities = Nationality.find.all();

        return ok(Json.toJson(nationalities));
    }
}

