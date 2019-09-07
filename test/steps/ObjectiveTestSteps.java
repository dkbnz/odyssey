package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import models.objectives.Objective;
import org.junit.Assert;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import repositories.objectives.ObjectiveRepository;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
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
    private static final String OBJECTIVE_URI = "/v1/objectives";


    private static final String DESTINATION_STRING = "Destination";
    private static final String RIDDLE_STRING = "Riddle";
    private static final String RADIUS_STRING = "Radius";
    private static final String OWNER_STRING = "Owner";

    private static final String DESTINATION = "destination";
    private static final String RIDDLE = "riddle";
    private static final String RADIUS = "radius";
    private static final String ID = "id";

    /**
     * Static variable to call the related value from the creation return JSON.
     */
    private static final String RESULT_OBJECTIVE_ID = "newObjectiveId";

    private ObjectMapper objectMapper =
            testContext.getApplication().injector().instanceOf(ObjectMapper.class);

    private ObjectiveRepository objectiveRepository =
            testContext.getApplication().injector().instanceOf(ObjectiveRepository.class);


    /**
     * Converts a given data table of destination values to a json node object of this destination.
     *
     * @param dataTable     the data table containing values of a destination.
     * @return              a JsonNode of a destination containing information from the data table.
     */
    private JsonNode convertDataTableToObjectiveJson(io.cucumber.datatable.DataTable dataTable, int index) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String destinationId           = list.get(index).get(DESTINATION_STRING);
        String riddle                  = list.get(index).get(RIDDLE_STRING);
        String radius                  = list.get(index).get(RADIUS_STRING);

        testContext.setTargetId(list.get(index).get(OWNER_STRING));


        //Add values to a JsonNode
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        ObjectNode jsonDestination = json.putObject(DESTINATION);

        if(!destinationId.equals("null")) {
            jsonDestination.put(ID,  Integer.parseInt(destinationId));
        }

        json.put(RIDDLE, riddle);
        json.put(RADIUS, radius);
        return json;
    }


    /**
     * Sends a request to create a objective with values from the given Json node.
     *
     * @param json      a JsonNode containing the values for a new objective object.
     */
    private void createObjectiveRequest(JsonNode json) {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(OBJECTIVE_URI + "/" + testContext.getTargetId())
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

        if (testContext.getStatusCode() == 200 || testContext.getStatusCode() == 201) {
            testContext.setResponseBody(Helpers.contentAsString(result));
            try {
                JsonNode returnJson = objectMapper.readTree(Helpers.contentAsString(result));
                objectiveId = returnJson.get(RESULT_OBJECTIVE_ID).asLong();
            } catch (IOException exception) {
                fail(exception.getMessage());
            }
        }
    }


    /**
     * Sends a request to edit a objective with values from the given Json node.
     *
     * @param json      a JsonNode containing the values for a edited objective object.
     */
    private void editObjectiveRequest(JsonNode json) {
        Http.RequestBuilder request = fakeRequest()
                .method(PUT)
                .bodyJson(json)
                .uri(OBJECTIVE_URI + "/" + objectiveId)
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
                .uri(OBJECTIVE_URI);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

        testContext.setResponseBody(Helpers.contentAsString(result));
    }


    @When("I attempt to delete the objective")
    public void iAttemptToDeleteTheObjective() {
        Http.RequestBuilder request = fakeRequest()
                .method(DELETE)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(OBJECTIVE_URI + "/" + objectiveId);
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

    @Then("the objective is successfully created")
    public void theObjectiveIsSuccessfullyCreated() {
        assertNotNull(objectiveRepository.findById(objectiveId));
    }
}
