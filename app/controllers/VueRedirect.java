package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class VueRedirect extends Controller {

    /**
     * Catch all method to serve the index page whenever a route is not found.
     * This is used in production so that reloading a vue page will not result in a 404 not found error.
     *
     * @param path  path that the client is requesting.
     * @return      vue frontend index page.
     */
    public Result matchAll(String path) {
        return ok(VueRedirect.class.getResourceAsStream("/public/index.html")).as("text/html");
    }

}