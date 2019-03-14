package controllers;

import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * Display Signup page
 */
public class SignUpController extends Controller {


    /**
     * Handle default path requests
     */
    @Inject
    FormFactory formFactory;
    private Form<ProfileFormData> form;


    /**
     * Starts the sign up page and initializes the profile form
     * @return send the profile form to the sign up html doc
     */
    public Result signUp() {
        this.form = formFactory.form(ProfileFormData.class);
        return ok(views.html.signUp.render(form));
    }


    /**
     * Saves the users entry after they have hit the next button
     * @param request the request holding all the profile information
     * @return redirects to the traveller page
     */
    public Result save(Http.Request request) {
        //Gets the JSON from the request
        JsonNode json = request.body().asJson();

        // Gets the username from the body from the JSON
        String username = json.get("username").asText();

        // searches databse for a username matching the JSON username
        Profile profile = Profile.find.query().where()
                .like("username", username).findOne();

        if (profile == null) {
            return badRequest();
        } else {
            return ok(json);
        }


        return badRequest();
    }
}
