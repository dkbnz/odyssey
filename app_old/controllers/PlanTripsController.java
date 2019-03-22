package controllers;

import models.Computer;
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

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import models.Destination;
import models.Profile;


/**
 * Display Profile page
 */
public class PlanTripsController extends Controller {
    /**
     * Handle default path requests
     */
//    @Inject
//    FormFactory formFactory;
//

    public Result plan() {
        return ok(views.html.plan.render());
    }

    public Result saveDestination(Http.Request request) {
        //Save the destination to the database of destinations here

//        Form<Destination> destinationForm = FormFactory.form(Destination.class).bindFromRequest(request);
//
//        Destination destination = destinationForm.get();
//        System.out.println(profile.getFirstName());
//        System.out.println(profile.getLastName());
//        System.out.println(profile.getUsername());
        return ok(views.html.plan.render());
    }

//    public void validate() {
//        System.out.println("WORKING");
//
//    }

}


