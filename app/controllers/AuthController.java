package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Profile;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

/**
 * Controller to handle the authorisation of clients.
 * Sets session cookies and
 */
public class AuthController extends Controller {


    private static final String USERNAME = "username";
    private static final String PASSFIELD = "password";
    private static final String AUTHORIZED = "authorized";

    /**
     * Checks if client is authorized, if authorized, return ok()
     * If not, checks if user exists in the database.
     * If exists, set session and return okay
     * Any other cases return appropriate error
     *
     * @param request HTTP request from the client
     * @return Result of users request
     */
    public Result login(Http.Request request) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> ok("OK")) // User is already logged in, return OK
                .orElseGet(() -> { // orElseGet tries to get the `getOptional` value, otherwise executes the following function

                    // User is not logged in, attempt to search database
                    JsonNode json = request.body().asJson();

                    // Check if a body was given and has required fields
                    if (json == null || (!(json.has(USERNAME) && json.has(PASSFIELD)))) {
                        // If JSON Object contains no user or pass key, return bad request
                        // Prevents null pointer exceptions when trying to get the values below.
                        System.out.println("InvalidJSON");
                        return badRequest();
                    }

                    String username = json.get(USERNAME).asText();
                    String password = json.get(PASSFIELD).asText();

                    Profile profile = Profile.find.query().where()
                            .like(USERNAME, username).findOne();

                    if ((profile != null) && (profile.password.equals(password))) {
                        // Profile was successfully fetched and password matches,
                        // Set session token as id and return ok (200 response)
                        return ok().addingToSession(request, AUTHORIZED, profile.id.toString());
                    }

                    return unauthorized("User/pass not found");

                });
    }

    /**
     * Clears session resulting in authorized tag being cleared,
     * Therefore deauthorizing client
     *
     * @return ok() result, as logout should always succeed
     */
    public Result logout() {
        return ok("OK").withNewSession(); // Sets a new session, clearing the old one
    }

}
