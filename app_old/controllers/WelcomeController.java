package controllers;

import play.mvc.Controller;
import play.mvc.Result;
/**
 * Display Welcome/Login destinationsPage
 */
public class WelcomeController extends Controller {
    /**
     * Handle default path requests
     */
    public Result welcome() {
        return ok(views.html.welcome.render());
    }
}