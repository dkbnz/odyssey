package controllers.profiles;

import io.ebean.ExpressionList;
import models.profiles.Nationality;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.profiles.NationalityRepository;

import com.google.inject.Inject;

/**
 * Controller to handle CRUD of nationalities,
 * As nationalities is primarily a reference table (static),
 * These actions should only be performed by an admin.
 */
public class NationalityController extends Controller {
    private static final String NATIONALITY_VALUE = "nationality";

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
        ExpressionList<Nationality> expressionList = nationalityRepository.getExpressionList();
        return ok(Json.toJson(expressionList.where().orderBy().asc(NATIONALITY_VALUE).findList()));
    }
}

