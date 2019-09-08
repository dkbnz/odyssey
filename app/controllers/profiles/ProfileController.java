package controllers.profiles;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.ExpressionList;
import models.points.AchievementTracker;
import models.profiles.Nationality;
import models.profiles.Passport;
import models.profiles.Profile;
import models.profiles.TravellerType;
import models.util.ApiError;
import play.mvc.Http;
import play.mvc.Result;
import play.libs.Json;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import com.google.inject.Inject;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.points.AchievementTrackerRepository;
import repositories.profiles.NationalityRepository;
import repositories.profiles.PassportRepository;
import repositories.profiles.ProfileRepository;
import repositories.destinations.TravellerTypeRepository;
import util.AuthenticationUtil;

import static play.mvc.Results.*;


/**
 * Controller to handle the CRUD of Profiles
 */
public class ProfileController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String USERNAME = "username";
    private static final String PASS_FIELD = "password";
    private static final String FIRST_NAME = "firstName";
    private static final String MIDDLE_NAME = "middleName";
    private static final String LAST_NAME = "lastName";
    private static final String NAME = "name";
    private static final String PASSPORT = "passports";
    private static final String NATIONALITY = "nationalities";
    private static final String GENDER = "gender";
    private static final String MIN_AGE = "min_age";
    private static final String MAX_AGE = "max_age";
    private static final String TRAVELLER_TYPE = "travellerTypes";
    private static final String DATE_OF_BIRTH = "dateOfBirth";
    private static final String NATIONALITY_FIELD = "nationalities.nationality";
    private static final String TRAVELLER_TYPE_FIELD = "travellerTypes.travellerType";
    private static final String ACHIEVEMENT_POINTS = "achievementTracker.points";
    private static final String POINTS = "points";
    private static final String PAGE = "page";
    private static final String PAGE_SIZE = "pageSize";
    private static final String SORT_BY = "sortBy";
    private static final String SORT_ORDER = "sortOrder";
    private static final String MIN_POINTS = "min_points";
    private static final String MAX_POINTS = "max_points";
    private static final String RANK = "rank";
    private static final String ACHIEVEMENT_RANK = "achievementTracker.rank";
    private static final String AUTHORIZED = "authorized";
    private static final String NOT_SIGNED_IN = "You are not logged in.";
    private static final String NO_PROFILE_FOUND = "No profile found.";
    private static final long AGE_SEARCH_OFFSET = 1;
    private static final long DEFAULT_ADMIN_ID = 1;
    private static final String UPDATED = "UPDATED";
    private static final String ID = "id";

    private ProfileRepository profileRepository;
    private NationalityRepository nationalityRepository;
    private PassportRepository passportRepository;
    private TravellerTypeRepository travellerTypeRepository;
    private AchievementTrackerRepository achievementTrackerRepository;

    @Inject
    public ProfileController(ProfileRepository profileRepository,
                             NationalityRepository nationalityRepository,
                             PassportRepository passportRepository,
                             TravellerTypeRepository travellerTypeRepository,
                             AchievementTrackerRepository achievementTrackerRepository) {
        this.profileRepository = profileRepository;
        this.passportRepository = passportRepository;
        this.nationalityRepository = nationalityRepository;
        this.travellerTypeRepository = travellerTypeRepository;
        this.achievementTrackerRepository = achievementTrackerRepository;
    }


    /**
     * Creates a user based on given Json body. All new users are not an admin by default. This is used on the Sign Up
     * page when a user is making a new profile. All parameters are compulsory, except for passport country. When a user
     * creates a new profile, a session is made and they are automatically logged in.
     *
     * @param request       an Http Request containing Json Body.
     * @return              if username exists, returns badRequest() (Http 400), if user is created, sets session and
     * returns              created() (Http 201).
     */
    public Result create(Http.Request request) {

        Profile userProfile = AuthenticationUtil.validateAuthentication(profileRepository, request);

        // If the user is not logged in, then they are unauthorized so can create a profile.
        // If they are logged in, they must be an admin.
        if (userProfile != null && !userProfile.isAdmin()) {
            return badRequest();
        }

        JsonNode json = request.body().asJson();

        Result checkJson = validateProfileJson(json);

        if (checkJson != null) {
            return checkJson;
        }

        if (profileExists(json.get(USERNAME).asText())) {
            return badRequest("Profile Exists");
        }

        Profile newUser = new Profile();
        AchievementTracker achievementTracker = new AchievementTracker();

        // Uses the hashProfilePassword() method to hash the given password.
        try {
            newUser.setPassword(AuthenticationUtil.hashProfilePassword(json.get(PASS_FIELD).asText()));
        } catch (NoSuchAlgorithmException e) {
            log.error("Unable to hash the user password", e);
        }

        newUser.setUsername(json.get(USERNAME).asText());
        newUser.setFirstName(json.get(FIRST_NAME).asText());
        newUser.setMiddleName(json.get(MIDDLE_NAME).asText());
        newUser.setLastName(json.get(LAST_NAME).asText());
        newUser.setGender(json.get(GENDER).asText());
        newUser.setDateOfBirth(LocalDate.parse(json.get(DATE_OF_BIRTH).asText()));
        newUser.setDateOfCreation(new Date());
        newUser.setAdmin(false);
        newUser.setAchievementTracker(achievementTracker);

        profileRepository.save(newUser);

        Consumer<JsonNode> nationalityAction = (JsonNode node) -> {
            Nationality newNat = nationalityRepository.findById(node.get(ID).asLong());
            newUser.addNationality(newNat);
        };

        json.get(NATIONALITY).forEach(nationalityAction);

        Consumer<JsonNode> passportAction = (JsonNode node) -> {
            Passport newPass = passportRepository.findById(node.get(ID).asLong());
            newUser.addPassport(newPass);
        };

        json.get(PASSPORT).forEach(passportAction);

        Consumer<JsonNode> travTypeAction = (JsonNode node) -> {
            TravellerType travType = travellerTypeRepository.findById(node.get(ID).asLong());
            newUser.addTravType(travType);
        };

        json.get(TRAVELLER_TYPE).forEach(travTypeAction);

        profileRepository.save(newUser);

        return (userProfile != null && userProfile.isAdmin())
                ? created("")
                : created().addingToSession(request, AUTHORIZED, newUser.id.toString());
    }


    /**
     * Validates a new user's data when creating a profile. The validation is the same as the agreed front-end
     * validation.
     *
     * @param json      the Json content given by the new user.
     * @return          a string value of the error if there is one, otherwise returns null.
     */
    private String userDataValid(JsonNode json) {
        String username = json.get(USERNAME).asText();
        String firstName = json.get(FIRST_NAME).asText();
        String middleName = json.get(MIDDLE_NAME).asText();
        String lastName = json.get(LAST_NAME).asText();
        String gender = json.get(GENDER).asText();
        LocalDate dateOfBirth = LocalDate.parse(json.get(DATE_OF_BIRTH).asText());

        if (validateUsername(username) != null) {
            return validateUsername(username);
        }

        if (validateName(firstName, "First Name") != null) {
            return validateName(firstName, "First Name");
        }

        if (validateName(middleName, MIDDLE_NAME) != null) {
            return validateName(middleName, MIDDLE_NAME);
        }

        if (validateName(lastName, "Last Name") != null) {
            return validateName(lastName, "Last Name");
        }

        if (validateGender(gender) != null) {
            return validateGender(gender);
        }

        if (validateDateOfBirth(dateOfBirth) != null) {
            return validateDateOfBirth(dateOfBirth);
        }

        return null;
    }


    /**
     * Validates the user's username (email address). Ensures meets a specific regular expression that is used to
     * validate emails.
     *
     * @param usernameValue         the value of the new user's username (email).
     * @return                      the string of the error message if it occurs, otherwise null if valid.
     */
    private String validateUsername(String usernameValue) {
        String emailRegex = "^([a-zA-Z0-9]+(@)([a-zA-Z]+((.)[a-zA-Z]+)*))(?=.{3,15})";
        if (usernameValue.matches(emailRegex)) {
            return "Username must be valid";
        }
        return null;
    }


    /**
     * Validates each of the users name fields when creating a new user. This validation is the same as the frontend
     * validation for the application.
     *
     * @param nameValue     the specific name data (first, middle or last) to be validated.
     * @param nameType      the string of the name so an appropriate error message is returned.
     * @return              the error message that may occur if any of the credentials are invalid.
     *                      Otherwise returns null.
     */
    private String validateName(String nameValue, String nameType) {
        if (nameType.equals(MIDDLE_NAME)) {
            if (nameValue.length() > 100) {
                return "Middle Name must be less than 100 characters.";
            }
            if (nameValue.matches(".*\\d.*")) {
                return nameType + " must not contain any numbers.";
            }
            return null;
        }
        if (nameValue.length() < 1 || nameValue.length() > 100) {
            return nameType + " must be between 1 and 100 characters.";
        }
        if (nameValue.matches(".*\\d.*")) {
            return nameType + " must not contain any numbers.";
        }
        return null;
    }


    /**
     * Validates the new user's gender, the gender must be one specified in the list.
     *
     * @param genderValue       the value of the new user's gender.
     * @return                  a string saying what is invalid about the user's gender, or null if valid.
     */
    private String validateGender(String genderValue) {
        ArrayList<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");
        genders.add("Other");

        if (!genders.contains(genderValue)) {
            return genderValue + " is not a valid gender, must be Male, Female or Other";
        }
        return null;
    }


    /**
     * Validates the new user's date of birth, the date of birth must be before today.
     *
     * @param dateOfBirthValue      the value of the new user's date of birth.
     * @return                      a string saying the user's date of birth is invalid, or null if valid.
     */
    private String validateDateOfBirth(LocalDate dateOfBirthValue) {
        if (LocalDate.now().isBefore(dateOfBirthValue)) {
            return "Date of birth must be before today";
        }

        if (LocalDate.of(1900, 1, 1).isAfter(dateOfBirthValue)) {
            return "Date of birth must be after 01/01/1900";
        }


        return null;
    }


    /**
     * Field validation method checking whether a username already exists in the database. This is to ensure there are
     * no duplicate usernames (emails), as the login functionality requires a username. This is checked on profile
     * creation in the ProfileController.
     *
     * @param username          the name being checked (inputted as a String).
     * @return                  false if the username is unique (acceptable), or true if the profile username exists
     *                          (unacceptable).
     */
    private boolean profileExists(String username) {
        return profileRepository.getExpressionList()
                .like(USERNAME, username)
                .findOne() != null;
    }


    /**
     * Field validation method checking whether a username already exists in the database. This is to ensure there are
     * no duplicate usernames (emails), as the login functionality requires a username. This error is shown to the
     * user when validating their email on Sign Up.
     *
     * @param request       an Http Request containing Json Body.
     * @return              ok() (Http 200) when there is no username in the database, or a badRequest() (Http 400) when
     *                      there is already is a user with that username in the database.
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
                    Profile userProfile = profileRepository.findById(Long.valueOf(userId));

                    if (userProfile == null) {
                        return notFound(NO_PROFILE_FOUND);
                    }

                    if (!profileExists(username) || userProfile.getUsername().equals(username)) {
                        return ok(); // If they are checking their own username, return ok()
                    } else {
                        return badRequest();
                    }
                })
                .orElseGet(() -> {
                    //User is not logged in, used for sign-up.
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
     * @param request      an Http Request containing Json Body.
     * @return             if profile is successfully retrieved, returns ok() (Http 200) with the Json body of the user
     *                     profile. Otherwise returns unauthorized() (Http 401).
     */
    public Result fetch(Http.Request request) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {
                    // User is logged in
                    Profile userProfile = profileRepository.findById(Long.valueOf(userId));
                    return ok(Json.toJson(userProfile));
                })
                .orElseGet(() -> unauthorized(NOT_SIGNED_IN)); // User is not logged in
    }


    /**
     * Deletes a currently logged in profile and invalidates their session. If user is admin and the id is specified
     * in the Json body, delete specified id. Ensures the global admin (id number of one) cannot be deleted by any
     * user, admin or not.
     *
     * @param request       an Http Request containing Json Body.
     * @return              ok() (Http 200) if the profile is successfully deleted.
     *                      unauthorized() (Http 401) if not logged in.
     *                      forbidden() (Http 403) when the logged in user is not an admin, or they are trying to delete
     *                      the global admin (id 1).
     */
    public Result delete(Http.Request request, Long id) {
        if (id == DEFAULT_ADMIN_ID) {
            return forbidden("You can not delete the default administrator");
        }
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {
                    // User is logged in
                    Profile userProfile = profileRepository.findById(Long.valueOf(userId));
                    Profile profileToDelete = profileRepository.findById(id);

                    if (userProfile == null || profileToDelete == null) {
                        return notFound(NO_PROFILE_FOUND);
                    }

                    if (!id.equals(Long.valueOf(userId))) { // Current user is trying to delete another user
                        // If user is admin, they can delete other profiles
                        if (userProfile.isAdmin()) {
                            profileRepository.delete(profileToDelete);
                            return ok("Delete successful");
                        }
                        return forbidden("You do not have admin rights to delete other users.");
                    }

                    // User is deleting their own profile
                    profileRepository.delete(profileToDelete);
                    return ok("Delete successful").withNewSession();
                })
                .orElseGet(() -> unauthorized(NOT_SIGNED_IN)); // User is not logged in
    }


    /**
     * Validates the Json of a given profile. Checks if all Json fields are present.
     * Ensures there are the required number of nationalities and traveller types.
     * Checks if user data is within expected ranges.
     *
     * @param jsonToValidate        the JsonNode of the users input.
     * @return                      badRequest() (Http 400) with associated error if an error exists.
     *                              null if checks pass.
     */
    private Result validateProfileJson(JsonNode jsonToValidate) {
        if (!(jsonToValidate.has(USERNAME)
                && jsonToValidate.has(PASS_FIELD)
                && jsonToValidate.has(FIRST_NAME)
                && jsonToValidate.has(MIDDLE_NAME)
                && jsonToValidate.has(LAST_NAME)
                && jsonToValidate.has(DATE_OF_BIRTH)
                && jsonToValidate.has(GENDER)
                && jsonToValidate.has(NATIONALITY)
                && jsonToValidate.has(PASSPORT)
                && jsonToValidate.has(TRAVELLER_TYPE)
        )) {
            return badRequest("Invalid Json");
        }

        String getError = userDataValid(jsonToValidate);

        if (getError != null) {
            return badRequest(getError);
        }

        if (jsonToValidate.get(NATIONALITY).size() == 0
                || jsonToValidate.get(TRAVELLER_TYPE).size() == 0) {
            return badRequest("Invalid number of Nationalities/Traveller Types");
        }
        return null;
    }


    /**
     * Takes a Http request containing a Json body and finds a logged in user. Then uses a PUT request to update
     * the logged in user based on the Http Request body. The validation is the same as creating a new profile.
     *
     * If the Id is specified in the Json body, and the logged in user is an admin, then edit the specified Id.
     *
     * @param request       an Http Request containing Json Body.
     * @return              ok() (Http 200) if the profile is successfully updated.
     *                      unauthorized() (Http 401) if not logged in.
     */
    public Result update(Http.Request request, Long editUserId) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {
                    Profile loggedInUser = profileRepository.findById(Long.valueOf(userId));
                    Profile profileToUpdate = profileRepository.findById(editUserId);

                    if (loggedInUser == null || profileToUpdate == null) {
                        return notFound(NO_PROFILE_FOUND); // User does not exist in the system.
                    }

                    if (!AuthenticationUtil.validUser(loggedInUser, profileToUpdate)) {
                        return forbidden(); // User has specified an id which is not their own and is not admin
                    }

                    JsonNode json = request.body().asJson();

                    Result checkJson = validateProfileJson(json);

                    if (checkJson != null) {
                        return checkJson;
                    }

                    // If the username has been changed, and the changed username exists return badRequest()
                    if (!json.get(USERNAME).asText().equals(profileToUpdate.getUsername())
                            && profileExists(json.get(USERNAME).asText())) {
                        return badRequest("Username exists");
                    }

                    if (!json.get(PASS_FIELD).asText().isEmpty()) { // Only update password if user has typed a new one
                        // Uses the hashProfilePassword() method to hash the given password.
                        try {
                            profileToUpdate.setPassword(AuthenticationUtil.hashProfilePassword(json.get(PASS_FIELD).asText()));
                        } catch (NoSuchAlgorithmException e) {
                            log.error("Unable to hash the user password", e);
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
                    profileRepository.update(profileToUpdate);

                    Consumer<JsonNode> nationalityAction = (JsonNode node) -> {
                        Nationality newNationality = nationalityRepository.findById(node.get(ID).asLong());
                        profileToUpdate.addNationality(newNationality);
                    };

                    json.get(NATIONALITY).forEach(nationalityAction);

                    Consumer<JsonNode> passportAction = (JsonNode node) -> {
                        Passport newPassport = passportRepository.findById(node.get(ID).asLong());
                        profileToUpdate.addPassport(newPassport);
                    };

                    json.get(PASSPORT).forEach(passportAction);

                    Consumer<JsonNode> travTypeAction = (JsonNode node) -> {
                        TravellerType newTravellerType = travellerTypeRepository.findById(node.get(ID).asLong());
                        profileToUpdate.addTravType(newTravellerType);
                    };

                    json.get(TRAVELLER_TYPE).forEach(travTypeAction);

                    profileRepository.update(profileToUpdate);

                    return ok(UPDATED);
                })
                .orElseGet(() -> unauthorized(NOT_SIGNED_IN)); // User is not logged in
    }


    /**
     * Performs an Ebean find query on the database to search for profiles.
     * If no query is specified in the Http request, it will return a list of all profiles. If a query is specified,
     * uses the searchProfiles() method to execute a search based on the search query parameters, also includes
     * pagination. This is used on the Search Profiles page.
     *
     * @param request           an Http Request containing Json Body.
     * @return                  unauthorized() (Http 401) if the user is not logged in.
     *                          ok() (Http 200) if the search is successful.
     */
    public Result list(Http.Request request) {
        int pageNumber = 0;
        int pageSize = 100;

        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(NOT_SIGNED_IN);
        }

        List<Profile> profiles;
        ExpressionList<Profile> expressionList = profileRepository.getExpressionList();

        if (request.getQueryString(PAGE) != null && !request.getQueryString(PAGE).isEmpty()) {
            pageNumber = Integer.parseInt(request.getQueryString(PAGE));
        }

        if (request.getQueryString(PAGE_SIZE) != null && !request.getQueryString(PAGE_SIZE).isEmpty()) {
            if (request.getQueryString(PAGE_SIZE).equals("Infinity")) {
                pageSize = Integer.MAX_VALUE;
            } else {
                pageSize = Integer.parseInt(request.getQueryString(PAGE_SIZE));
            }
        }


        String getError = validQueryString(request);

        if (getError != null) {
            return badRequest(getError);
        }

        searchProfiles(expressionList, request);

        if (request.getQueryString(SORT_BY) != null && request.getQueryString(SORT_BY).length() > 0
                && Boolean.parseBoolean(request.getQueryString(SORT_ORDER))) {
            profiles = expressionList
                    .where()
                    .orderBy().asc(request.getQueryString(SORT_BY))
                    .setFirstRow(pageNumber*pageSize)
                    .setMaxRows(pageSize)
                    .findPagedList()
                    .getList();
        } else if (request.getQueryString(SORT_BY) != null && request.getQueryString(SORT_BY).length() > 0
                && !Boolean.parseBoolean(request.getQueryString(SORT_ORDER))) {
            profiles = expressionList
                    .where()
                    .orderBy().desc(request.getQueryString(SORT_BY))
                    .setFirstRow(pageNumber*pageSize)
                    .setMaxRows(pageSize)
                    .findPagedList()
                    .getList();
        } else {
            profiles = expressionList
                    .orderBy().desc(POINTS)
                    .setFirstRow(pageNumber*pageSize)
                    .setMaxRows(pageSize)
                    .findPagedList()
                    .getList();
        }

        return ok(Json.toJson(profiles));
    }


    /**
     * Validates the search query string for profiles.
     *
     * @param request          the Http request containing the query string, given by the user.
     * @return                 string message of error in query string, empty if no error present.
     */
    private String validQueryString(Http.Request request) {
        Integer minAge = 0;
        Integer maxAge = 120;
        if (request.getQueryString(MIN_AGE) == null || request.getQueryString(MAX_AGE) == null) {
            return null;
        }
        try {
            if (!request.getQueryString(MIN_AGE).isEmpty() && !request.getQueryString(MAX_AGE).isEmpty()) {
                minAge = Integer.valueOf(request.getQueryString(MIN_AGE));
                maxAge = Integer.valueOf(request.getQueryString(MAX_AGE));
            }
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

        return null;
    }


    /**
     * Function to validate a query string and return a list of profiles based on the query string.
     * If no profiles are found, return an empty list. This is used on the leader board page for searching for profiles.
     *
     * @param expressionList    the ExpressionList containing the relevant search queries.
     * @param request           the Http request containing the specified query string.
     */
    private void searchProfiles(ExpressionList<Profile> expressionList, Http.Request request) {
        LocalDate minDate = LocalDate.of(1000, 1, 1);
        LocalDate maxDate = LocalDate.of(3000, 12, 30);

        if (checkQueryFieldExists(request, NAME)) {
            // Uses the name part of the query to search for profiles by their first, middle or last names.
            expressionList.disjunction()
                    .eq(FIRST_NAME, request.getQueryString(NAME))
                    .eq(MIDDLE_NAME, request.getQueryString(NAME))
                    .eq(LAST_NAME, request.getQueryString(NAME))
            .endJunction();
        }

        if (checkQueryFieldExists(request, GENDER)) {
            expressionList.eq(GENDER, request.getQueryString(GENDER));
        }

        if (checkQueryFieldExists(request, MIN_AGE)) {
            maxDate = LocalDate.now().minusYears(Integer.parseInt(request.getQueryString(MIN_AGE)));
        }

        if (checkQueryFieldExists(request, MAX_AGE)) {
            minDate = LocalDate.now().minusYears(Integer.parseInt(request.getQueryString(MAX_AGE)) + AGE_SEARCH_OFFSET);
        }
        expressionList.between(DATE_OF_BIRTH, minDate, maxDate);

        if (checkQueryFieldExists(request, NATIONALITY)) {
            expressionList.eq(NATIONALITY_FIELD, request.getQueryString(NATIONALITY));
        }

        if (checkQueryFieldExists(request, TRAVELLER_TYPE)) {
            expressionList.eq(TRAVELLER_TYPE_FIELD, request.getQueryString(TRAVELLER_TYPE));
        }

        if (checkQueryFieldExists(request, MIN_POINTS)) {
            expressionList.ge(ACHIEVEMENT_POINTS, request.getQueryString(MIN_POINTS));
        }

        if (checkQueryFieldExists(request, MAX_POINTS)) {
            expressionList.le(ACHIEVEMENT_POINTS, request.getQueryString(MAX_POINTS));
        }

        if(request.getQueryString(RANK) != null && !request.getQueryString(RANK).isEmpty()) {
            Integer rank = Integer.parseInt(request.getQueryString(RANK)) - 1;

            int pointsFromRank = achievementTrackerRepository.getPointsFromRank(rank);
            expressionList.le(ACHIEVEMENT_POINTS, pointsFromRank);
        }
    }


    /**
     * Helper function for searching for profiles. Checks if the query string contains the given field and the field
     * is not empty.
     *
     * @param request   the Http request containing the query string.
     * @param field     the field to be checked for in the query.
     * @return          boolean true if the given field exists in the query, false otherwise.
     */
    private boolean checkQueryFieldExists(Http.Request request, String field) {
        return request.getQueryString(field) != null && !request.getQueryString(field).isEmpty();
    }


    /**
     * Retrieves the total number of profiles stored in the database. This is so the pagination can display the total
     * number of profiles in the database.
     *
     * @param request   an Http request containing the login information and authentication data.
     * @return          unauthorized() (Http 401) if the user is not logged in.
     *                  ok() (Http 200) containing the total number of profiles stored in the database.
     */
    public Result getTotalNumberOfProfiles(Http.Request request) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);

        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        ExpressionList<Profile> expressionList = profileRepository.getExpressionList();

        String getError = validQueryString(request);

        if (getError != null) {
            return badRequest(getError);
        }

        searchProfiles(expressionList, request);
        return ok(Json.toJson(expressionList.findCount()));
    }


    /**
     * Makes another user (based on the Http request body) an admin if the currently logged in user is an admin.
     * If user is not logged in they are unauthorised, if they are logged in and they are not admin they are forbidden
     * to make another user an admin.
     *
     * @param request       an Http Request containing Json Body.
     * @param id            the id of the user to made an admin.
     * @return              ok() (Http 200) if successfully making a user admin,
     *                      unauthorized() (Http 401) if they are not logged in,
     *                      forbidden() (Http 403) if logged in user is not an admin.
     */
    public Result makeAdmin(Http.Request request, Long id) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);

        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        if (!loggedInUser.isAdmin()) {
            return forbidden(ApiError.forbidden());
        }

        Profile requestedUser = profileRepository.findById(id);

        if (requestedUser == null) {
            return notFound(ApiError.notFound());
        }

        requestedUser.setAdmin(true);
        profileRepository.update(requestedUser);

        return ok(Json.toJson(requestedUser));
    }


    /**
     * Removes the admin property from a specified user based on the user id. This can only be done if the currently
     * logged in user is an admin and the user they are trying to change is not the global admin.
     *
     * @param request       an Http Request containing Json Body.
     * @param id            the id of the user to be removed as an admin.
     * @return              forbidden() (Http 403) if logged in user is not an admin or the profile trying to be changed
     *                      is the global admin,
     *                      unauthorized() (Http 401) if the user is not logged in,
     *                      ok() (Http 200) if successfully updating the admin property of a profile.
     */
    public Result removeAdmin(Http.Request request, Long id) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);

        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        if (!loggedInUser.isAdmin()) {
            return forbidden(ApiError.forbidden());
        }

        Profile requestedUser = profileRepository.findById(id);

        if (requestedUser == null) {
            return notFound(ApiError.notFound());
        }

        requestedUser.setAdmin(false);
        profileRepository.update(requestedUser);

        return ok(Json.toJson(requestedUser));
    }
}
