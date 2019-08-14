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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;

public class TreasureHuntTestSteps {

    /**
     * Singleton class which stores generally used variables
     */
    private TestContext testContext;

    /**
     * Test file with test steps common over different scenarios
     */
    private GeneralSteps generalSteps;


    /**
     * The ID of the treasure hunt to be updated.
     */
    private Long treasureHuntId;


    /**
     * Authorisation token for sessions
     */
    private static final String AUTHORIZED = "authorized";


    /**
     * The treasure hunt uri.
     */
    private static final String TREASURE_HUNT_URI = "/v1/treasureHunts";


    private static final String DESTINATION_STRING = "Destination";
    private static final String RIDDLE_STRING = "Riddle";
    private static final String START_DATE_STRING = "Start Date";
    private static final String END_DATE_STRING = "End Date";
    private static final String OWNER_STRING = "Owner";

    private static final String DESTINATION = "destination";
    private static final String RIDDLE = "riddle";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String ID = "id";

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
        testContext = TestContext.getInstance();

        generalSteps = new GeneralSteps();
        generalSteps.setUp();
    }


    /**
     * Converts a given data table of destination values to a json node object of this destination.
     * @param dataTable     the data table containing values of a destination.
     * @return              a JsonNode of a destination containing information from the data table.
     */
    private JsonNode convertDataTableToTreasureHuntJson(io.cucumber.datatable.DataTable dataTable, int index) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String destinationId           = list.get(index).get(DESTINATION_STRING);
        String riddle                  = list.get(index).get(RIDDLE_STRING);
        String startDate               = list.get(index).get(START_DATE_STRING);
        String endDate                 = list.get(index).get(END_DATE_STRING);

        testContext.setTargetId(list.get(index).get(OWNER_STRING));

        if (startDate.equals("null")) {
            startDate = getTreasureHuntDateBuffer(true);
        }

        if (endDate.equals("null")) {
            endDate = getTreasureHuntDateBuffer(false);
        }

        //Add values to a JsonNode
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        ObjectNode jsonDestination = json.putObject(DESTINATION);

        if(!destinationId.equals("null")) {
            jsonDestination.put(ID,  Integer.parseInt(destinationId));
        }

        json.put(RIDDLE, riddle);
        json.put(START_DATE, startDate);
        json.put(END_DATE, endDate);

        return json;
    }


    /**
     * Creates a new datetime object from today's date. This is then used to ensure our tests will always pass, as a
     * buffer is used to make the start date before today and the end date after today.
     *
     * @param isStartDate   boolean value to determine if the date being changed is the start or the end date.
     * @return              the start or end date, which is modified by the necessary date buffer.
     */
    private String getTreasureHuntDateBuffer(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();

        if (isStartDate) {
            calendar.add(Calendar.DATE, START_DATE_BUFFER);
        }
        calendar.add(Calendar.DATE, END_DATE_BUFFER);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:MM:ssZ");
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
        generalSteps.tearDown();
    }


    /**
     * Sends a request to create a treasure hunt with values from the given Json node.
     * @param json      a JsonNode containing the values for a new treasure hunt object.
     */
    private void createTreasureHuntRequest(JsonNode json) {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(TREASURE_HUNT_URI + "/" + testContext.getTargetId())
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

        if (testContext.getStatusCode() == 200 || testContext.getStatusCode() == 201) {
            testContext.setResponseBody(Helpers.contentAsString(result));
            treasureHuntId = Long.valueOf(testContext.getResponseBody());
        }
    }


    /**
     * Sends a request to edit a treasure hunt with values from the given Json node.
     * @param json      a JsonNode containing the values for a edited treasure hunt object.
     */
    private void editTreasureHuntRequest(JsonNode json) {
        Http.RequestBuilder request = fakeRequest()
                .method(PUT)
                .bodyJson(json)
                .uri(TREASURE_HUNT_URI + "/" + treasureHuntId)
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
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
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(TREASURE_HUNT_URI);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

        testContext.setResponseBody(Helpers.contentAsString(result));
    }


    @When("I attempt to delete the treasure Hunt")
    public void iAttemptToDeleteTheTreasureHunt() {
        Http.RequestBuilder request = fakeRequest()
                .method(DELETE)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(TREASURE_HUNT_URI + "/" + treasureHuntId);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    @When("I attempt to create a treasure hunt with the following values")
    public void iAttemptToCreateATreasureHuntWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(testContext.getLoggedInId());
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode json = convertDataTableToTreasureHuntJson(dataTable, i);
            createTreasureHuntRequest(json);
        }
    }


    @When("I attempt to edit the treasure hunt with the following values")
    public void iAttemptToEditTheTreasureHuntWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode editValues = convertDataTableToTreasureHuntJson(dataTable, i);
            editTreasureHuntRequest(editValues);
        }
    }


    @Then("the response contains at least one treasure hunt")
    public void theResponseContainsAtLeastOneTreasureHunt() throws IOException {
        int responseSize = new ObjectMapper().readTree(testContext.getResponseBody()).size();
        Assert.assertTrue(responseSize > 0);
    }


    @Then("the response contains no treasure hunts")
    public void theResponseContainsNoTreasureHunts() throws IOException {
        int responseSize = new ObjectMapper().readTree(testContext.getResponseBody()).size();
        Assert.assertEquals(0, responseSize);
    }
}
