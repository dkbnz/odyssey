package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import play.Application;
import play.db.Database;
import play.db.evolutions.Evolutions;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;

public class AuthenticationTestSteps {

    /**
     * Variable to hold the status code of the result.
     */
    private int statusCode;

    /**
     * The login endpoint uri.
     */
    private static final String LOGIN_URI = "/v1/login";


    /**
     * The logout endpoint uri.
     */
    private static final String LOGOUT_URI = "/v1/logout";


    /**
     * A valid username for login credentials for admin user.
     */
    private static final String VALID_USERNAME = "admin@travelea.com";


    /**
     * A valid password for login credentials for admin user.
     */
    private static final String VALID_PASSWORD = "admin1";


    /**
     * The fake application.
     */

    protected Application application;


    /**
     * Database instance for the fake application.
     */
    protected Database database;

    /**
     * Runs before each test scenario.
     * Sets up a fake application for testing.
     * Applies configuration settings to use an in memory database for the fake application.
     * Starts the application.
     * Calls apply evolutions to set up the database state.
     */
    @Before
    public void setUp() {

        Map<String, String> configuration = new HashMap<>();
        configuration.put("play.db.config", "db");
        configuration.put("play.db.default", "default");
        configuration.put("db.default.driver", "org.h2.Driver");
        configuration.put("db.default.url", "jdbc:h2:mem:testDBAuth;MODE=MYSQL;");
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
     * Sends a fake request to the application to logout.
     */
    private void logoutRequest() {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .uri(LOGOUT_URI);
        route(application, request);
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
     * Converts a given data table of destination values to a json node object of this destination.
     * @param dataTable     The data table containing values of a destination.
     * @return              A JsonNode of a profile containing information from the data table.
     */
    private JsonNode convertDataTableToJsonNode(io.cucumber.datatable.DataTable dataTable) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String username = list.get(0).get("Username");
        String password = list.get(0).get("Password");

        //Add values to a JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.createObjectNode();

        ((ObjectNode) json).put(VALID_USERNAME, username);
        ((ObjectNode) json).put(VALID_PASSWORD, password);

        return json;
    }

    /**
     * Asserts the fake application is in test mode.
     */
    @Given("I am running the application")
    public void iAmRunningTheApplication() {
        Assert.assertTrue(application.isTest());
    }


    /**
     * Logs in using the default admin credentials
     */
    @When("I send a POST request to the login endpoint with the admin credentials")
    public void iSendAPOSTRequestToTheLoginEndpointWithTheAdminCredentials() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.createObjectNode();

        ((ObjectNode) json).put("username", VALID_USERNAME);
        ((ObjectNode) json).put("password", VALID_PASSWORD);

        Http.RequestBuilder request = fakeRequest()
                .bodyJson(json)
                .method(POST)
                .uri(LOGIN_URI);
        Result loginResult = route(application, request);

        statusCode = loginResult.status();
    }

    /**
     * Logs in using a login that is not stored in the database.
     * @param dataTable contains the invalid username and password.
     */
    @When("I send a POST request to the login endpoint with the following credentials")
    public void iSendAPOSTRequestToTheLoginEndpointWithTheFollowingCredentials(io.cucumber.datatable.DataTable dataTable) {
        JsonNode json = convertDataTableToJsonNode(dataTable);

        Http.RequestBuilder request = fakeRequest()
                .bodyJson(json)
                .method(POST)
                .uri(LOGIN_URI);
        Result loginResult = route(application, request);

        statusCode = loginResult.status();
    }

    /**
     * Logs out a user using the logout endpoint
     */
    @When("I send a POST request to the logout endpoint")
    public void iSendaPostRequestToLogout() {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .uri(LOGOUT_URI);
        Result loginResult = route(application, request);

        statusCode = loginResult.status();
    }


    /**
     * Checks if the status code received is OK (200).
     */
    @Then("I receive an OK status code")
    public void the_status_code_received_is_OK() {
        assertEquals(OK, statusCode);
    }

    /**
     * Checks if the status code received is BAD_REQUEST (400).
     */
    @Then("the received status code is BadRequest")
    public void theRecievedStatusCodeIsBadRequest() {
        assertEquals(BAD_REQUEST, statusCode);
    }
}
