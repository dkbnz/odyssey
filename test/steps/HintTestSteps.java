package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import models.objectives.Objective;
import models.profiles.Profile;
import org.junit.Assert;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import repositories.objectives.ObjectiveRepository;
import repositories.profiles.ProfileRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.fail;
import static play.test.Helpers.POST;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

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
    private static final String HINTS_URI = "/hints/";


    /**
     * New instance of the general test steps.
     */
    private GeneralTestSteps generalTestSteps = new GeneralTestSteps();


    private static final String MESSAGE_STRING = "Message";
    private static final String HINT = "hint";


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


    /**
     * Converts a given data table containing a hint into a json node object.
     *
     * @param dataTable     the data table containing the hint message.
     * @param index         the position in the data table the json components are extracted from.
     * @return              a JsonNode of a hint containing information extracted from the data table.
     */
    private JsonNode convertDataTableToHintJson(io.cucumber.datatable.DataTable dataTable, int index) {
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String hintMessage = list.get(index).get(MESSAGE_STRING);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put(HINT, hintMessage);

        return json;
    }


    /**
     * Sends a request to create a hint from the given json node.
     *
     * @param json              a JsonNode containing the body of the request.
     * @param objectiveId       the Id of the objective the hint is created for.
     */
    private void createHintRequest(JsonNode json, int objectiveId) {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(OBJECTIVE_URI + objectiveId + HINTS_URI + testContext.getTargetId())
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        testContext.setResponseBody(Helpers.contentAsString(result));
    }


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
        Collection<Objective> solvedObjectives = objectiveRepository.findAllCompletedUsing(loggedInUser);
        Objective objective = objectiveRepository.findById(Long.valueOf(objectiveId));
        Assert.assertTrue(solvedObjectives.contains(objective));
    }


    @Given("^I have not solved the objective with id (\\d+)$")
    public void iHaveNotSolvedTheObjectiveWithId(Integer objectiveId) {
        Profile loggedInUser = profileRepository.findById(Long.parseLong(testContext.getLoggedInId()));
        Collection<Objective> solvedObjectives = objectiveRepository.findAllCompletedUsing(loggedInUser);
        Objective objective = objectiveRepository.findById(Long.valueOf(objectiveId));
        Assert.assertFalse(solvedObjectives.contains(objective));
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
}
