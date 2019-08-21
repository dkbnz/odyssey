package controllers.profiles;

import models.profiles.Nationality;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.profiles.NationalityRepository;

import com.google.inject.Inject;
import java.util.List;

/**
 * Controller to handle CRUD of nationalities,
 * As nationalities is primarily a reference table (static),
 * These actions should only be performed by an admin.
 */
public class NationalityController extends Controller {

    private NationalityRepository nationalityRepository;

    @Inject
    public NationalityController(NationalityRepository nationalityRepository) {
        this.nationalityRepository = nationalityRepository;
    }

    /**
     * Pulls a list of Nationalities from the database and returns it as a Json list.
     *
     * @return ok() (Http 200) with the result as a Json body.
     */
    public Result list() {
        List<Nationality> nationalities = nationalityRepository.findAll();
        return ok(Json.toJson(nationalities));
    }
}

