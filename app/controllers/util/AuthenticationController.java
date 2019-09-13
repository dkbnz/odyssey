package controllers.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.profiles.Profile;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.profiles.ProfileRepository;
import util.AuthenticationUtil;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller to handle the authorisation of clients.
 * Sets session cookies when user is logging in, handles hashing a user's password and handles logging out.
 */
public class AuthenticationController extends Controller {

    private static final Logger LOGGER = Logger.getLogger( AuthenticationController.class.getName() );
    private static final String USERNAME = "username";
    private static final String AUTHENTICATION_FIELD = "password";
    private static final String AUTHORIZED = "authorized";

    private ProfileRepository profileRepository;

    @Inject
    public AuthenticationController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    /**
     * Checks if client is authorized, if authorized, return ok() (Http 200) .If not, checks if user exists in the
     * database. If exists, set session and return ok() (Http 200).
     *
     * @param request   Http request from the client.
     * @return          ok() (Http 200) if the user is logged in/successfully logged in, badRequest() (Http 400) if the
     *                  user credentials are invalid, unauthorized() (Http 401) if no profile matching the user's
     *                  credentials can be found.
     */
    public Result login(Http.Request request) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> ok("OK, Already logged In")) // User is already logged in, return OK
                .orElseGet(() -> { // orElseGet tries to get the `getOptional` value, otherwise executes the following function

                    // User is not logged in, attempt to search database
                    JsonNode loginJson = request.body().asJson();

                    // Check if a body was given and has required fields
                    if (loginJson == null || (!(loginJson.has(USERNAME) && loginJson.has(AUTHENTICATION_FIELD)))) {
                        // If JSON Object contains no user or pass key, return bad request
                        // Prevents null pointer exceptions when trying to get the values below.
                        return badRequest("Bad User Credentials");
                    }

                    String username = loginJson.get(USERNAME).asText();

                    // Uses the hashProfilePassword() method to hash the given password.
                    String password = null;
                    try {
                        password = AuthenticationUtil.hashProfilePassword(loginJson.get(AUTHENTICATION_FIELD).asText());
                    } catch (NoSuchAlgorithmException e) {
                        LOGGER.log(Level.SEVERE, "Invalid JSON: JSON Object contains no user or password key", e);
                    }

                    Profile profile = profileRepository.getExpressionList()
                            .like(USERNAME, username).findOne();

                    if ((profile != null) && (profile.getPassword().equals(password))) {
                        // Profile was successfully fetched and password matches,
                        // Set session token as id and return ok (200 response)
                        return ok("Logged In").addingToSession(request, AUTHORIZED, profile.id.toString());
                    }

                    return unauthorized("Invalid user credentials provided");
                });
    }


    /**
     * Clears session resulting in authorized tag being cleared, therefore de-authorizing client.
     *
     * @return ok() (Http 200) result, as logout should always succeed.
     */
    public Result logout() {
        return ok("OK, Logged out").withNewSession(); // Sets a new session, clearing the old one
    }
}
