package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import org.apache.http.auth.AUTH;
import org.junit.Assert;
import org.springframework.beans.BeansException;
import play.Application;
import play.db.Database;
import play.db.evolutions.Evolutions;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;

public class ProfileTestSteps {

    @Inject
    Application application;
    protected Database database;

    private static final String AUTHORIZED = "authorized";
    private int statusCode;
    private int loginStatusCode;
    private static final String PROFILES_URI = "/v1/profiles";
    private static final String PROFILES_UPDATE__URI = "/v1/profile/";
    private static final String TRAVTYPES_URI = "/v1/travtypes";
    private static final String NATIONALITIES_URI = "/v1/nationalities";
    private static final String LOGIN_URI = "/v1/login";
    private static final String LOGOUT_URI = "/v1/logout";

    /**
     * A valid username for login credentials for admin user.
     */
    private static final String VALID_USERNAME = "admin@travelea.com";


    /**
     * A valid password for login credentials for admin user.
     */
    private static final String VALID_AUTHPASS = "admin1";

    /**
     * The session used for keeping logged in users
     */
    private Map<String, String> session;

    private static final int NUMBER_OF_TRAVELTYPES = 7;
    private static final int NUMBER_OF_NATIONALITIES = 108;
    private static final int NUMBER_OF_PROFILES = 2;

    private static final Logger LOGGER = Logger.getLogger( ProfileTestSteps.class.getName() );




    @Before
    public void setUp() {
        Map<String, String> configuration = new HashMap<>();
        configuration.put("play.db.config", "db");
        configuration.put("play.db.default", "default");
        configuration.put("db.default.driver", "org.h2.Driver");
        configuration.put("db.default.url", "jdbc:h2:mem:testDBProfile;MODE=MYSQL;");
        configuration.put("ebean.default", "models.*");
        configuration.put("play.evolutions.db.default.enabled", "true");
        configuration.put("play.evolutions.autoApply", "false");

        //Set up the fake application to use the in memory database config
        application = fakeApplication(configuration);

        database = application.injector().instanceOf(Database.class);
        applyEvolutions();

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
     * Sends a logout request.
     * Cleans up the database by cleaning up evolutions and shutting it down.
     * Stops running the fake application.
     */
    @After
    public void tearDown() {
        logoutRequest();
        cleanEvolutions();
        database.shutdown();
        Helpers.stop(application);
    }

    /**
     * Sends a fake request to the application to logout.
     */
    private void logoutRequest() {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .uri(LOGOUT_URI);
        route(application, request);
    }

    /**
     * Sends a fake request to the application to login.
     * @param username      The string of the username to complete the login with.
     * @param password      The string of the password to complete the login with.
     */
    private void loginRequest(String username, String password) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.createObjectNode();

        ((ObjectNode) json).put("username", username);
        ((ObjectNode) json).put("password", password);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(LOGIN_URI);
        Result loginResult = route(application, request);
        session = request.session();
        statusCode = loginResult.status();
    }


    /**
     * Applies up evolutions to the database from the test/evolutions/default directory.
     *
     * This populates the database with necessary tables and values.
     */
    private void cleanEvolutions() {
        Evolutions.cleanupEvolutions(database);
    }


    @Given("the application is running")
    public void theApplicationIsRunning() throws BeansException {
        Assert.assertTrue(application.isTest());
    }

    /**
     * Attempts to send a log in request with user credentials from constants VALID_USERNAME
     * and VALID_AUTHPASS.
     *
     * Asserts the login was successful with a status code of OK (200).
     */
    @And("I have logged in")
    public void iAmLoggedIn() {
        loginRequest(VALID_USERNAME, VALID_AUTHPASS);
        assertEquals(OK, statusCode);
    }


    @When("I send a GET request to the profiles endpoint")
    public void iSendAGETRequestToTheProfilesEndpoint() throws BeansException {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, "1")
                .uri(PROFILES_URI);
        Result result = route(application, request);
        statusCode = result.status();

        Iterator<JsonNode> iterator = getTheResponseIterator(Helpers.contentAsString(result));

