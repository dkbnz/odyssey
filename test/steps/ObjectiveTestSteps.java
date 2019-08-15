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
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static play.test.Helpers.*;

public class ObjectiveTestSteps {

    /**
     * Singleton class which stores generally used variables
     */
    private TestContext testContext = TestContext.getInstance();


    /**
     * The ID of the objective to be updated.
     */
    private Long objectiveId;


    /**
     * Authorisation token for sessions
     */
    private static final String AUTHORIZED = "authorized";


    /**
     * The objective uri.
     */
    private static final String TREASURE_HUNT_URI = "/v1/objectives";


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
     * Converts a given data table of destination values to a json node object of this destination.
     * @param dataTable     the data table containing values of a destination.
     * @return              a JsonNode of a destination containing information from the data table.
     */
    private JsonNode convertDataTableToObjectiveJson(io.cucumber.datatable.DataTable dataTable, int index) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String destinationId           = list.get(index).get(DESTINATION_STRING);
        String riddle                  = list.get(index).get(RIDDLE_STRING);
        String startDate               = list.get(index).get(START_DATE_STRING);
        String endDate                 = list.get(index).get(END_DATE_STRING);

        testContext.setTargetId(list.get(index).get(OWNER_STRING));

        if (startDate.equals("null")) {
            startDate = getObjectiveDateBuffer(true);
        }

        if (endDate.equals("null")) {
            endDate = getObjectiveDateBuffer(false);
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
    private String getObjectiveDateBuffer(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();

        if (isStartDate) {
            calendar.add(Calendar.DATE, START_DATE_BUFFER);
        }
        calendar.add(Calendar.DATE, END_DATE_BUFFER);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:MM:ssZ");
        return sdf.format(calendar.getTime());
    }


    /**
     * Sends a request to create a objective with values from the given Json node.
     * @param json      a JsonNode containing the values for a new objective object.
     */
    private void createObjectiveRequest(JsonNode json) {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(TREASURE_HUNT_URI + "/" + testContext.getTargetId())
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

        if (testContext.getStatusCode() == 200 || testContext.getStatusCode() == 201) {
            testContext.setResponseBody(Helpers.contentAsString(result));
            objectiveId = Long.valueOf(testContext.getResponseBody());
        }
    }


    /**
     * Sends a request to edit a objective with values from the given Json node.
     * @param json      a JsonNode containing the values for a edited objective object.
     */
    private void editObjectiveRequest(JsonNode json) {
        Http.RequestBuilder request = fakeRequest()
                .method(PUT)
                .bodyJson(json)
                .uri(TREASURE_HUNT_URI + "/" + objectiveId)
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    @Given("a objective already exists with the following values")
    public void aObjectiveAlreadyExistsWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode json = convertDataTableToObjectiveJson(dataTable, i);
            createObjectiveRequest(json);
            Assert.assertEquals(CREATED, testContext.getStatusCode());
        }
    }


    @When("I request to retrieve all objectives")
    public void iRequestToRetrieveAllObjectives() {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(TREASURE_HUNT_URI);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

        testContext.setResponseBody(Helpers.contentAsString(result));
    }


    @When("I attempt to delete the treasure Hunt")
    public void iAttemptToDeleteTheObjective() {
        Http.RequestBuilder request = fakeRequest()
                .method(DELETE)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(TREASURE_HUNT_URI + "/" + objectiveId);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    @When("I attempt to create a objective with the following values")
    public void iAttemptToCreateAObjectiveWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(testContext.getLoggedInId());
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode json = convertDataTableToObjectiveJson(dataTable, i);
            createObjectiveRequest(json);
        }
    }


    @When("I attempt to edit the objective with the following values")
    public void iAttemptToEditTheObjectiveWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode editValues = convertDataTableToObjectiveJson(dataTable, i);
            editObjectiveRequest(editValues);
        }
    }


    @Then("the response contains at least one objective")
    public void theResponseContainsAtLeastOneObjective() throws IOException {
        int responseSize = new ObjectMapper().readTree(testContext.getResponseBody()).size();
        Assert.assertTrue(responseSize > 0);
    }


    @Then("the response contains no objectives")
    public void theResponseContainsNoObjectives() throws IOException {
        int responseSize = new ObjectMapper().readTree(testContext.getResponseBody()).size();
        Assert.assertEquals(0, responseSize);
    }
}
