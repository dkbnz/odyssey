package controllers;

import models.destinations.DestinationTypeEnum;
import org.springframework.util.StringUtils;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


/**
 * Controller to handle the serving of pages
 */
public class PageController extends Controller {
    
    private static final String AUTHORIZED = "authorized";

    /**
     * Method to serve the index page
     */
    public Result index(Http.Request request) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> redirect("/dash")) // User is logged in, redirect to dash
                .orElseGet(() -> ok(views.html.index.page.render())); // Otherwise, present index
    }

    /**
     * Method to serve the dashboard page
     */
    public Result dash(Http.Request request) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> ok(views.html.dash.page.render())) // User is logged in, render dash
                .orElseGet(() -> redirect("/")); // User is not logged in, redirect to index
    }

    /**
     * Method to serve the search profiles page
     */
    public Result searchProfiles(Http.Request request) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> ok(views.html.viewProfiles.profilesPage.render())) // User is logged in, render dash
                .orElseGet(() -> redirect("/")); // User is not logged in, redirect to index
    }

    /**
     * Method to serve the destinations page
     */
    public Result destinations(Http.Request request) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {
                    // User is logged in, render destinations
                    List<DestinationTypeEnum> types = new ArrayList<>(EnumSet.allOf(DestinationTypeEnum.class));
                    List<String> newDestinations = formatTypes(types);

                    return ok(views.html.viewDestinations.destinationsPage.render(types, newDestinations));
                })
                .orElseGet(() -> redirect("/")); // User is not logged in, redirect to index
    }

    private List<String> formatTypes(List<DestinationTypeEnum> types) {
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
}
