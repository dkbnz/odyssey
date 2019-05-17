package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import models.Nationality;
import models.Passport;
import models.Profile;
import models.TravellerType;
import play.mvc.Http;
import play.mvc.Result;
import play.libs.Json;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static play.mvc.Results.*;


/**
 * Controller to handle the CRUD of Profiles
 */
public class ProfileController {

    private static final Logger LOGGER = Logger.getLogger( ProfileController.class.getName() );
    private static final String USERNAME = "username";
    private static final String PASS_FIELD = "password";
    private static final String FIRST_NAME = "firstName";
    private static final String MIDDLE_NAME = "middleName";
    private static final String LAST_NAME = "lastName";
    private static final String PASSPORT = "passports";
    private static final String NATIONALITY = "nationalities";
    private static final String GENDER = "gender";
    private static final String MIN_AGE = "min_age";
    private static final String MAX_AGE = "max_age";
    private static final String TRAVELLER_TYPE = "travellerTypes";
    private static final String DATE_OF_BIRTH = "dateOfBirth";
    private static final String NATIONALITY_FIELD = "nationalities.nationality";
    private static final String TRAVELLER_TYPE_FIELD = "travellerTypes.travellerType";
    private static final String CREATED_BY_ADMIN = "createdByAdmin";
    private static final String AUTHORIZED = "authorized";
    private static final String NOT_SIGNED_IN = "You are not logged in.";
    private static final long AGE_SEARCH_OFFSET = 1;
    private static final long DEFAULT_ADMIN_ID = 1;
    private static final String UPDATED = "UPDATED";
    private static final String ID = "id";

    /**
     * Creates a user based on given Json body. All new users are not an admin by default. This is used on the Sign Up
     * page when a user is making a new profile. All parameters are compulsory, except for passport country. When a user
     * creates a new profile, a session is made and they are automatically logged in.
     * @param request   Http Request containing Json Body.
     * @return          If username exists, returns badRequest() (Http 400), if user is created, sets session and
     *                  returns created() (Http 201).
     */
    public Result create(Http.Request request) {

        JsonNode json = request.body().asJson();

        if (!(json.has(USERNAME)
                && json.has(PASS_FIELD)
                && json.has(FIRST_NAME)
                && json.has(MIDDLE_NAME)
                && json.has(LAST_NAME)
                && json.has(DATE_OF_BIRTH)
                && json.has(GENDER)
                && json.has(NATIONALITY)
                && json.has(PASSPORT)
                && json.has(TRAVELLER_TYPE)
        ) || profileExists(json.get(USERNAME).asText())) {
            return badRequest();
        }

        if(json.get(NATIONALITY).size() == 0
        || json.get(TRAVELLER_TYPE).size() == 0) {
            return badRequest();
        }

        Profile newUser = new Profile();

        // Uses the hashProfilePassword() method to hash the given password.
        try {
            newUser.setPassword(hashProfilePassword(json.get(PASS_FIELD).asText()));
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Unable to hash the user password", e);
        }

        newUser.setUsername(json.get(USERNAME).asText());
        newUser.setFirstName(json.get(FIRST_NAME).asText());
        newUser.setMiddleName(json.get(MIDDLE_NAME).asText());
        newUser.setLastName(json.get(LAST_NAME).asText());
        newUser.setGender(json.get(GENDER).asText());
        newUser.setDateOfBirth(LocalDate.parse(json.get(DATE_OF_BIRTH).asText()));
        newUser.setDateOfCreation(new Date());
        newUser.setIsAdmin(false);

        newUser.save();

        Consumer<JsonNode> nationalityAction = (JsonNode node) -> {
            Nationality newNat = Nationality.find.byId(node.asInt());
            newUser.addNationality(newNat);
        };

        json.get(NATIONALITY).forEach(nationalityAction);

        Consumer<JsonNode> passportAction = (JsonNode node) -> {
            Passport newPass = Passport.find.byId(node.asInt());
            newUser.addPassport(newPass);
        };

        json.get(PASSPORT).forEach(passportAction);

        Consumer<JsonNode> travTypeAction = (JsonNode node) -> {
            TravellerType travType = TravellerType.find.byId(node.asInt());
            newUser.addTravType(travType);
        };

        json.get(TRAVELLER_TYPE).forEach(travTypeAction);

        newUser.save();

        if (json.get(CREATED_BY_ADMIN).asText().equals("true")) {
            return created();
        } else {
            return created().addingToSession(request, AUTHORIZED, newUser.id.toString());
        }
    }

