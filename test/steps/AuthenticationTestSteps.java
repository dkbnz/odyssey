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
     * Singleton class which stores generally used variables
     */
    private TestContext testContext;

    /**
     * Test file with test steps common over different scenarios
     */
    private GeneralSteps generalSteps;


    /**
     * The username string variable.
     */
    private static final String USERNAME = "username";

    /**
     * The password string variable.
     */
    private static final String PASS_FIELD = "password";


    /**
     * The login uri.
     */
    private static final String LOGIN_URI = "/v1/login";

    /**
     * Runs before each test scenario.
     * Sets up a fake application for testing.
     * Applies configuration settings to use an in memory database for the fake application.
     * Starts the application.
     * Calls apply evolutions to set up the database state.
     */
    @Before
    public void setUp() {
        testContext = TestContext.getInstance();

        generalSteps = new GeneralSteps();
        generalSteps.setUp();
    }


    /**
     * Runs after each test scenario.
     * Sends a logout request.
     * Cleans up the database by cleaning up evolutions and shutting it down.
     * Stops running the fake application.
     */
    @After
    public void tearDown() {
        generalSteps.tearDown();
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
        ObjectNode json = mapper.createObjectNode();

        json.put(USERNAME, username);
        json.put(PASS_FIELD, password);

        return json;
    }


    @When("I log in")
    public void iLogIn() {
        generalSteps.iAmLoggedIn();
    }


    @When("I log out")
    public void iLogOut() {
        generalSteps.iAmNotLoggedIn();
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
        Result loginResult = route(testContext.getApplication(), request);

        testContext.setStatusCode(loginResult.status());
    }
}
