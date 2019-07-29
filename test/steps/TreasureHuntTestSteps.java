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
import repositories.treasureHunts.TreasureHuntRepository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.*;

public class TreasureHuntTestSteps {

    /**
     * Currently logged-in user
     */
    private String loggedInId;


    /**
     * User who owns the treasure hunt
     */
    private String targetUserId;


    /**
     * Variable to hold the status code of the result.
     */
    private int statusCode;


    /**
     * The Json body of the response.
     */
    private String responseBody;

    /**
     * Authorisation token for sessions
     */
    private static final String AUTHORIZED = "authorized";


    /**
     * The treasure hunt uri.
     */
    private static final String TREASURE_HUNT_URI = "/v1/treasureHunts";


    /**
     * Valid login credentials for an admin user.
     */
    private static final String ADMIN_ID = "1";


    /**
     * Valid login credentials for a regular user.
     */
    private static final String REG_ID = "2";


    /**
     * The login endpoint uri.
     */
    private static final String LOGIN_URI = "/v1/login";



    /**
     * Valid login credentials for an admin user.
     */
    private static final String ADMIN_USERNAME = "admin@travelea.com";
    private static final String ADMIN_AUTHPASS = "admin1";


    /**
     * Valid login credentials for a regular user.
     */
    private static final String REG_USERNAME = "guestUser@travelea.com";
    private static final String REG_AUTHPASS = "guest123";


    /**
     * Valid login credentials for an alternate user.
     */
    private static final String ALT_USERNAME = "testuser1@email.com";
    private static final String ALT_AUTHPASS = "guest123";
    private static final String ALT_ID = "3";


    /**
     * The fake application.
     */

    private Application application;


    /**
     * Database instance for the fake application.
     */
    private Database database;

    /**
     * Repository to access the destinations in the running application.
     */
    private TreasureHuntRepository treasureHuntRepository = new TreasureHuntRepository();


    private static final String DESTINATION_STRING = "Destination";
    private static final String RIDDLE_STRING = "Riddle";
    private static final String START_DATE_STRING = "Start Date";
    private static final String END_DATE_STRING = "End Date";
    private static final String OWNER_STRING = "Owner";

    private static final String DESTINATION = "destination";
    private static final String RIDDLE = "riddle";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";
    private static final String OWNER = "owner";

    private static final int START_DATE_BUFFER = -10;
    private static final int END_DATE_BUFFER = 10;



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
        configuration.put("db.default.url", "jdbc:h2:mem:testDBTreasureHunt;MODE=MYSQL;");
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

        json.put("username", username);
        json.put("password", password);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(LOGIN_URI);
        Result loginResult = route(application, request);

        statusCode = loginResult.status();
    }



    /**
     * Converts a given data table of destination values to a json node object of this destination.
     * @param dataTable     the data table containing values of a destination.
     * @return              a JsonNode of a destination containing information from the data table.
     */
    private JsonNode convertDataTableToTreasureHuntJson(io.cucumber.datatable.DataTable dataTable, int index) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String destination         = list.get(index).get(DESTINATION_STRING);
        String riddle              = list.get(index).get(RIDDLE_STRING);
        String start_date          = list.get(index).get(START_DATE_STRING);
        String end_date            = list.get(index).get(END_DATE_STRING);
        String owner               = list.get(index).get(OWNER_STRING);

        targetUserId = owner;

        if (start_date.equals("null")) {
            start_date = getTreasureHuntDateBuffer(true);
        }

        if (end_date.equals("null")) {
            end_date = getTreasureHuntDateBuffer(false);
        }
        //Add values to a JsonNode
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put(DESTINATION, destination);
        json.put(RIDDLE, riddle);
        json.put(START_DATE, start_date);
        json.put(END_DATE, end_date);
        json.put(OWNER, owner);

        return json;
    }


    /**
     * Creates a new datetime object from today's date. This is then used to ensure our tests will always pass, as a
     * buffer is used to make the start date before today and the end date after today.
     *
     * @param isStartDate   boolean value to determine if the date being changed the start or the end date.
     * @return              the start or end date, which is modified by the necessary date buffer.
     */
    private String getTreasureHuntDateBuffer(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();

        if (isStartDate) {
            calendar.add(Calendar.DATE, START_DATE_BUFFER);
        }
        calendar.add(Calendar.DATE, END_DATE_BUFFER);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:MM:ss");
        return sdf.format(calendar.getTime());
    }


    /**
     * Runs after each test scenario.
     * Sends a logout request.
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
     * Sends a request to create a destination with values from the given json node.
     * @param json      a JsonNode containing the values for a new destination object.
     */
    private void createTreasureHuntRequest(JsonNode json) {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(TREASURE_HUNT_URI + "/" + targetUserId)
                .session(AUTHORIZED, loggedInId);
        Result result = route(application, request);
        statusCode = result.status();
    }


    @Given("I have the application running")
    public void iHaveTheApplicationRunning() {
        Assert.assertTrue(application.isTest());
    }


    @Given("I am logged in as a normal user")
    public void iAmLoggedInAsANormalUser() {
        loginRequest(REG_USERNAME, REG_AUTHPASS);
        assertEquals(OK, statusCode);
        loggedInId = REG_ID;
    }


    @Given("I am logged in as an Admin user")
    public void iAmLoggedInAsAnAdminUser() {
        loggedInId = ADMIN_ID;
    }


    @Given("The user is not logged in")
    public void theUserIsNotLoggedIn() {
        assertNull(loggedInId);
    }


    @Given("a treasure hunt already exists with the following values")
    public void aTreasureHuntAlreadyExistsWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode json = convertDataTableToTreasureHuntJson(dataTable, i);
            createTreasureHuntRequest(json);
        }
    }


    @When("I request to retrieve all treasure hunts")
    public void iRequestToRetrieveAllTreasureHunts() {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, loggedInId)
                .uri(TREASURE_HUNT_URI);
        Result result = route(application, request);
        statusCode = result.status();

        responseBody = Helpers.contentAsString(result);
    }


    @When("I attempt to delete the treasure Hunt")
    public void iAttemptToDeleteTheTreasureHunt() {
        Http.RequestBuilder request = fakeRequest()
                .method(DELETE)
                .session(AUTHORIZED, loggedInId)
                .uri(TREASURE_HUNT_URI + "/");
        Result result = route(application, request);
        statusCode = result.status();
    }


    @When("I attempt to create a treasure hunt with the following values")
    public void iAttemptToCreateATreasureHuntWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        targetUserId = loggedInId;
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode json = convertDataTableToTreasureHuntJson(dataTable, i);
            createTreasureHuntRequest(json);
        }
    }


    @Then("the status code I receive is (\\d+)$")
    public void theStatusCodeIReceiveIs(int expectedStatusCode) {
        assertEquals(expectedStatusCode, statusCode);
    }


    @Then("the response contains at least one treasure hunt")
    public void theResponseContainsAtLeastOneTreasureHunt() throws IOException {
        int responseSize = new ObjectMapper().readTree(responseBody).size();
        Assert.assertTrue(responseSize > 0);
    }
}