        // Checks the response for admin profile and length of 2 users
        boolean passProfiles = false;
        int count = 0;
        while (iterator.hasNext()) {
            JsonNode jsonProfile = iterator.next();
            count++;
            if (jsonProfile.get("id").asText().equals("1")) {
                if (jsonProfile.get("username").asText().equals("admin@travelea.com")) {
                    passProfiles = true;
                }
            }
        }
        if (count != NUMBER_OF_PROFILES) {
            passProfiles = false;
        }
        statusCode = result.status();
        Assert.assertEquals(true, passProfiles);

    }


    @Then("the status code is OK")
    public void theReceivedStatusCodeIs() throws BeansException{
        Assert.assertEquals(OK, statusCode);
    }


    @When("I send a GET request to the \\/travtypes endpoint")
    public void iSendAGETRequestToTheTravtypesEndpoint() throws BeansException {
        // Does the request to back end
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(TRAVTYPES_URI);
        Result result = route(application, request);

        // Gets the response
        Iterator<JsonNode> iterator = getTheResponseIterator(Helpers.contentAsString(result));

        // Checks the response for Holidaymaker and length of 7 traveller types
        boolean passTraveltypes = false;
        int count = 0;
        while (iterator.hasNext()) {
            JsonNode jsonTravellerType = iterator.next();
            count++;
            if (jsonTravellerType.get("id").asText().equals("5")) {
                if (jsonTravellerType.get("travellerType").asText().equals("Holidaymaker")) {
                    passTraveltypes = true;
                }
            }
        }
        if (count != NUMBER_OF_TRAVELTYPES) {
            passTraveltypes = false;
        }
        statusCode = result.status();
        Assert.assertEquals(true, passTraveltypes);
    }

    @When("I send a GET request to the \\/nationalities endpoint")
    public void iSendAGETRequestToTheNationalitiesEndpoint() {
        // Does the fake request to back end
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(NATIONALITIES_URI);
        Result result = route(application, request);

        // Gets the response
        Iterator<JsonNode> iterator = getTheResponseIterator(Helpers.contentAsString(result));

        // Checks the response for the nationality
        boolean passNationalities = false;
        int count = 0;
        while (iterator.hasNext()) {
            JsonNode jsonTravellerType = iterator.next();
            count++;
            if (jsonTravellerType.get("id").asText().equals("16")) {
                if (jsonTravellerType.get("nationality").asText().equals("Chinese")) {
                    passNationalities = true;
                }
            }
        }
        if (count != NUMBER_OF_NATIONALITIES) {
            passNationalities = false;
        }

        statusCode = result.status();
        Assert.assertEquals(true, passNationalities);
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

    @Given("The following profile exists with username {string} within the TravelEA database:")
    public void theFollowingProfileExistsWithUsernameWithinTheTravelEADatabase(String username) {
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
            if (jsonProfile.get("username").asText().equals(username)) {
                foundProfile = true;
            }
        }

        Assert.assertTrue(foundProfile);
    }


    @When("A user attempts to create a profile with the following fields:")
    public void aUserAttemptsToCreateAProfileWithTheFollowingFields(io.cucumber.datatable.DataTable dataTable) {
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

    @Given("The following profile does not exist with the username {string} within the TravelEA database")
    public void theFollowingProfileDoesNotExistWithTheUsernameWithinTheTravelEADatabase(String username) {
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
            if (jsonProfile.get("username").asText().equals(username)) {
                foundProfile = true;
            }
        }

        Assert.assertFalse(foundProfile);
    }

    @Then("the status code recieved is Created")
    public void theStatusCodeRecievedIsCreated() throws BeansException{
        Assert.assertEquals(CREATED, statusCode);
    }

    @Then("the status code is BadRequest")
    public void theStatusCodeIsBadRequest() throws BeansException{
        Assert.assertEquals(BAD_REQUEST, statusCode);
    }

    @When("The user attempts to update their profile information within the TravelEA database:")
    public void theUserAttemptsToUpdateTheirProfileInformationWithinTheTravelEADatabase(io.cucumber.datatable.DataTable dataTable) {
        // Creates the json for the profile
        JsonNode json = convertDataTableToJsonNode(dataTable);

        // Sending the fake request to the back end for updating
        Http.RequestBuilder request = fakeRequest()
                .method(PUT)
                .session(AUTHORIZED, "2")
                .bodyJson(json)
                .uri(PROFILES_UPDATE__URI + 2);
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

        List<String> nationality = new ArrayList<String>();
        List<String> traveller_type = new ArrayList<String>();
        List<String> passport = new ArrayList<String>();

        nationality.add(list.get(0).get("nationality"));
        traveller_type.add(list.get(0).get("traveller_type"));
        passport.add(list.get(0).get("passport_country"));

        ArrayNode nationalityNode = mapper.valueToTree(nationality);
        ArrayNode traveller_typeNode = mapper.valueToTree(traveller_type);
        ArrayNode passportNode = mapper.valueToTree(passport);

        //Add values to a JsonNode
        JsonNode json = mapper.createObjectNode();

        ((ObjectNode) json).put("username", username);
        ((ObjectNode) json).put("password", password);
        ((ObjectNode) json).put("firstName", firstName);
        ((ObjectNode) json).put("middleName", middleName);
        ((ObjectNode) json).put("lastName", lastName);
        ((ObjectNode) json).put("gender", gender);
        ((ObjectNode) json).put("dateOfBirth", dateOfBirth);
        ((ObjectNode) json).putArray("nationalities").addAll(nationalityNode);
        ((ObjectNode) json).putArray("travellerTypes").addAll(traveller_typeNode);
        ((ObjectNode) json).putArray("passports").addAll(passportNode);

        return json;
    }

}
