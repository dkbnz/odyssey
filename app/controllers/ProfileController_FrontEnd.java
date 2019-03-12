package controllers;

import models.Computer;
import models.Profile;
import org.joda.time.DateTime;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import repository.CompanyRepository;
import repository.ComputerRepository;
import views.html.profile;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * Display Profile page
 */
public class ProfileController extends Controller {



    Profile currentUser = new Profile("James", "DaMan", "JamsKool", "password", new Date(), Arrays.asList("New Zealand", "Australia", "Egypt"), "Male", Arrays.asList("New Zealand", "Australia"));

    /**
     * Handle default path requests
     */
    public Result profile() {
        return ok(views.html.profile.render(currentUser));
    }
}
