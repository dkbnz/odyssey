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

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static play.mvc.Results.*;



/**
 * Controller to handle the CRUD of Profiles
 */
public class ProfileController {


    private static final String USERNAME = "username";
    private static final String PASSFIELD = "password";
    private static final String FIRST_NAME = "first_name";
    private static final String MIDDLE_NAME = "middle_name";
    private static final String LAST_NAME = "last_name";
    private static final String PASSPORT = "passport_country";
    private static final String NATIONALITY = "nationality";
    private static final String GENDER = "gender";
    private static final String MIN_AGE = "min_age";
    private static final String MAX_AGE = "max_age";
    private static final String TRAVELLER_TYPE = "traveller_type";
    private static final String DATE_OF_BIRTH = "date_of_birth";
    private static final String NATIONALITY_FIELD = "nationalities.nationality";
    private static final String TRAVELLER_TYPE_FIELD = "travellerTypes.travellerType";
    private static final String AUTHORIZED = "authorized";
    private static final String notSignedIn = "You are not logged in.";

    /**
     * Creates a user based on given JSON body.
     * If username exists, returns
     * If user is created, sets session and returns created() (HTTP 201)
     */
    public Result create(Http.Request request) {

        JsonNode json = request.body().asJson();

        if (!(json.has(USERNAME)
                && json.has(PASSFIELD)
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

        newUser.setUsername(json.get(USERNAME).asText());
        newUser.setPassword(json.get(PASSFIELD).asText());
        newUser.setFirstName(json.get(FIRST_NAME).asText());
        newUser.setMiddleName(json.get(MIDDLE_NAME).asText());
        newUser.setLastName(json.get(LAST_NAME).asText());
        newUser.setGender(json.get(GENDER).asText());

        newUser.setDateOfBirth(LocalDate.parse(json.get(DATE_OF_BIRTH).asText()));
        newUser.setDateOfCreation(new Date());

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

        return created().addingToSession(request, AUTHORIZED, newUser.id.toString());
    }


    /**
     * Field validation method checking whether a username already exists in the database
     *
     * @param username the name being checked (inputted as a String)
     * @return false if the username is unique (acceptable), or true if the profile username exists (unacceptable)
     */
    private boolean profileExists(String username) {
        return Profile.find
                .query()
                .where()
                .like(USERNAME, username)
                .findOne() != null;
    }


    /**
     * Function called from the routes request and sends back a request based on the result
     *
     * @param request the json object of the form
     * @return ok when there is no username in the database, or a bad request when there already is a user in the database
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
     * Fetches a single profile from the database.
     * If the Id is specified in the JSON request, and the client is an admin, returns specified Id.
     * If the Id is not specified, but the client is logged in, returns client profile
     *
     * @param request HTTP request from client
     * @return HTTP Result of the request
     */
    public Result fetch(Http.Request request) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {
                    // User is logged in
                    Profile userProfile = Profile.find.byId(Integer.valueOf(userId));

                    return ok(views.html.dash.profile.render(userProfile));
                })
                .orElseGet(() -> unauthorized(notSignedIn)); // User is not logged in
    }


