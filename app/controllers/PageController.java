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
        return ok(views.html.index.page.render());
    }

    /**
     * Method to serve the dashboard destinationsPage
     */
    public Result dash() { return ok(views.html.dash.page.render()); }

    /**
     * Method to serve the destinations page
     */
    public Result destinations() { return ok(views.html.viewDestinations.destinationsPage.render()); }
}
