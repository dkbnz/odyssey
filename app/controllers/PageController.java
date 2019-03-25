package controllers;

import models.destinations.DestinationType;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


/**
 * Controller to handle the serving of pages
 */
public class PageController extends Controller {

    /**
     * Method to serve the index destinationsPage
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
    public Result destinations() {
        List<DestinationType> types = new ArrayList<>(EnumSet.allOf(DestinationType.class));
        return ok(views.html.viewDestinations.destinationsPage.render(types));
    }
}