    /**
     * Deletes a currently logged in profile and invalidates their session
     * If user is admin and the id is specified in the JSON body, delete specified id.
     *
     * @param request HTTP Request containing JSON Body
     * @return HTTP Result of the request
     */
    public Result delete(Http.Request request, Long id) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {
                    // User is logged in
                    Profile userProfile = Profile.find.byId(Integer.valueOf(userId));

                    if (!id.equals(Long.valueOf(userId))) { // Current user is trying to delete another user
                        if (true) { // TODO: Implement admin rights here
                            Profile profileToDelete = Profile.find.byId(Integer.valueOf(userId));
                            profileToDelete.delete();// TODO: Handle case where admin deletes currently logged in user.
                            return ok("Delete successful");
                        } else {
                            return unauthorized("You do not have admin rights to delete other users.");
                        }
                    } else {
                        // User is deleting their own profile
                        userProfile.delete();
                        return ok("Delete successful").withNewSession();
                    }

                })
                .orElseGet(() -> unauthorized(notSignedIn)); // User is not logged in
    }


    /**
     * Takes a Http request containing a Json body and finds logged in user, then updates said user
     *
     * @param request
     * @return
     */
    public Result update(Http.Request request) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {
                    // User is logged in
                    Profile userProfile = Profile.find.byId(Integer.valueOf(userId));
                    JsonNode json = request.body().asJson();

                    if (!(json.has(USERNAME)
                            && json.has(PASSFIELD)
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

                    if (!json.get(PASSFIELD).asText().isEmpty()) { // Only update password if user has typed a new one
                        userProfile.setPassword(json.get(PASSFIELD).asText());
                    }

                    userProfile.setUsername(json.get(USERNAME).asText());
                    userProfile.setFirstName(json.get(FIRST_NAME).asText());
                    userProfile.setMiddleName(json.get(MIDDLE_NAME).asText());
                    userProfile.setLastName(json.get(LAST_NAME).asText());
                    userProfile.setDateOfBirth(LocalDate.parse(json.get(DATE_OF_BIRTH).asText()));
                    userProfile.setGender(json.get(GENDER).asText());

                    userProfile.clearNationalities();
                    userProfile.clearPassports();
                    userProfile.clearTravellerTypes();

                    // Save user profile to clear nationalities, travellerTypes and passports
                    userProfile.update();

                    Consumer<JsonNode> nationalityAction = (JsonNode node) -> {
                        Nationality newNat = Nationality.find.byId(node.asInt());
                        userProfile.addNationality(newNat);
                    };

                    json.get(NATIONALITY).forEach(nationalityAction);

                    Consumer<JsonNode> passportAction = (JsonNode node) -> {
                        Passport newPass = Passport.find.byId(node.asInt());
                        userProfile.addPassport(newPass);
                    };

                    json.get(PASSPORT).forEach(passportAction);

                    Consumer<JsonNode> travTypeAction = (JsonNode node) -> {
                        TravellerType travType = TravellerType.find.byId(node.asInt());
                        userProfile.addTravType(travType);
                    };

                    json.get(TRAVELLER_TYPE).forEach(travTypeAction);

                    userProfile.update();

                    return ok("UPDATED");

                })
                .orElseGet(() -> unauthorized(notSignedIn)); // User is not logged in
    }


    public Result edit(Http.Request request) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {
                    // User is logged in
                    Profile userProfile = Profile.find.byId(Integer.valueOf(userId));
                    List<Nationality> nationalities = Nationality.find.all();
                    List<Passport> passports = Passport.find.all();
                    List<TravellerType> travTypes = TravellerType.find.all();

                    return ok(views.html.dash.editProfile.render(userProfile, nationalities, passports, travTypes));
                })
                .orElseGet(() -> unauthorized(notSignedIn)); // User is not logged in
    }


    /**
     * Performs an ebean find query on the database to search for profiles
     * Ensures the pro //TODO: fix this?
     *
     * @return badRequest if propertyName is not valid
     * List of profiles otherwise
     */
    public Result list(Http.Request request) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {
                    ObjectMapper mapper = new ObjectMapper();
                    ArrayNode results = mapper.createArrayNode();
                    List<Profile> profiles;

                    if (request.queryString().isEmpty()) {
                        // No query string given. retrieve all profiles
                        profiles = Profile.find.all();
                    } else {
                        profiles = searchProfiles(request.queryString());
                    }

                    return ok(views.html.viewProfiles.tableProfiles.render(profiles));
                })
                .orElseGet(() -> unauthorized(notSignedIn)); // User is not logged in
    }


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
            minDate = LocalDate.now().minusYears(Integer.parseInt(maxAge));
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

}
