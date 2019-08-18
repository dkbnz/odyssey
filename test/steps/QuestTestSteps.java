package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import models.objectives.Objective;
import models.quests.Quest;
import org.junit.Assert;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import repositories.objectives.ObjectiveRepository;
import repositories.quests.QuestRepository;

import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;
import static play.mvc.Http.HttpVerbs.PUT;
import static play.test.Helpers.*;

public class QuestTestSteps {

    /**
     * Singleton class which stores generally used variables.
     */
    private TestContext testContext = TestContext.getInstance();


    /**
     * New instance of the general test steps.
     */
    private GeneralTestSteps generalTestSteps = new GeneralTestSteps();


    /**
     * User Ids for the test context.
     */
    private static final String REGULAR_USER = "2";
    private static final String NON_EXISTENT_USER = "-1";



    /**
     * All the static variables that reference columns in the cucumber DataTable for a quest.
     */
    private static final String TITLE_STRING = "Title";
    private static final String START_DATE_STRING = "Start Date";
    private static final String END_DATE_STRING = "End Date";


    /**
     * All the static variables that reference columns in the cucumber DataTable for a objective.
     */
    private static final String OBJECTIVE_DESTINATION_STRING = "Destination";
    private static final String OBJECTIVE_RIDDLE_STRING = "Riddle";
    private static final String OBJECTIVE_RADIUS_STRING = "Radius";


    /**
     * The static Json variable keys for a quest.
     */
    private static final String TITLE = "title";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String OBJECTIVES = "objectives";


    /**
     * The static Json variable keys for a objective.
     */
    private static final String ID = "id";
    private static final String OBJECTIVE_DESTINATION = "destination";
    private static final String OBJECTIVE_RIDDLE = "riddle";
    private static final String OBJECTIVE_RADIUS = "radius";


    /**
     * Authorisation token for sessions
     */
    private static final String AUTHORIZED = "authorized";


    /**
     * The quest URI endpoint.
     */
    private static final String QUEST_URI = "/v1/quests";


    /**
     * The id of the newly created quest.
     */
    private Long questId;


    private ObjectNode questObjectJson;
    private List<ObjectNode> questObjectivesJson = new ArrayList<>();


    /**
     * Repository to access the quests in the running application.
     */
    private QuestRepository questRepository = testContext.getApplication().injector().instanceOf(QuestRepository.class);


    /**
     * Repository to access the objectives in the running application.
     */
    private ObjectiveRepository objectiveRepository =
            testContext.getApplication().injector().instanceOf(ObjectiveRepository.class);


    /**
     * Converts a given data table of quest values to a Json node object of this quest.
     *
     * @param dataTable     the data table containing values of an quest.
     */
    private void convertDataTableToQuestJson(io.cucumber.datatable.DataTable dataTable, int index) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String title             = list.get(index).get(TITLE_STRING);
        String startDate         = list.get(index).get(START_DATE_STRING);
        String endDate           = list.get(index).get(END_DATE_STRING);

        if (startDate.isEmpty()) {
            startDate = generalTestSteps.getDateBuffer(true);
        }

        if (endDate.isEmpty()) {
            endDate = generalTestSteps.getDateBuffer(false);
        }

