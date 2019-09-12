package controllers.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import controllers.points.AchievementTrackerController;
import models.profiles.Profile;
import models.util.ApiError;
import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.profiles.ProfileRepository;
import util.AuthenticationUtil;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.joda.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
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
    private static final Integer HOURS_IN_A_DAY = 24;
    private static final Integer STARTING_STREAK_NUMBER = 1;
    private static final Integer LOST_STREAK = 0;
    private static final String TIME_FIELD = "clientTime";
    private static final String TIME_OFFSET = "timeOffset";

    private ProfileRepository profileRepository;
    private AchievementTrackerController achievementTrackerController;

    @Inject
    public AuthenticationController(ProfileRepository profileRepository, AchievementTrackerController achievementTrackerController) {
        this.achievementTrackerController = achievementTrackerController;
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
            if (loginJson == null ||
                    (!(loginJson.has(USERNAME) &&
                            loginJson.has(PASS_FIELD)))) {
                // If JSON Object contains no user or pass key, return bad request
                // Prevents null pointer exceptions when trying to get the values below.
                return badRequest(ApiError.invalidJson());

//                 &&
//                loginJson.has(TIME_FIELD) &&
//                        loginJson.has(TIME_OFFSET)
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

//            String clientTime = loginJson.get(TIME_FIELD).asText();
//            int timeOffset = loginJson.get(TIME_OFFSET).asInt();

            if ((profile != null) && (profile.getPassword().equals(password))) {
                // Profile was successfully fetched and password matches,
                // checks if needing to increment the streaker badge
//                checkStreakIncrement(profile, clientTime, timeOffset);
                // Set session token as id and return ok (200 response)
                return ok(Json.toJson(LOGGED_IN)).addingToSession(request, AUTHORIZED, profile.id.toString());
            }

            return unauthorized(ApiError.unauthorized());
        } else {
            // user is logged in

            JsonNode loginJson = request.body().asJson();

            if (loginJson == null ||
                    (!(loginJson.has(USERNAME) &&
                            loginJson.has(PASS_FIELD)))) {
                // If JSON Object contains no user or pass key, return bad request
                // Prevents null pointer exceptions when trying to get the values below.
                return badRequest(ApiError.invalidJson());
            }
//
//            String clientTime = loginJson.get(TIME_FIELD).asText();
//            int timeOffset = loginJson.get(TIME_OFFSET).asInt();
//
//            checkStreakIncrement(loggedInUser, clientTime, timeOffset);
            return ok(Json.toJson(ALREADY_LOGGED_IN));
        }
    }


    /**
     * Clears session resulting in authorized tag being cleared, therefore de-authorizing client.
     *
     * @return ok() (Http 200) result, as logout should always succeed.
     */
    public Result logout() {
        return ok(Json.toJson(LOGGED_OUT)).withNewSession(); // Sets a new session, clearing the old one
    }


    private void checkStreakIncrement(Profile profile, String clientTime, int timeOffset) {


        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
        DateTime clientDateTime = formatter.parseDateTime(clientTime);

        // UTC time converted from the clients time and timezone
        DateTime utcDateTime = clientDateTime.plusMinutes(timeOffset);

        // The previous time the user was seen using the application before this interaction
        DateTime lastSeen = profile.getLastSeen();

        // The time at which the profile should be awarded an increase in their streak
        DateTime incrementTime = profile.getIncrementTime();

        if (incrementTime == null && lastSeen == null) {
            // Users first time (sign up)
            profile.getAchievementTracker().setCurrentStreak(STARTING_STREAK_NUMBER);
            profile.setIncrementTime(utcDateTime.plusHours(HOURS_IN_A_DAY));

        } else {

            if (utcDateTime.isBefore(lastSeen.plusHours(HOURS_IN_A_DAY))) {
                // User has not lost their streak just pushes their boundary time

                if (utcDateTime.isAfter(incrementTime)) {
                    // Passed the increment time and so adds to the users streak
                    profile.getAchievementTracker().addToCurrentStreak();
                    profile.setIncrementTime(utcDateTime.plusHours(HOURS_IN_A_DAY));
                    achievementTrackerController.rewardAction(profile);
                }

            } else {
                // User has lost their streak
                profile.getAchievementTracker().setCurrentStreak(LOST_STREAK);
                profile.setIncrementTime(utcDateTime.plusHours(HOURS_IN_A_DAY));
            }
        }

        profile.setLastSeen(utcDateTime);


        profileRepository.update(profile);

    }
}
