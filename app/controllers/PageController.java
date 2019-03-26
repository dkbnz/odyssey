package controllers;

import models.destinations.DestinationType;
import org.springframework.util.StringUtils;
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
        List<String> newDestinations = formatTypes(types);

        return ok(views.html.viewDestinations.destinationsPage.render(types, newDestinations));
    }

    private List<String> formatTypes(List<DestinationType> types) {
        List<String> newDestinations = new ArrayList<String>();
        for (int i = 0; i < types.size(); i++) {
            String toAdd = types.get(i).toString().replace('_', ' ');
            toAdd = toAdd.toLowerCase();

            ArrayList<String> wordSplit= new ArrayList<String>();
            for (String word :toAdd.split(" ")) {
                wordSplit.add(StringUtils.capitalize(word));
            }
            toAdd = String.join(" ", wordSplit);
            newDestinations.add(toAdd);
        }
        return newDestinations;
    }

    public Result searchProfiles() {
        return ok(views.html.viewProfiles.profilesPage.render());
    }
}