        //Add values to a JsonNode
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put(TITLE, title);
        json.put(START_DATE, startDate);
        json.put(END_DATE, endDate);
        questObjectJson = json;
    }


    /**
     * Converts a given data table of objective values to a Json node object of this quest.
     *
     * @param dataTable     the data table containing values of a objective.
     */
    private void convertDataTableToObjectiveJson(io.cucumber.datatable.DataTable dataTable, int index) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String destination       = list.get(index).get(OBJECTIVE_DESTINATION_STRING);
        String riddle            = list.get(index).get(OBJECTIVE_RIDDLE_STRING);
        String radius            = list.get(index).get(OBJECTIVE_RADIUS_STRING);

        //Add values to a JsonNode
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.with(OBJECTIVE_DESTINATION).put(ID, destination);
        json.put(OBJECTIVE_RIDDLE, riddle);
        json.put(OBJECTIVE_RADIUS, radius);
        questObjectivesJson.add(json);
    }


    /**
     * Sends a request to create a quest with values from the given Json node.
     *
     * @param json      a JsonNode containing the values for a new quest object.
     */
    private void createQuestRequest(JsonNode json) throws IOException {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(QUEST_URI + "/" + testContext.getTargetId())
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        questObjectivesJson.clear();

        if (testContext.getStatusCode() < 400) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(Helpers.contentAsString(result));
            questId = Long.parseLong(actualObj.get(ID).toString());
        }

        testContext.setResponseBody(Helpers.contentAsString(result));
    }


    /**
     * Sends a request to get all quests.
     */
    private void getAllQuestsRequest() {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(QUEST_URI)
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        testContext.setResponseBody(Helpers.contentAsString(result));
    }


    /**
     * Sends a request to get all quests for a given user.
     *
     * @param userId        the id of the user who owns the quests.
     */
    private void getQuestsRequest(String userId) {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(QUEST_URI + "/" + userId)
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        testContext.setResponseBody(Helpers.contentAsString(result));
    }


    /**
     * Sends a request to edit a quest with values from the given Json node.
     *
     * @param json      a JsonNode containing the values for a new quest object.
     */
    private void editQuestRequest(JsonNode json) {
        Http.RequestBuilder request = fakeRequest()
                .method(PUT)
                .bodyJson(json)
                .uri(QUEST_URI + "/" + questId)
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    /**
     * Sends a request to delete a quest with the given id.
     *
     * @param questId       the id of the quest to delete.
     */
    private void deleteQuestRequest(Integer questId) {
        Http.RequestBuilder request = fakeRequest()
                .method(DELETE)
                .uri(QUEST_URI + "/" + questId)
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    @Given("a quest already exists with the following values")
    public void aQuestAlreadyExistsWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(testContext.getLoggedInId());
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            convertDataTableToQuestJson(dataTable, i);
        }
    }


    @Given("the quest has the following objective")
    public void theQuestHasTheFollowingObjective(io.cucumber.datatable.DataTable dataTable) {
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            convertDataTableToObjectiveJson(dataTable, i);
            questObjectJson.putArray(OBJECTIVES).addAll(questObjectivesJson);
        }
    }


    @When("I attempt to edit the quest with the following values")
    public void iAttemptToEditTheQuestWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            convertDataTableToQuestJson(dataTable, i);
        }
    }


    @When("I start to create a quest using the following values")
    public void iStartToCreateAQuestUsingTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(testContext.getLoggedInId());
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            convertDataTableToQuestJson(dataTable, i);
        }
    }


    @When("I start to create a quest for a regular user using the following values")
    public void iStartToCreateAQuestForARegularUserUsingTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(REGULAR_USER);
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            convertDataTableToQuestJson(dataTable, i);
        }
    }


    @When("I start to create a quest for a non-existent user using the following values")
    public void iStartToCreateAQuestForANonExistentUserUsingTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(NON_EXISTENT_USER);
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            convertDataTableToQuestJson(dataTable, i);
        }
    }


    @When("I create the quest")
    public void iCreateTheQuest() {
        try {
            createQuestRequest(questObjectJson);
        } catch (IOException e) {
            fail();
        }
    }


    @When("I edit the quest")
    public void iEditTheQuest() {
        editQuestRequest(questObjectJson);
    }


    @When("I attempt to retrieve all quests")
    public void iAttemptToRetrieveAllQuests() {
        getAllQuestsRequest();
    }


    @When("I attempt to retrieve my quests")
    public void iAttemptToRetrieveMyQuests() {
        getQuestsRequest(testContext.getLoggedInId());
    }


    @When("^I attempt to retrieve quests for user (\\d+)$")
    public void iAttemptToRetrieveQuestsForUser(Integer userId) {
        getQuestsRequest(userId.toString());
    }


    @When("^I delete a quest with id (\\d+)$")
    public void iDeleteAQuestWithId(Integer questId) {
        deleteQuestRequest(questId);
    }


    @Then("^the response contains (\\d+) quests$")
    public void theResponseContainsQuests(int numberOfQuests) throws IOException {
        int responseSize = new ObjectMapper().readTree(testContext.getResponseBody()).size();
        Assert.assertEquals(numberOfQuests, responseSize);
    }


    @Then("^the quest with id (\\d+) no longer exists$")
    public void theQuestWithIdNoLongerExists(Integer questId) {
        Quest quest = questRepository.findById(questId.longValue());
        Assert.assertNull(quest);
    }


    @Then("^the objective with id (\\d+) still exists$")
    public void theObjectiveWithIdStillExists(Integer objectiveId) {
        Objective objective = objectiveRepository.findById(objectiveId.longValue());
        Assert.assertNotNull(objective);
    }
}
