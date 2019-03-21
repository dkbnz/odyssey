package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Display Welcome/Login page
 */
public class PageController extends Controller {
    /**
     * Handle default path requests
     */
    public Result index() {
        return ok(views.html.index.render());
    }
}
