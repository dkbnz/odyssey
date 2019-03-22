package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Controller to handle the serving of pages
 */
public class PageController extends Controller {

    /**
     * Method to serve the index page
     */
    public Result index() {
        return ok(views.html.index.render());
    }

    /**
     * Method to serve the dashboard page
     */
    public Result dash() { return ok(views.html.dashboard.render()); }
}
