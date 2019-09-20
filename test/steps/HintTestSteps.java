package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import models.objectives.Objective;
import models.profiles.Profile;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import play.db.evolutions.Evolutions;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import repositories.objectives.ObjectiveRepository;
import repositories.profiles.ProfileRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static play.test.Helpers.*;

public class HintTestSteps {

    /**
     * Singleton class which stores generally used variables.
     */
    private TestContext testContext = TestContext.getInstance();


    /**
     * Authorisation token for sessions
     */
    private static final String AUTHORIZED = "authorized";


    /**
     * The objective uri.
     */
    private static final String OBJECTIVE_URI = "/v1/objectives/";


    /**
     * The hints uri.
     */
    private static final String HINTS_URI = "/hints";


    private static final String MESSAGE_STRING = "Message";
    private static final String MESSAGE = "message";


    /**
     * Repository to access the objectives in the running application.
     */
    private ObjectiveRepository objectiveRepository =
            testContext.getApplication().injector().instanceOf(ObjectiveRepository.class);

    /**
     * Repository to access the profiles in the running application.
     */
    private ProfileRepository profileRepository =
            testContext.getApplication().injector().instanceOf(ProfileRepository.class);


//    @Before
//    public void hintSetUp() {
//        Evolutions.applyEvolutions(
//                testContext.getDatabase(),
//                Evolutions.fromClassLoader(
//                        getClass().getClassLoader(),
//                        "test/hints/"
//                )
//        );
//    }
//
//
//    @After
//    public void hintTearDown() {
//        Evolutions.cleanupEvolutions(testContext.getDatabase());
//    }


    /**
     * Converts a given data table containing a hint into a json node object.
     *
     * @param dataTable the data table containing the hint message.
     * @param index     the position in the data table the json components are extracted from.
     * @return a JsonNode of a hint containing information extracted from the data table.
     */
    private JsonNode convertDataTableToHintJson(io.cucumber.datatable.DataTable dataTable, int index) {
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String hintMessage = list.get(index).get(MESSAGE_STRING);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put(MESSAGE, hintMessage);

        return json;
    }


    /**
     * Sends a request to create a hint from the given json node.
     *
     * @param json        a JsonNode containing the body of the request.
     * @param objectiveId the Id of the objective the hint is created for.
     */
    private void createHintRequest(JsonNode json, int objectiveId) {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(OBJECTIVE_URI + objectiveId + HINTS_URI + "/" + testContext.getTargetId())
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        testContext.setResponseBody(Helpers.contentAsString(result));
    }


    /**
     * Sends a request to retrieve all hints for an objective with the Id specified.
     *
     * @param objectiveId the Id of of the objective that is having its hints retrieved.
     */
    private void fetchAllHintsRequest(int objectiveId) {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(OBJECTIVE_URI + objectiveId + HINTS_URI);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        testContext.setResponseBody(Helpers.contentAsString(result));
    }


//    @Given("the custom hint data is added")
//    public void theCustomHintDataIsAdded() {
//        Evolutions.applyEvolutions(
//                testContext.getDatabase(),
//                Evolutions.fromClassLoader(
//                        getClass().getClassLoader(),
//                        "custom/"
//                )
//        );
//    }


    @Given("^I own the objective with id (\\d+)$")
    public void iOwnTheObjectiveWithId(Integer objectiveId) {
        Objective objective = objectiveRepository.findById(Long.valueOf(objectiveId));
        Assert.assertEquals(testContext.getLoggedInId(), objective.getOwner().getId().toString());
    }


    @Given("^I do not own the objective with id (\\d+)$")
    public void iDoNotOwnTheObjectiveWithId(Integer objectiveId) {
        Objective objective = objectiveRepository.findById(Long.valueOf(objectiveId));
        Assert.assertNotEquals(testContext.getLoggedInId(), objective.getOwner().getId().toString());
    }


    @Given("^I have solved the objective with id (\\d+)$")
    public void iHaveSolvedTheObjectiveWithId(Integer objectiveId) {
        Profile loggedInUser = profileRepository.findById(Long.parseLong(testContext.getLoggedInId()));
        Objective objective = objectiveRepository.findById(Long.valueOf(objectiveId));
        Assert.assertTrue(objectiveRepository.hasSolved(loggedInUser, objective));
    }


    @Given("^I have not solved the objective with id (\\d+)$")
    public void iHaveNotSolvedTheObjectiveWithId(Integer objectiveId) {
        Profile loggedInUser = profileRepository.findById(Long.parseLong(testContext.getLoggedInId()));
        Objective objective = objectiveRepository.findById(Long.valueOf(objectiveId));
        Assert.assertFalse(objectiveRepository.hasSolved(loggedInUser, objective));
    }


    @When("^I attempt to create a hint with the following values for the objective with id (\\d+)$")
    public void iAttemptToCreateAHintWithTheFollowingValuesForTheObjectiveWithId(Integer objectiveId, io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(testContext.getLoggedInId());
        for (int i = 0; i < dataTable.height() - 1; i++) {
            JsonNode json = convertDataTableToHintJson(dataTable, i);
            createHintRequest(json, objectiveId);
        }
    }


    @When("^I attempt to create a hint for user (\\d+) with the following values for the objective with id (\\d+)$")
    public void iAttemptToCreateAHintWithTheFollowingValuesForTheObjectiveWithId(Integer userId, Integer objectiveId, io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(userId.toString());
        for (int i = 0; i < dataTable.height() - 1; i++) {
            JsonNode json = convertDataTableToHintJson(dataTable, i);
            createHintRequest(json, objectiveId);
        }
    }


    @When("^I attempt to retrieve all hints for the objective with id (\\d+)$")
    public void iAttemptToRetrieveAllHintsForTheObjectiveWithId(Integer objectiveId) {
        fetchAllHintsRequest(objectiveId);
    }


    @Then("^The response contains (\\d+) hints$")
    public void theResponseContainsHints(Integer expectedNumberOfHints) {
        assert(expectedNumberOfHints == testContext.getResponseBody().length());
    }
}
