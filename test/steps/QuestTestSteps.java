package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import models.Profile;
import models.objectives.Objective;
import models.quests.Quest;
import models.quests.QuestAttempt;
import org.junit.Assert;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import repositories.ProfileRepository;
import repositories.objectives.ObjectiveRepository;
import repositories.quests.QuestAttemptRepository;
import repositories.quests.QuestRepository;

import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;
import static play.mvc.Http.HttpVerbs.OPTIONS;
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
    private static final String OPERATOR = "operator";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String OBJECTIVES = "objectives";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String COUNTRY = "country";


    /**
     * Fields for query to create query string
     */
    private String queryTitle;
    private String queryOperator;
    private String queryObjectives;
    private String queryFirstName;
    private String queryLastName;
    private String queryCountry;


    /**
     * String to add the equals character (=) (<) (>) to build a query string.
     */
    private static final String EQUALS = "=";
    private static final String LESS_THAN = "%3C";
    private static final String GREATER_THAN = "%3E";


    /**
     * String to add the ampersand character (&) to build a query string.
     */
    private static final String AND = "&";


    /**
     * String to add the question mark character (?) to build a query string.
     */
    private static final String QUESTION_MARK = "?";


    /**
     * String for any spaces found within retrieval queries.
     */
    private static final String SPACE = " ";


    /**
     * String required in the position of any spaces found in a query.
     */
    private static final String QUERY_SPACE_REPLACE = "%20";


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
     * The quest attempt URI endpoint.
     */
    private static final String QUEST_ATTEMPT_URI = "/attempt/";


    /**
     * The quest attempt URI endpoint.
     */
    private static final String QUEST_COMPLETE_URI = "/complete";


    /**
     * The profiles quest URI endpoint.
     */
    private static final String PROFILES_URI = "/profiles/";


    /**
     * The id of the newly created quest.
     */
    private Long questId;


    /**
     * The id of the newly created quest attempt.
     */
    private Long questAttemptId;


    private ObjectNode questObjectJson;
    private List<ObjectNode> questObjectivesJson = new ArrayList<>();


    /**
     * Repository to access the quests in the running application.
     */
    private QuestRepository questRepository = testContext.getApplication().injector().instanceOf(QuestRepository.class);


    /**
     * Repository to access the profiles in the running application.
     */
    private ProfileRepository profileRepository = testContext.getApplication().injector().instanceOf(ProfileRepository.class);


    /**
     * Repository to access the quest attempts in the running application.
     */
    private QuestAttemptRepository questAttemptRepository =
            testContext.getApplication().injector().instanceOf(QuestAttemptRepository.class);


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
     * Creates a query string for the search quest request.
     * Builds this query string with empty values except for the given search value associated
     * with the given search field.

     * @return                  The complete query string.
     */
    private String createSearchQuestQueryString() {

        StringBuilder stringBuilder = new StringBuilder()
                .append(QUESTION_MARK)

                .append(TITLE)
                .append(EQUALS)
                .append(queryTitle)

                .append(AND)
                .append(OPERATOR)
                .append(EQUALS)
                .append(queryOperator)

                .append(AND)
                .append(OBJECTIVES)
                .append(EQUALS)
                .append(queryObjectives)

                .append(AND)
                .append(FIRST_NAME)
                .append(EQUALS)
                .append(queryFirstName)

                .append(AND)
                .append(LAST_NAME)
                .append(EQUALS)
                .append(queryLastName)

                .append(AND)
                .append(COUNTRY)
                .append(EQUALS)
                .append(queryCountry);

        return stringBuilder.toString();
    }


    /**
     * Returns a string that is either empty or containing the given value.
     * Checks if the given field matches the search field. If so, returns the given value to search.
     *
     * @param searchField       the search field name as defined by the application.
     * @param givenField        the field name given to the test.
     * @param givenValue        the value to search for if the search and given fields match.
     * @return                  a string that contains the given value or an empty string.
     */
    private String getValue(String searchField, String givenField, String givenValue) {
        return searchField.equals(givenField) ? givenValue : "";
    }


    /**
     * Sends a request to get all quests.
     */
    private void getAllQuestsRequest(String query) {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(QUEST_URI + query)
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
     * Sends a request to get all quest attempts.
     */
    private void retrieveQuestAttempts() {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(QUEST_URI + PROFILES_URI + testContext.getTargetId())
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        testContext.setResponseBody(Helpers.contentAsString(result));
    }


    /**
     * Sends a request to get all completed quests for a given user.

     */
    private void retrieveCompleteQuests() {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(QUEST_URI + "/" + testContext.getTargetId() + QUEST_COMPLETE_URI)
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


    /**
     * Sends a request to start a quest with the given id.
     *
     * @param questId           the id of the quest to start.
     * @throws IOException      thrown if there is an error reading the response body following the request.
     */
    private void startQuestRequest(Integer questId) throws IOException {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .uri(QUEST_URI + "/" + questId + QUEST_ATTEMPT_URI + testContext.getTargetId())
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        testContext.setResponseBody(Helpers.contentAsString(result));

        setQuestAttemptId();
    }


    /**
     * Sets the quest attempt id attribute after sending a request.
     *
     * @throws IOException      thrown if there is an error reading the response body following the request.
     */
    private void setQuestAttemptId() throws IOException {
        if (testContext.getStatusCode() < 400) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(testContext.getResponseBody());
            questAttemptId = Long.parseLong(actualObj.get(ID).toString());
        } else {
            questAttemptId = null;
        }
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


    @Given("^a quest exists with id (\\d+)$")
    public void aQuestExistsWithId(Integer questId) {
        Assert.assertNotNull(questRepository.findById(Long.valueOf(questId)));
    }


    @Given("^a quest does not exist with id (\\d+)$")
    public void aQuestDoesNotExistWithId(Integer questId) {
        Assert.assertNull(questRepository.findById(Long.valueOf(questId)));
    }


    @Given("^an objective exists with id (\\d+)$")
    public void anObjectiveExistsWithId(Integer objectiveId) {
        Assert.assertNotNull(objectiveRepository.findById(Long.valueOf(objectiveId)));
    }


    @Given("^the quest with id (\\d+) has been completed$")
    public void theQuestWithIdHasBeenCompleted(Integer questId) {
        Profile profile = profileRepository.findById(Long.valueOf(testContext.getLoggedInId()));
        List<QuestAttempt> questAttempts= questAttemptRepository.findAllUsing(profile, Long.valueOf(questId));
        Assert.assertFalse(questAttempts.isEmpty());
        Assert.assertTrue(questAttempts.get(0).isCompleted());
    }


    @Given("^the quest with id (\\d+) has been completed by user (\\d+)$")
    public void theQuestWithIdHasBeenCompletedByUser(Integer questId, Integer userId) {
        Profile profile = profileRepository.findById(Long.valueOf(userId));
        List<QuestAttempt> questAttempts= questAttemptRepository.findAllUsing(profile, Long.valueOf(questId));
        Assert.assertFalse(questAttempts.isEmpty());
        Assert.assertTrue(questAttempts.get(0).isCompleted());
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


    @When("^I attempt to retrieve all quests with title \'(.*)\'$")
    public void iAttemptToRetrieveAllQuestsWithTitle(String title) {
        queryTitle = getValue(TITLE, TITLE, title).replace(SPACE, QUERY_SPACE_REPLACE);
        queryOperator = getValue(OPERATOR, "","");
        queryObjectives = getValue(OBJECTIVES, "","");
        queryFirstName = getValue(FIRST_NAME, "", "");
        queryLastName = getValue(LAST_NAME, "", "");
        queryCountry = getValue(COUNTRY, "", "");
        String query = createSearchQuestQueryString();
        getAllQuestsRequest(query);
    }


    @When("^I attempt to retrieve all quests with exactly (\\d+) objectives$")
    public void iAttemptToRetrieveAllQuestsWithObjectivesNumbering(Integer numberOfObjectives) {
        queryTitle = getValue(TITLE, "", "");
        queryOperator = getValue(OPERATOR, OPERATOR, EQUALS);
        queryObjectives = getValue(OBJECTIVES, OBJECTIVES, numberOfObjectives.toString());
        queryFirstName = getValue(FIRST_NAME, "", "");
        queryLastName = getValue(LAST_NAME, "", "");
        queryCountry = getValue(COUNTRY, "", "");
        String query = createSearchQuestQueryString();
        getAllQuestsRequest(query);
    }

    @When("^I attempt to retrieve all quests with less than (\\d+) objectives$")
    public void iAttemptToRetrieveAllQuestsWithLessThanObjectivesNumbering(Integer numberOfObjectives) {
        queryTitle = getValue(TITLE, "", "");
        queryOperator = getValue(OPERATOR, OPERATOR, LESS_THAN);
        queryObjectives = getValue(OBJECTIVES, OBJECTIVES, numberOfObjectives.toString());
        queryFirstName = getValue(FIRST_NAME, "", "");
        queryLastName = getValue(LAST_NAME, "", "");
        queryCountry = getValue(COUNTRY, "", "");
        String query = createSearchQuestQueryString();
        getAllQuestsRequest(query);
    }

    @When("^I attempt to retrieve all quests with greater than (\\d+) objectives$")
    public void iAttemptToRetrieveAllQuestsWithGreaterThanObjectivesNumbering(Integer numberOfObjectives) {
        queryTitle = getValue(TITLE, "", "");
        queryOperator = getValue(OPERATOR, OPERATOR, GREATER_THAN);
        queryObjectives = getValue(OBJECTIVES, OBJECTIVES, numberOfObjectives.toString());
        queryFirstName = getValue(FIRST_NAME, "", "");
        queryLastName = getValue(LAST_NAME, "", "");
        queryCountry = getValue(COUNTRY, "", "");
        String query = createSearchQuestQueryString();
        getAllQuestsRequest(query);
    }

    @When("^I attempt to retrieve all quests created by the user \'(.*)\' \'(.*)\'$")
    public void iAttemptToRetrieveAllQuestsByUser(String firstName, String lastName) {
        queryTitle = getValue(TITLE, "", "");
        queryOperator = getValue(OPERATOR, "", "");
        queryObjectives = getValue(OBJECTIVES, "", "");
        queryFirstName = getValue(FIRST_NAME, FIRST_NAME, firstName).replace(SPACE, QUERY_SPACE_REPLACE);
        queryLastName = getValue(LAST_NAME, LAST_NAME, lastName).replace(SPACE, QUERY_SPACE_REPLACE);
        queryCountry = getValue(COUNTRY, "", "");
        String query = createSearchQuestQueryString();
        getAllQuestsRequest(query);
    }

    @When("^I attempt to retrieve all quests that contain the country \'(.*)\'$")
    public void iAttemptToRetrieveAllQuestsByCountry(String country) {
        queryTitle = getValue(TITLE, "", "");
        queryOperator = getValue(OPERATOR, "", "");
        queryObjectives = getValue(OBJECTIVES, "", "");
        queryFirstName = getValue(FIRST_NAME, "", "");
        queryLastName = getValue(LAST_NAME, "", "");
        queryCountry = getValue(COUNTRY, COUNTRY, country).replace(SPACE, QUERY_SPACE_REPLACE);
        String query = createSearchQuestQueryString();
        getAllQuestsRequest(query);
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


    @When("^I start a quest with id (\\d+)$")
    public void iStartAQuestWithId(Integer questId) throws IOException {
        testContext.setTargetId(testContext.getLoggedInId());
        startQuestRequest(questId);
    }


    @When("^I start a quest with id (\\d+) for user (\\d+)$")
    public void iStartAQuestWithIdForUser(Integer questId, Integer userId) throws IOException {
        testContext.setTargetId(userId.toString());
        startQuestRequest(questId);
    }


    @When("^I retrieve all active quests for user (\\d+)$")
    public void iRetrieveAllActiveQuestsForUser(Integer userId) {
        testContext.setTargetId(userId.toString());
        retrieveQuestAttempts();
    }


    @When("I retrieve all my complete quests")
    public void iRetrieveAllMyCompleteQuests() {
        testContext.setTargetId(testContext.getLoggedInId());
        retrieveCompleteQuests();
    }


    @When("^I retrieve all complete quests for user (\\d+)$")
    public void iRetrieveAllCompleteQuestsForUser(Integer userId) {
        testContext.setTargetId(userId.toString());
        retrieveCompleteQuests();
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


    @Then("the new quest attempt exists")
    public void theNewQuestAttemptExists() {
        Assert.assertNotNull(questAttemptRepository.findById(questAttemptId));
    }


    @Then("the response has owner view")
    public void theResponseHasOwnerView() throws IOException {
        JsonNode destinationField = new ObjectMapper().readTree(testContext.getResponseBody()).get(0).get(OBJECTIVES).get(0).get(OBJECTIVE_DESTINATION);
        Assert.assertNotNull(destinationField);
    }

    @Then("the response has public view")
    public void theResponseHasPublicView() throws IOException {
        JsonNode destinationField = new ObjectMapper().readTree(testContext.getResponseBody()).get(0).get(OBJECTIVES).get(0).get(OBJECTIVE_DESTINATION);
        Assert.assertNull(destinationField);
    }
}
