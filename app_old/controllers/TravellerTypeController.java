package controllers;

import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

public class TravellerTypeController extends Controller {

    @Inject
    FormFactory formFactory;
    private Form<ProfileFormData> form;

    public Result select() {
        this.form = formFactory.form(ProfileFormData.class);
        return ok(views.html.selectTravellerTypes.render(form));
    }

    public Result finish(Http.Request request) {
        Form<ProfileFormData> profileForm = formFactory.form(ProfileFormData.class).bindFromRequest(request);

        ProfileFormData profile = profileForm.get();
        return redirect(routes.ProfileController.profile());
    }
}
