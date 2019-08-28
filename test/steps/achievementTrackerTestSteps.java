package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import repositories.points.AchievementTrackerRepository;
import repositories.quests.QuestRepository;

import java.io.IOException;

import static play.test.Helpers.*;

public class achievementTrackerTestSteps {

    /**
     * Singleton class which stores generally used variables
     */
    private TestContext testContext = TestContext.getInstance();


    /**
     * The achievement tracker URI endpoint.
     */
    private static final String ACHIEVEMENT_TRACKER_URI = "/v1/achievementTracker/";


    /**
     * The points URI endpoint
     */
    private static final String POINTS_URI = "/points";


    /**
     * The quest URI endpoint.
     */
    private static final String QUEST_URI = "/v1/quests";


    /**
     * Authorisation token for sessions
     */
    private static final String AUTHORIZED = "authorized";


    /**
     * The quest attempt URI endpoint.
     */
    private static final String QUEST_ATTEMPT_URI = "/attempt/";


    /**
     * The quest attempt guess URI endpoint.
     */
    private static final String GUESS_URI = "/guess/";


    /**
     * The quest attempt check in URI endpoint.
     */
    private static final String CHECK_IN_URI = "/checkIn";


    /**
     * Boolean to evaluate against the response body of a riddle guess.
     */
    private static final boolean SUCCESSFUL_GUESS = true;


    private static final long QUEST_ATTEMPT_ID = 4;
    private static final long DESTINATION_TO_GUESS = 1834;



    /**
     * And object mapper used during tests.
     */
    private ObjectMapper mapper = new ObjectMapper();

    /**
     *
     */

    /**
     * Points the profile started with.
     */
    private int startingPoints;


    /**
     * Points the profile has after an action.
     */
    private int currentPoints;


    /**
     * Repository to access the profiles' achievement trackers during runtime.
     */
    private AchievementTrackerRepository achievementTrackerRepository =
            testContext.getApplication().injector().instanceOf(AchievementTrackerRepository.class);

    /**
     * Repository to access quests during test runtime.
     */
    private QuestRepository questRepository =
            testContext.getApplication().injector().instanceOf(QuestRepository.class);


    @Given("I have some starting points")
    public void iHaveSomeStartingPoints() throws IOException {
        String userToView = testContext.getLoggedInId();
        getPointsRequest(userToView);
        JsonNode responseBody = mapper.readTree(testContext.getResponseBody());
        startingPoints = responseBody.get("userPoints").asInt();
    }



    @When("I solve the current riddle for a Quest")
    public void iSolveTheFirstRiddleOfTheQuestWithID() throws IOException {
        sendRiddleGuessRequest(QUEST_ATTEMPT_ID, DESTINATION_TO_GUESS);
        JsonNode responseBody = mapper.readTree(testContext.getResponseBody());
        Assert.assertEquals(SUCCESSFUL_GUESS, responseBody.get("guessResult").asBoolean());
    }

    @Then("I have gained points.")
    public void iHaveGainedPoints() throws IOException {
        String userToView = testContext.getLoggedInId();
        getPointsRequest(userToView);

        JsonNode responseBody = mapper.readTree(testContext.getResponseBody());
        currentPoints = responseBody.get("userPoints").asInt();
        Assert.assertTrue(currentPoints > startingPoints);
    }

    private void getPointsRequest(String userId) {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(ACHIEVEMENT_TRACKER_URI + userId + POINTS_URI)
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        testContext.setResponseBody(Helpers.contentAsString(result));
    }

    private void sendRiddleGuessRequest(long attemptId, long destinationId) {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .uri(QUEST_URI + QUEST_ATTEMPT_URI + attemptId + GUESS_URI + destinationId)
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        testContext.setResponseBody(Helpers.contentAsString(result));
    }

}
