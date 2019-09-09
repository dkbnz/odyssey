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
import repositories.quests.QuestRepository;

import java.io.IOException;

import static play.test.Helpers.*;

public class AchievementTrackerTestSteps {

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
    private static final boolean UNSUCCESSFUL_GUESS = false;


    private static final long TO_CHECK_IN_RIDDLE_ID = 3L;
    private static final long TO_GUESS_RIDDLE_ID = 4L;
    private static final long DESTINATION_TO_GUESS = 1834L;
    private static final long INCORRECT_DESTINATION_GUESS = 6024L;


    // -------------------------- IDs of users used for tests ---------------------------

    private static final long REG_USER_ID = 2L;
    private static final long OTHER_USER_ID = 3L;
    private static final long GLOBAL_ADMIN_ID = 1L;



    /**
     * And object mapper used during tests.
     */
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Points the profile started with.
     */
    private int startingPoints;


    /**
     * Points the profile has after an action.
     */
    private int currentPoints;


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


    private void sendCheckInRequest(long attemptId) {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .uri(QUEST_URI + QUEST_ATTEMPT_URI + attemptId + CHECK_IN_URI)
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        testContext.setResponseBody(Helpers.contentAsString(result));
    }


    @Given("I have some starting points")
    public void iHaveSomeStartingPoints() throws IOException {
        String userToView = testContext.getLoggedInId();
        getPointsRequest(userToView);
        JsonNode responseBody = mapper.readTree(testContext.getResponseBody());
        startingPoints = responseBody.get("userPoints").asInt();
    }


    @Given("I currently have no destinations created")
    public void iCurrentlyHaveNoDestinationsCreated() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }


    @Given("I currently have no trips created")
    public void iCurrentlyHaveNoTripsCreated() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }


    @Given("I currently have no quests created")
    public void iCurrentlyHaveNoQuestsCreated() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }


    @Given("^the user has (\\d+) points$")
    public void theUserHasPoints(int userPoints) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }


    @When("^I search for profiles with (\\d+) points$")
    public void iSearchForProfilesWithPoints(int searchPoints) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }


    @When("I solve the current riddle for a Quest")
    public void iSolveTheFirstRiddleOfTheQuestWithID() throws IOException {
        sendRiddleGuessRequest(TO_GUESS_RIDDLE_ID, DESTINATION_TO_GUESS);
        JsonNode responseBody = mapper.readTree(testContext.getResponseBody());
        Assert.assertEquals(SUCCESSFUL_GUESS, responseBody.get("guessResult").asBoolean());
    }


    @When("I create a new trip with the following values")
    public void iCreateANewTripWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        throw new cucumber.api.PendingException();
    }


    @When("the trip has a destination with the following values")
    public void theTripHasADestinationWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        throw new cucumber.api.PendingException();
    }


    @Then("I have gained points")
    public void iHaveGainedPoints() throws IOException {
        String userToView = testContext.getLoggedInId();
        getPointsRequest(userToView);

        JsonNode responseBody = mapper.readTree(testContext.getResponseBody());
        currentPoints = responseBody.get("userPoints").asInt();
        Assert.assertTrue("Current points is not greater than starting points",currentPoints > startingPoints);
    }


    @When("I try to view my points")
    public void iTryToViewMyPoints() {
        String userToView = testContext.getLoggedInId();
        getPointsRequest(userToView);
    }


    @Then("I am given my point total")
    public void iAmGivenMyPointTotal() throws IOException {
        // Get the userPoints value from the JSON, convert it to an int and store it under current points if not null.

        JsonNode responseBody = mapper.readTree(testContext.getResponseBody());
        Assert.assertNotNull("No userPoints JSON value", responseBody.get("userPoints"));

        currentPoints =  responseBody.get("userPoints").asInt();

        // Points should never be negative, so something has gone wrong.
        Assert.assertTrue("Points value is negative", currentPoints >= 0);

    }


    @When("I try to view another user's points value")
    public void iTryToViewAnotherUserSPointsValue() {
        String userToView = Long.toString(OTHER_USER_ID);
        getPointsRequest(userToView);
    }


    @Then("I am given their total number of points")
    public void iAmGivenTheirTotalNumberOfPoints() throws IOException {
        JsonNode responseBody = mapper.readTree(testContext.getResponseBody());
        Assert.assertNotNull("No userPoints JSON value", responseBody.get("userPoints"));

        currentPoints =  responseBody.get("userPoints").asInt();

        // Points should never be negative, so something has gone wrong.
        Assert.assertTrue("Points value is negative", currentPoints >= 0);
    }


    @When("I incorrectly guess the answer to a quest riddle")
    public void iIncorrectlyGuessTheAnswerToAQuestRiddle() throws IOException {
        sendRiddleGuessRequest(TO_GUESS_RIDDLE_ID, INCORRECT_DESTINATION_GUESS);
        JsonNode responseBody = mapper.readTree(testContext.getResponseBody());
        Assert.assertEquals(UNSUCCESSFUL_GUESS, responseBody.get("guessResult").asBoolean());
    }


    @When("I check into a destination")
    public void iCheckIntoADestination() {
        sendCheckInRequest(TO_CHECK_IN_RIDDLE_ID);

        Assert.assertEquals(200, testContext.getStatusCode());

    }


    @Then("I have not gained points")
    public void iHaveNotGainedPoints() throws IOException {
        String userToView = testContext.getLoggedInId();
        getPointsRequest(userToView);

        JsonNode responseBody = mapper.readTree(testContext.getResponseBody());
        currentPoints = responseBody.get("userPoints").asInt();
        Assert.assertEquals("Starting and end point values are not equal", currentPoints, startingPoints);
    }


    @Then("^I gain the \"(.*)\" badge with level (\\d+)$")
    public void iGainTheBadgeWithLevel(String obtainedBadge, int badgeLevel) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }


    @Then("I have completed the quest")
    public void iHaveCompletedTheQuest() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

}
