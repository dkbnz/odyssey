package controllers.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.profiles.Profile;
import models.util.ApiError;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.profiles.ProfileRepository;
import util.AuthenticationUtil;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller to handle the authorisation of clients.
 * Sets session cookies when user is logging in, handles hashing a user's password and handles logging out.
 */
public class AuthenticationController extends Controller {

    private static final Logger LOGGER = Logger.getLogger( AuthenticationController.class.getName() );
    private static final String USERNAME = "username";
    private static final String PASS_FIELD = "password";
    private static final String AUTHORIZED = "authorized";
    private static final String LOGGED_IN = "OK, logged In";
    private static final String ALREADY_LOGGED_IN = "OK, Already Logged In";
    private static final String LOGGED_OUT = "OK, Logged out";
    private static final String HASH_FAIL = "Invalid JSON: JSON Object contains no user or password key";
    private static final Integer DAY_IN_MS = 86400000;
    private static final String TIME_FIELD = "clientTime";

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

        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);

        if (loggedInUser == null) {
            // user is not logged in and attempt to log in with user and pass key
            JsonNode loginJson = request.body().asJson();

            // Check if a body was given and has required fields
            if (loginJson == null || (!(loginJson.has(USERNAME) && loginJson.has(PASS_FIELD)))) {
                // If JSON Object contains no user or pass key, return bad request
                // Prevents null pointer exceptions when trying to get the values below.
                return badRequest(ApiError.invalidJson());
            }

            String username = loginJson.get(USERNAME).asText();

            // Uses the hashProfilePassword() method to hash the given password.
            String password = null;
            try {
                password = AuthenticationUtil.hashProfilePassword(loginJson.get(PASS_FIELD).asText());
            } catch (NoSuchAlgorithmException e) {
                LOGGER.log(Level.SEVERE, HASH_FAIL, e);
                return badRequest(ApiError.badRequest(HASH_FAIL));
            }

            Profile profile = profileRepository.getExpressionList()
                    .like(USERNAME, username).findOne();

            if ((profile != null) && (profile.getPassword().equals(password))) {
                // Profile was successfully fetched and password matches,
                // checks if needing tpo increment the streaker badge
                //checkStreakIncrement(profile);
                // Set session token as id and return ok (200 response)
                return ok(LOGGED_IN).addingToSession(request, AUTHORIZED, profile.id.toString());
            }

            return unauthorized(ApiError.unauthorized());
        } else {
            // user is logged in
            //checkStreakIncrement(loggedInUser);
            return ok(ALREADY_LOGGED_IN);
        }
    }


    /**
     * Clears session resulting in authorized tag being cleared, therefore de-authorizing client.
     *
     * @return ok() (Http 200) result, as logout should always succeed.
     */
    public Result logout() {
        return ok(LOGGED_OUT).withNewSession(); // Sets a new session, clearing the old one
    }


    private void checkStreakIncrement(Profile profile, Date clientTime) {

//
//        Date lastSeen = profile.getLastSeen();
//        Date incrementTime = profile.getIncrementTime();
//        Date streakTimeout = new Date(lastSeen + 2);


//        LocalDate localDateLastLogin = lastLogin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        int yearLastLogin  = localDateLastLogin.getYear();
//        int monthLastLogin = localDateLastLogin.getMonthValue();
//        int dayLastLogin  = localDateLastLogin.getDayOfMonth();
//
//        Date daysAgoDate = new Date(System.currentTimeMillis() - DAY_IN_MS);
//
//        LocalDate localDate = daysAgoDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        int yearTest  = localDate.getYear();
//        int monthTest = localDate.getMonthValue();
//        int dayTest   = localDate.getDayOfMonth();

//        profile.setLastLogin(daysAgoDate);
        profileRepository.update(profile);

//        Profile profileTest = profileRepository.findById(Long.valueOf(userId));

//        LocalDate profileLastLogin = profileTest.getLastLogin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//
//        int yearProfile  = profileLastLogin.getYear();
//        int monthProfile = profileLastLogin.getMonthValue();
//        int dayProfile   = profileLastLogin.getDayOfMonth();

    }
}
