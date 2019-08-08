package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import models.Profile;
import org.junit.Assert;
import play.Application;
import play.db.Database;
import play.db.evolutions.Evolutions;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import repositories.ProfileRepository;

import com.google.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static play.test.Helpers.*;

public class AdminTestSteps {

    @Inject
    Application application;
    private Database database;

    private static final String AUTHORIZED = "authorized";
    private int statusCode;
    private static final String PROFILES_URI = "/v1/profiles";
    private static final String PROFILES_UPDATE_URI = "/v1/profile/";
    private static final String SINGLE_PROFILE_URI = "/v1/profile";
    private static final String LOGIN_URI = "/v1/login";
    private static final String USERNAME = "username";
    private static final String PASS_FIELD = "password";


    /**
     * A valid username for login credentials for admin user.
     */
    private static final String VALID_USERNAME = "admin@travelea.com";


    /**
     * A valid password for login credentials for admin user.
     */
    private static final String VALID_AUTHPASS = "admin1";
    private static final String ADMIN_ID = "1";

    private static final Logger LOGGER = Logger.getLogger( AdminTestSteps.class.getName() );


    /**
     * A valid username for login credentials for a regular user.
     */
    private static final String REG_USER = "guestUser@travelea.com";


    /**
     * A valid password for login credentials for a regular user.
     */
    private static final String REG_PASS = "guest123";
    private static final String REG_ID = "2";

    private String userId;
    private ProfileRepository profileRepository;

    @Before
    public void setUp() {
        Map<String, String> configuration = new HashMap<>();
        configuration.put("play.db.config", "db");
        configuration.put("play.db.default", "default");
        configuration.put("db.default.driver", "org.h2.Driver");
        configuration.put("db.default.url", "jdbc:h2:mem:testDBAdmin;MODE=MYSQL;");
        configuration.put("ebean.default", "models.*");
        configuration.put("play.evolutions.db.default.enabled", "true");
        configuration.put("play.evolutions.autoApply", "false");

        //Set up the fake application to use the in memory database config
        application = fakeApplication(configuration);

        database = application.injector().instanceOf(Database.class);
        applyEvolutions();

        profileRepository = application.injector().instanceOf(ProfileRepository.class);

        Helpers.start(application);
    }

    /**
     * Applies down evolutions to the database from the test/evolutions/default directory.
     *
     * This drops tables and data from the database.
     */
    private void applyEvolutions() {
        Evolutions.applyEvolutions(
                database,
                Evolutions.fromClassLoader(
                        getClass().getClassLoader(),
                        "test/"
                )
        );
    }

    /**
     * Runs after each test scenario.
     * Cleans up the database by cleaning up evolutions and shutting it down.
     * Stops running the fake application.
     */
    @After
    public void tearDown() {
        cleanEvolutions();
        database.shutdown();
        Helpers.stop(application);
    }

    /**
     * Applies up evolutions to the database from the test/evolutions/default directory.
     *
     * This populates the database with necessary tables and values.
     */
    private void cleanEvolutions() {
        Evolutions.cleanupEvolutions(database);
    }

    /**
     * Sends a fake request to the application to login.
     * @param username      The string of the username to complete the login with.
     * @param password      The string of the password to complete the login with.
     */
    private void loginRequest(String username, String password) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put(USERNAME, username);
        json.put(PASS_FIELD, password);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(LOGIN_URI);
        Result loginResult = route(application, request);

