package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Manage a database of profiles
 */
public class HomeController extends Controller {

    public Result index() {
        return ok(views.html.index.render());
    }
}
            
