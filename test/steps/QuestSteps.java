package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mysql.cj.xdevapi.JsonArray;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import models.destinations.Destination;
import models.objectives.Objective;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import repositories.ProfileRepository;
import repositories.objectives.ObjectiveRepository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;
import static play.mvc.Http.HttpVerbs.PUT;
import static play.test.Helpers.POST;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

public class QuestSteps {

    /**
     * Singleton class which stores generally used variables
     */
    private TestContext testContext = TestContext.getInstance();


    /**
     * All the static variables that reference columns in the cucumber DataTable for a quest.
     */
    private static final String TITLE_STRING = "Title";
    private static final String START_DATE_STRING = "Start Date";
    private static final String END_DATE_STRING = "End Date";
    private static final String OBJECTIVES_STRING = "Objectives";


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
     * Date buffers to ensure the tests always pass.
     */
    private static final int START_DATE_BUFFER = -10;
    private static final int END_DATE_BUFFER = 10;


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
            startDate = getDateBuffer(true);
        }

        if (endDate.isEmpty()) {
            endDate = getDateBuffer(false);
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
     * Creates a new datetime object from today's date. This is then used to ensure our tests will always pass, as a
     * buffer is used to make the start date before today and the end date after today.
     *
     * @param isStartDate   boolean value to determine if the date being changed is the start or the end date.
     * @return              the start or end date, which is modified by the necessary date buffer.
     */
    private String getDateBuffer(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();

        if (isStartDate) {
            calendar.add(Calendar.DATE, START_DATE_BUFFER);
        }
        calendar.add(Calendar.DATE, END_DATE_BUFFER);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:MM:ssZ");
        return sdf.format(calendar.getTime());
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
            try {
                createQuestRequest(questObjectJson);
            } catch (IOException e) {
                fail();
            }
        }
    }

    @Given("with the following objective")
    public void questEditObjectives(io.cucumber.datatable.DataTable dataTable) {
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            convertDataTableToObjectiveJson(dataTable, i);
            questObjectJson.putArray(OBJECTIVES).addAll(questObjectivesJson);
            editQuestRequest(questObjectJson);
        }
    }


    @When("I attempt to edit the quest with the following values")
    public void iAttemptToEditTheQuestWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            convertDataTableToQuestJson(dataTable, i);
        }
    }

    @When("I attempt to create a quest using the following json")
    public void iCreateAQuestUsingTheFollowingJson(String questJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode body = mapper.readTree(questJson);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(body)
                .uri(QUEST_URI + "/" +testContext.getLoggedInId())
                .session(AUTHORIZED, testContext.getLoggedInId());

        Result result = route(testContext.getApplication(), request);

        testContext.setStatusCode(result.status());
        testContext.setResponseBody(Helpers.contentAsString(result));
    }

}