        statusCode = loginResult.status();
    }


    /**
     * Gets the response as an iterator array Node from any fake request so that you can iterate over the response data
     * @param content the string of the result using helper content as string
     * @return an Array node iterator
     */
    private Iterator<JsonNode> getTheResponseIterator(String content) {
        JsonNode arrNode = null;
        try {
            arrNode = new ObjectMapper().readTree(content);
        } catch (IOException e) {

            LOGGER.log(Level.SEVERE, "Unable to get response iterator for fake request.", e);
        }
        return arrNode.elements();
    }


    @Given("The following profile does not exist with username {string} within the TravelEA database")
    public void theFollowingProfileDoesNotExistWithUsernameWithinTheTravelEADatabase(String username) {
        // Sends the fake request
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, "1")
                .uri(PROFILES_URI);
        Result result = route(application, request);
        statusCode = result.status();

        // Gets the response
        Iterator<JsonNode> iterator = getTheResponseIterator(Helpers.contentAsString(result));

        // Finds profile from the iterator
        boolean foundProfile = false;
        while (iterator.hasNext() && !foundProfile) {
            JsonNode jsonProfile = iterator.next();
            if (jsonProfile.get(USERNAME).asText().equals(username)) {
                foundProfile = true;
            }
        }

        Assert.assertFalse(foundProfile);

    }

    @Given("An admin is logged in")
    public void anAdminIsLoggedIn() {
        loginRequest(VALID_USERNAME, VALID_AUTHPASS);
        Assert.assertEquals(OK, statusCode);
    }

    @When("An admin attempts to create a profile with the following fields:")
    public void anAdminAttemptsToCreateAProfileWithTheFollowingFields(io.cucumber.datatable.DataTable dataTable) {
        // Creates the json for the profile
        JsonNode json = convertDataTableToJsonNode(dataTable);

        // Sending the fake request to the back end
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(PROFILES_URI);
        Result result = route(application, request);
        statusCode = result.status();

    }

    /**
     * Converts given data table information and creates a profile json for creating a profile
     * @param dataTable the data table from cucumber
     * @return the json formatted string of the profile
     */
    private JsonNode convertDataTableToJsonNode(io.cucumber.datatable.DataTable dataTable) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String username = list.get(0).get("username");
        String password = list.get(0).get("password");
        String firstName = list.get(0).get("first_name");
        String middleName = list.get(0).get("middle_name");
        String lastName = list.get(0).get("last_name");
        String gender = list.get(0).get("gender");
        String dateOfBirth = list.get(0).get("date_of_birth");

        // complex json
        ObjectMapper mapper = new ObjectMapper();

        //Add values to a ObjectNode
        ObjectNode json = mapper.createObjectNode();

        ObjectNode nationalityNode = mapper.createObjectNode();
        nationalityNode.put("id", Integer.valueOf(list.get(0).get("nationality")));

        ObjectNode travellerTypeNode = mapper.createObjectNode();
        travellerTypeNode.put("id", Integer.valueOf(list.get(0).get("traveller_type")));

        ObjectNode passportNode = mapper.createObjectNode();
        passportNode.put("id", Integer.valueOf(list.get(0).get("passport_country")));

        json.put("username", username);
        json.put("password", password);
        json.put("firstName", firstName);
        json.put("middleName", middleName);
        json.put("lastName", lastName);
        json.put("gender", gender);
        json.put("dateOfBirth", dateOfBirth);
        json.putArray("nationalities").add(nationalityNode);
        json.putArray("travellerTypes").add(travellerTypeNode);
        json.putArray("passports").add(passportNode);

        return json;
    }


    @Given("I am logged in as an admin")
    public void iAmLoggedInAsAnAdmin() {
        loginRequest(VALID_USERNAME, VALID_AUTHPASS);
        Assert.assertEquals(OK, statusCode);
        userId = ADMIN_ID;
    }

    @Given("a user exists in the database with the id {int} and username {string}")
    public void aUserExistsInTheDatabaseWithTheIdAndUsername(Integer id, String username) {
        Profile profile = profileRepository.findById(id.longValue());
        Assert.assertNotNull(profile);
        Assert.assertEquals(profile.getUsername(), username);
    }

    @Given("a user does not exist with the username {string}")
    public void aUserDoesNotExistWithTheUsername(String username) {
        Assert.assertNull(profileRepository.getExpressionList()
                .like(USERNAME, username)
                .findOne());
    }

    @When("I change the username of the user with id {int} to {string}")
    public void iChangeTheUsernameOfTheUserWithIdTo(Integer idTochange, String newUsername) {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, String.valueOf(idTochange))
                .uri(SINGLE_PROFILE_URI);
        Result result = route(application, request);
        statusCode = result.status();

        Assert.assertEquals(OK, statusCode);

        ObjectNode profileToEdit = null;

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            profileToEdit = (ObjectNode) objectMapper.readTree(Helpers.contentAsString(result));
            profileToEdit.put(USERNAME, newUsername);
            profileToEdit.put(PASS_FIELD, "");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error converting string to Json", e);
        }

        // Sending the fake request to the back end for updating
        request = fakeRequest()
                .method(PUT)
                .session(AUTHORIZED, userId)
                .bodyJson(profileToEdit)
                .uri(PROFILES_UPDATE_URI + idTochange);

        result = route(application, request);
        statusCode = result.status();
    }

    @Given("I am logged in as a regular user")
    public void iAmLoggedInAsARegularUser() {
        loginRequest(REG_USER, REG_PASS);
        Assert.assertEquals(OK, statusCode);
        userId = REG_ID;
    }

    @Then("I receive a status code of {int}")
    public void iReceiveAStatusCodeOf(int expectedStatus) {
        Assert.assertEquals(expectedStatus, statusCode);
    }

}
