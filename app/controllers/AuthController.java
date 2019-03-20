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
                .getOptional("authorized")
                .map(userId -> ok("OK")) // User is already logged in, return OK
                .orElseGet(() -> {
                    // User is not logged in, attempt to search database
                    JsonNode json = request.body().asJson();
                    System.out.println(json);

                    if (!(json.has("username") && json.has("password"))) {
                        // If JSON Object contains no user or pass key, return bad request
                        // Prevents null pointer exceptions when trying to get the values below.
                        return badRequest();
                    }

                    String username = json.get("username").asText();
                    String password = json.get("password").asText();

                    Profile profile = Profile.find.query().where()
                            .like("username", username).findOne();

                    if ((profile != null) && (profile.password.equals(password))) {
                        // Profile was successfully fetched and password matches,
                        // Set session token as id and return ok (200 response)
                        return ok().addingToSession(request, "authorized", profile.id.toString());
                    }

                    return unauthorized("User/pass not found");

                });
    }

    /**
     * Clears session resulting in authorized tag being cleared,
     * Therefore deauthorizing client
     * @return ok() result, as logout should always succeed
     */
    public Result logout() {
        return ok("OK").withNewSession(); // Sets a new session, clearing the old one
    }

}