    /**
     * Hashes a password string using the SHA 256 method from the MessageDigest library.
     * @param password                  the string you want to hash.
     * @return                          a string of the hashed binary array as a hexadecimal string.
     * @throws NoSuchAlgorithmException if the algorithm specified does not exist for the MessageDigest library.
     */
    private String hashProfilePassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return DatatypeConverter.printHexBinary(digest.digest(password.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Field validation method checking whether a username already exists in the database. This is to ensure there are
     * no duplicate usernames (emails), as the login functionality requires a username. This is checked on profile
     * creation in the ProfileController.
     *
     * @param username  the name being checked (inputted as a String).
     * @return          false if the username is unique (acceptable), or true if the profile username exists
     *                  (unacceptable).
     */
    private boolean profileExists(String username) {
        return Profile.find
                .query()
                .where()
                .like(USERNAME, username)
                .findOne() != null;
    }

    /**
     * Field validation method checking whether a username already exists in the database. This is to ensure there are
     * no duplicate usernames (emails), as the login functionality requires a username. This error is shown to the
     * user when validating their email on Sign Up.
     *
     * @param request   Http Request containing Json Body.
     * @return          ok() (Http 200) when there is no username in the database, or a badRequest() (Http 400) when
     *                  there is already is a user with that username in the database.
     */
    public Result checkUsername(Http.Request request) {
        JsonNode json = request.body().asJson();

        if (!json.has(USERNAME)) {
            return badRequest();
        }

        String username = json.get(USERNAME).asText();

        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {
                    // User is logged in, used for editing
                    Profile userProfile = Profile.find.byId(Integer.valueOf(userId));

                    if (!profileExists(username) || userProfile.getUsername().equals(username)) {
                        return ok(); // If they are checking their own username, return ok()
                    } else {
                        return badRequest();
                    }
                })
                .orElseGet(() -> {
                    //User is not logged in, used for signup"You are not logged in."
                    if (!profileExists(username)) {
                        return ok();
                    } else {
                        return badRequest();
                    }
                }); // User is not logged in
    }

    /**
     * Fetches a single profile from the database based on the Http Request body. This is used to display the currently
     * logged in profile on the dash page, and used throughout the application wherever the logged in profile is
     * referenced.
     *
     * @param request   Http Request containing Json Body.
     * @return          If profile is successfully retrieved, returns ok() (Http 200) with the Json body of the user
     *                  profile. Otherwise returns unauthorized() (Http 401).
     */
    public Result fetch(Http.Request request) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {
                    // User is logged in
                    Profile userProfile = Profile.find.byId(Integer.valueOf(userId));
                    return ok(Json.toJson(userProfile));
                })
                .orElseGet(() -> unauthorized(NOT_SIGNED_IN)); // User is not logged in
    }

    /**
     * Deletes a currently logged in profile and invalidates their session. If user is admin and the id is specified
     * in the Json body, delete specified id. Ensures the global admin (id number of one) cannot be deleted by any
     * user, admin or not.
     *
     * @param request   Http Request containing Json Body.
     * @return          ok() (Http 200) if the profile is successfully deleted. Returns unauthorized() (Http 401),
     *                  if not logged in. Otherwise returns forbidden() (Http 403), this is logged in user is not
     *                  and admin, or they are trying to delete the global admin (id number one).
     */
    public Result delete(Http.Request request, Long id) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {
                    // User is logged in
                    Profile userProfile = Profile.find.byId(Integer.valueOf(userId));
                    if (!id.equals(Long.valueOf(userId))) { // Current user is trying to delete another user
                        // If user is admin, they can delete other profiles
                        if (userProfile.getIsAdmin()) {
                            Profile profileToDelete = Profile.find.byId(Integer.valueOf(id.intValue()));
                            if (profileToDelete.getId() == DEFAULT_ADMIN_ID) {
                                return forbidden("You can not delete the default administrator");
                            } else {
                                profileToDelete.delete();
                                return ok("Delete successful");
                            }
                        } else {
                            return forbidden("You do not have admin rights to delete other users.");
                        }
                    } else {
                        // User is deleting their own profile
                        if (userProfile.getId() == DEFAULT_ADMIN_ID) {
                            return forbidden("You can not delete the default administrator");
                        } else {
                            userProfile.delete();
                            return ok("Delete successful").withNewSession();
                        }
                    }
                })
                .orElseGet(() -> unauthorized(NOT_SIGNED_IN)); // User is not logged in
    }

    /**
     * Takes a Http request containing a Json body and finds a logged in user. Then uses a PUT request to update
     * the logged in user based on the Http Request body. The validation is the same as creating a new profile.
     *
     * If the Id is specified in the Json body, and the logged in user is an admin, then edit the specified Id.
     *
     * @param request   Http Request containing Json Body.
     * @return          ok() (Http 200) if the profile is successfully updated. Returns unauthorized() (Http 401),
     *                  if not logged in.
     */
    public Result update(Http.Request request, Long editUserId) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {
                    Profile loggedInUser = Profile.find.byId(Integer.valueOf(userId));
                    Profile profileToUpdate;

                    // If user is admin, or if they are editing their own profile then allow them to edit.
                    if(loggedInUser.getIsAdmin() || editUserId == Long.valueOf(userId)) {
                        profileToUpdate = Profile.find.byId(editUserId.intValue());
                    } else if (editUserId != Long.valueOf(userId)) {
                        return forbidden(); // User has specified an id which is not their own, but is not admin
                    } else {
                        return badRequest(); // User has not specified an id, but is trying to update their own profile
                    }

                    if (profileToUpdate == null) {
                        return badRequest(); // User does not exist in the system.
                    }

                    JsonNode json = request.body().asJson();

                    if (!(json.has(USERNAME)
                            && json.has(PASS_FIELD)
                            && json.has(FIRST_NAME)
                            && json.has(MIDDLE_NAME)
                            && json.has(LAST_NAME)
                            && json.has(DATE_OF_BIRTH)
                            && json.has(GENDER)
                            && json.has(NATIONALITY)
                            && json.has(PASSPORT)
                            && json.has(TRAVELLER_TYPE)
                    )) {
                        return badRequest();
                    }

                    if(json.get(NATIONALITY).size() == 0
                            || json.get(TRAVELLER_TYPE).size() == 0) {
                        return badRequest();
                    }

                    // If the username has been changed, and the changed username exists return badRequest();
                    if(!json.get(USERNAME).asText().equals(profileToUpdate.getUsername())
                            && profileExists(json.get(USERNAME).asText())) {
                        return badRequest();
                    }

                    if (!json.get(PASS_FIELD).asText().isEmpty()) { // Only update password if user has typed a new one

                        // Uses the hashProfilePassword() method to hash the given password.
                        try {
                            profileToUpdate.setPassword(hashProfilePassword(json.get(PASS_FIELD).asText()));
                        } catch (NoSuchAlgorithmException e) {
                            LOGGER.log(Level.SEVERE, "Unable to hash the user password", e);
                        }
                    }

                    profileToUpdate.setUsername(json.get(USERNAME).asText());
                    profileToUpdate.setFirstName(json.get(FIRST_NAME).asText());
                    profileToUpdate.setMiddleName(json.get(MIDDLE_NAME).asText());
                    profileToUpdate.setLastName(json.get(LAST_NAME).asText());
                    profileToUpdate.setDateOfBirth(LocalDate.parse(json.get(DATE_OF_BIRTH).asText()));
                    profileToUpdate.setGender(json.get(GENDER).asText());

                    profileToUpdate.clearNationalities();
                    profileToUpdate.clearPassports();
                    profileToUpdate.clearTravellerTypes();

                    // Save user profile to clear nationalities, travellerTypes and passports
                    profileToUpdate.update();

                    Consumer<JsonNode> nationalityAction = (JsonNode node) -> {
                        Nationality newNat = Nationality.find.byId(node.get(ID).asInt());
                        profileToUpdate.addNationality(newNat);
                    };

                    json.get(NATIONALITY).forEach(nationalityAction);

                    Consumer<JsonNode> passportAction = (JsonNode node) -> {
                        Passport newPass = Passport.find.byId(node.get(ID).asInt());
                        profileToUpdate.addPassport(newPass);
                    };

                    json.get(PASSPORT).forEach(passportAction);

                    Consumer<JsonNode> travTypeAction = (JsonNode node) -> {
                        TravellerType travType = TravellerType.find.byId(node.get(ID).asInt());
                        profileToUpdate.addTravType(travType);
                    };

                    json.get(TRAVELLER_TYPE).forEach(travTypeAction);

                    profileToUpdate.update();

                    return ok(UPDATED);

                })
                .orElseGet(() -> unauthorized(NOT_SIGNED_IN)); // User is not logged in
    }

    /**
     * Performs an Ebean find query on the database to search for profiles.
     * If no query is specified in the Http request, it will return a list of all profiles. If a query is specified,
     * uses the searchProfiles() method to execute a search based on the search query parameters. This is used on the
     * Search Profiles page.
     *
     * @param request  Http Request containing Json Body.
     * @return         unauthorized() (Http 401) if the user is not logged in. Otherwise returns ok() (Http 200) if the
     *                 search is successfully done.
     */
    public Result list(Http.Request request) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {
                    ObjectMapper mapper = new ObjectMapper();
                    List<Profile> profiles;

                    if (request.queryString().isEmpty()) {
                        // No query string given. retrieve all profiles
                        profiles = Profile.find.all();
                    } else {
                        String getError = validQueryString(request.queryString());
                        if (getError.isEmpty()) {
                            profiles = searchProfiles(request.queryString());
                        } else {
                            return badRequest(getError);
                        }
                    }

                    return ok(Json.toJson(profiles));
                })
                .orElseGet(() -> unauthorized(NOT_SIGNED_IN)); // User is not logged in
    }

    /**
     * Validates the search query string for profiles.
     *
     * @param queryString the query string from the request body, given by the user.
     * @return String message of error in query string, empty if no error present.
     */
    private String validQueryString(Map<String, String[]> queryString) {
        Integer minAge;
        Integer maxAge;

        try {
            minAge = Integer.valueOf(queryString.get(MIN_AGE)[0]);
            maxAge = Integer.valueOf(queryString.get(MAX_AGE)[0]);
        } catch (Exception e) {
            return "Ages cannot be converted to Integers";
        }

        if ((maxAge < 0) || (maxAge > 120)) {
            return "Max age must be between 0 and 120";
        }

        if ((minAge < 0) || (minAge > 120)) {
            return "Min age must be between 0 and 120";
        }

        if (minAge > maxAge) {
            return "Min age must be less than or equal to max age";
        }

        return "";
    }

    /**
     * Function to validate a query string and return a list of profiles based on the query string.
     * If no profiles are found, return an empty list. This is used on the Search Profiles page.
     *
     * @param queryString   the query string of the search parameters that are used for searching for profiles.
     * @return              the list of profiles found from the resulting query string (can be empty).
     */
    private List<Profile> searchProfiles(Map<String, String[]> queryString) {
        ExpressionList<Profile> profileExpressionList = Ebean.find(Profile.class).where();
        String nationality = queryString.get(NATIONALITY)[0];
        String gender = queryString.get(GENDER)[0];
        String minAge = queryString.get(MIN_AGE)[0];
        String maxAge = queryString.get(MAX_AGE)[0];
        String travellerType = queryString.get(TRAVELLER_TYPE)[0];
        LocalDate minDate = LocalDate.of(1000, 1, 1);
        LocalDate maxDate = LocalDate.of(3000, 12, 30);

        if (gender.length() != 0) {
            profileExpressionList.eq(GENDER, gender);
        }
        if ((maxAge.length() != 0)) {
            minDate = LocalDate.now().minusYears(Integer.parseInt(maxAge) + AGE_SEARCH_OFFSET);
        }
        if ((minAge.length() != 0)) {
            maxDate = LocalDate.now().minusYears(Integer.parseInt(minAge));
        }
        profileExpressionList.between(DATE_OF_BIRTH, minDate, maxDate);

        if (nationality.length() != 0) {
            profileExpressionList.eq(NATIONALITY_FIELD, nationality);
        }
        if (travellerType.length() != 0) {
            profileExpressionList.eq(TRAVELLER_TYPE_FIELD, travellerType);
        }

        return profileExpressionList.findList();
    }

    /**
     * Makes another user (based on the Http request body) an admin if the currently logged in user is an admin.
     * If user is not logged in they are unauthorised, if they are logged in and they are not admin they are forbidden
     * to make another user an admin.
     *
     * @param request   Http Request containing Json Body.
     * @param id        the id of the user to made an admin.
     * @return          ok() (Http 200) if successfully making a user admin, unauthorized() (Http 401) if they are not
     *                  logged in, forbidden() (Http 403) if logged in user is not an admin.
     */
    public Result makeAdmin(Http.Request request, Long id) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {
                    // User is logged in
                    Profile userProfile = Profile.find.byId(Integer.valueOf(userId));
                    // If profile logged in is admin, can make another user admin.
                    if (userProfile.getIsAdmin()) {
                        Profile updateProfile = Profile.find.byId(id.intValue());
                        updateProfile.setIsAdmin(true);
                        updateProfile.update();
                    } else {
                        return forbidden();
                    }
                    return ok(UPDATED);
                })
                .orElseGet(() -> unauthorized(NOT_SIGNED_IN)); // User is not logged in
    }

    /**
     * Removes the admin property from a specified user based on the user id. This can only be done if the currently
     * logged in user is an admin and the user they are trying to change is not the global admin.
     *
     * @param request   Http Request containing Json Body.
     * @param id        the id of the user to be removed as an admin.
     * @return          forbidden() (Http 403) if logged in user is not an admin or the profile trying to be changed is
     *                  the global admin, unauthorized() (Http 401) if the user is not logged in, ok() (Http 200) if
     *                  successfully updating the admin property of a profile.
     */
    public Result removeAdmin(Http.Request request, Long id) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {
                    // User is logged in
                    Profile userProfile = Profile.find.byId(Integer.valueOf(userId));
                    // If the logged in user is admin
                    if (userProfile.getIsAdmin()) {
                        Profile updateProfile = Profile.find.byId(id.intValue());
                        // If the profile trying to be changed is not the global admin (id number one).
                        if (!updateProfile.getId().equals(DEFAULT_ADMIN_ID)) {
                            if (updateProfile.getIsAdmin()) {
                                updateProfile.setIsAdmin(false);
                                updateProfile.update();
                            } else {
                                return badRequest();
                            }
                        } else {
                            return forbidden();
                        }
                    } else {
                        return forbidden();
                    }
                    return ok(UPDATED);
                })
                .orElseGet(() -> unauthorized(NOT_SIGNED_IN)); // User is not logged in
    }
}
