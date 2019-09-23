package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import models.profiles.Profile;
import models.quests.Quest;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import org.junit.Assert;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import repositories.profiles.ProfileRepository;

import java.io.IOException;
import java.time.ZoneId;
import java.util.*;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class AchievementTrackerTestSteps {

    /**
     * Singleton class which stores generally used variables
     */
    private TestContext testContext = TestContext.getInstance();


    /**
     * New instance of the general test steps.
     */
    private GeneralTestSteps generalTestSteps = new GeneralTestSteps();


    /**
     * The achievement tracker URI endpoint.
     */
    private static final String ACHIEVEMENT_TRACKER_URI = "/v1/achievementTracker/";


    /**
     * The profiles uri.
     */
    private static final String PROFILE_URI = "/v1/profile";


    /**
     * The points URI endpoint
     */
    private static final String POINTS_URI = "/points";


    /**
     * The quest URI endpoint.
     */
    private static final String QUEST_URI = "/v1/quests";


    /**
     * Last seen URI endpoint
     */
    private static final String LAST_SEEN_URI = "/v1/achievementTracker/updateLastSeen";


    /**
     * The destination endpoint uri.
     */
    private static final String DESTINATION_URI = "/v1/destinations";


    /**
     * The trip endpoint uri.
     */
    private static final String TRIP_URI = "/v1/trips";


    /**
     * The badges endpoint uri.
     */
    private static final String BADGES_URI = "/v1/achievementTracker/badges";


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


    private static final int EMPTY_LIST_RESPONSE_SIZE = 2;
    private static final String DESTINATIONS = "destinations";
    private static final String TRIPS = "trips";
    private static final String QUESTS = "quests";


    /**
     * The static Json variable keys for a trip.
     */
    private static final String NAME = "trip_name";
    private static final String ID = "id";
    private static final String TRIP_DESTINATIONS = "trip_destinations";
    private static final String DESTINATION = "destination_id";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";


    private static final String NAME_STRING = "Name";
    private static final String DESTINATION_STRING = "Destination";
    private static final String START_DATE_STRING = "Start Date";
    private static final String END_DATE_STRING = "End Date";

    private static final String ACHIEVEMENT_TRACKER = "achievementTracker";
    private static final String BADGES = "badges";
    private static final String BADGE_NAME = "name";
    private static final String LEVEL = "level";
    private static final String PROGRESS = "progress";

    private static final String CURRENT_STREAK = "streak";
    private static final String LAST_SEEN_DATE = "lastSeenDate";
    private static final String BRONZE_TEST_USER_FIRST_NAME = "bronzeTest";
    private static final int FIRST_LEVEL_BADGE = 1;
    private static final String CLIENT_DATE_FIELD = "clientDate";
    private static final String CLIENT_DATE_OFFSET = "dateOffset";
    private static final Integer DAY_IN_MS = 86400000;
    private static final String REG_AUTH_PASS = "guest123";

    private ObjectNode tripJson;
    private List<ObjectNode> tripDestinations = new ArrayList<>();

    /**
     * Global variable for the person's badges.
     */
    private JsonNode badges;

    /**
     * Global variable for the requested badge's progress.
     */
    private int currentBadgeProgress = 0;

    /**
     * Global variable for the requested badge's level.
     */
    private int currentBadgeLevel = 0;

    /**
     * An object mapper used during tests.
     */
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Points the profile started with.
     */
    private int startingPoints;

    /**
     * Users current streak global variable
     */
    private int currentStreak;

    /**
     * Users last login date global variable
     */
    private Date lastLoginDate;

    /**
     * Points the profile has after an action.
     */
    private int currentPoints;

    /**
     * Profile repository injected
     */
    private ProfileRepository profileRepository =
            testContext.getApplication().injector().instanceOf(ProfileRepository.class);


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


    /**
     * Converts a given data table of trip values to a Json node object of this trip.
     *
     * @param dataTable     the data table containing values of a trip.
     */
    private void convertDataTableTripJson(io.cucumber.datatable.DataTable dataTable, int index) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String name       = list.get(index).get(NAME_STRING);

        //Add values to a JsonNode
        ObjectMapper mapper = new ObjectMapper();
        tripJson = mapper.createObjectNode();
        tripJson.put(NAME, name);
    }


    /**
     * Converts a given data table of trip destination values to a Json node object of this trip.
     *
     * @param dataTable     the data table containing values of a trip destination.
     */
    private void convertDataTableToObjectiveJson(io.cucumber.datatable.DataTable dataTable, int index) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String destination         = list.get(index).get(DESTINATION_STRING);
        String startDate           = list.get(index).get(START_DATE_STRING);
        String endDate             = list.get(index).get(END_DATE_STRING);

        // If there is already destinations in the trip, then we need the dates to be spaced out.
        int dateBuffer = 0;
        if (!tripDestinations.isEmpty()) {
            dateBuffer += 10;
        }

        if (startDate.isEmpty()) {
            startDate = generalTestSteps.getDateBuffer(true, dateBuffer);
        }

        if (endDate.isEmpty()) {
            endDate = generalTestSteps.getDateBuffer(false, dateBuffer);
        }


        //Add values to a JsonNode
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put(DESTINATION, destination);
        json.put(START_DATE, startDate);
        json.put(END_DATE, endDate);
        tripDestinations.add(json);
    }


    /**
     * Sends the backend request to create a trip.
     *
     * @param json  the given Json body containing a trip.
     */
    private void createTripRequest(JsonNode json) {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .bodyJson(json)
                .uri(TRIP_URI + "/" + testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        tripDestinations.clear();

        testContext.setResponseBody(Helpers.contentAsString(result));
    }


    /**
     * Calculates the requested badge progress and level for the currently logged in profile.
     *
     * @param badgeName     the requested badge.
     */
    private void getBadgeProgressAndLevelForLoggedInUser(String badgeName) {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(PROFILE_URI);
        Result result = route(testContext.getApplication(), request);
        try {
            ObjectNode profile = mapper.readTree(Helpers.contentAsString(result)).deepCopy();
            badges = profile.get(ACHIEVEMENT_TRACKER).get(BADGES);
        } catch (Exception e) {
            fail("Unable to retrieve badges");
        }

        // Iterates through each badge in the list of the person's badges.
        for (JsonNode badge: badges) {
            // If the current viewing badge is the requested badge, return it's level.
            if (badge.get(BADGE_NAME).asText().equals(badgeName)) {
                currentBadgeProgress = badge.get(PROGRESS).asInt();
                currentBadgeLevel = badge.get(LEVEL).asInt();
            }
        }
    }


    /**
     * Gets the users current streak information
     * @param userId the id for the users streak check
     */
    private void getProfileStreakInformation(String userId) {
        Profile profile = profileRepository.findById(Long.valueOf(userId));
        currentStreak = profile.getAchievementTracker().getCurrentStreak();
        lastLoginDate = profile.getLastSeenDate();
    }


    @Given("I have some starting points")
    public void iHaveSomeStartingPoints() throws IOException {
        startingPoints = profileRepository.findById(
                Long.valueOf(
                        testContext.getLoggedInId()
                )
        ).getAchievementTracker().getPoints();
        System.out.println("STARTING POINTS: " + startingPoints);
    }


    @Given("^the user with id (\\d+) has some starting points$")
    public void theUserWithIdHasSomeStartingPoints(Integer userId) {
        startingPoints = profileRepository.findById(Long.valueOf(userId)).getAchievementTracker().getPoints();
    }


    @Given("I currently have no \"(.*)\" created$")
    public void iCurrentlyHaveNoTripsCreated(String endPoint) {
        String uri = "";
        switch(endPoint) {
            case DESTINATIONS:
                uri = DESTINATION_URI;
                break;
            case TRIPS:
                uri = TRIP_URI;
                break;
            case QUESTS:
                uri = QUEST_URI;
                break;
        }

        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(uri + "/" + testContext.getLoggedInId())
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        testContext.setResponseBody(Helpers.contentAsString(result));
        Assert.assertEquals(OK, testContext.getStatusCode());
        Assert.assertEquals(EMPTY_LIST_RESPONSE_SIZE, testContext.getResponseBody().length());
    }


    @Given("^the user has (\\d+) points$")
    public void theUserHasPoints(int userPoints) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }


    @Given("^my current progress towards the \"(.*)\" badge is (\\d+)$")
    public void myCurrentProgressTowardsTheBadgeIs(String badgeName, int progress) {
        getBadgeProgressAndLevelForLoggedInUser(badgeName);
        Assert.assertEquals(progress, currentBadgeProgress);
    }


    @Given("^the user with id \"(.*)\" has a current streak of (\\d+)$")
    public void theUserWithIdHasACurrentStreakOf(String id, int currentStreakTest) {
        getProfileStreakInformation(id);
        Assert.assertEquals(currentStreakTest, currentStreak);
    }


    @Given("^the user with id \"(.*)\" current progress towards the \"(.*)\" badge is (\\d+)$")
    public void theUserWithIdCurrentProgressTowardsTheBadgeIs(String id, String badgeName, int progress) {
        testContext.setLoggedInId(id);
        getBadgeProgressAndLevelForLoggedInUser(badgeName);
        Assert.assertEquals(progress, currentBadgeProgress);
    }


    @Given("^the user with id \"(.*)\" last logged in (\\d+) day ago$")
    public void theUserWithIdLastLoggedInDayAgo(String userId, Integer days) throws ParseException {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -days);
        Date daysAgoDate = cal.getTime();

        Profile profile = profileRepository.findById(Long.valueOf(userId));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date clientDate = new SimpleDateFormat("yyyy-MM-dd").parse(format.format(daysAgoDate));

        assertNotNull(profile);

        profile.setLastSeenDate(clientDate);
        profileRepository.update(profile);
        Profile profileCheck = profileRepository.findById(Long.valueOf(userId));

        assertNotNull(profileCheck);

        Assert.assertEquals(clientDate, profileCheck.getLastSeenDate());
    }


    @When("^the user with id \"(.*)\" updates their last seen to today$")
    public void theUserWithIdUpdatesTheirLastSeenToToday(String userId) {
        ObjectNode json = mapper.createObjectNode();


        String daysAgoDateString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .format(new Date());

        json.put(CLIENT_DATE_FIELD, daysAgoDateString);
        json.put(CLIENT_DATE_OFFSET, 0);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .uri(LAST_SEEN_URI)
                .bodyJson(json)
                .session(AUTHORIZED, userId);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        testContext.setLoggedInId(userId);

        Assert.assertEquals(OK, testContext.getStatusCode());
    }


    @When("the created user now updates their last seen date")
    public void theCreatedUserNowUpdatesTheirLastSeenDate() {

        Profile foundProfile = null;

        List<Profile> profiles = profileRepository.findAll();
        for (Profile profile: profiles) {
            if (profile.getFirstName().equals(BRONZE_TEST_USER_FIRST_NAME)) {
                foundProfile = profile;
            }
        }

        if (foundProfile != null) {
            testContext.setLoggedInId(foundProfile.getId().toString());
            ObjectNode json = mapper.createObjectNode();

            String daysAgoDateString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .format(new Date());

            json.put(CLIENT_DATE_FIELD, daysAgoDateString);
            json.put(CLIENT_DATE_OFFSET, 0);

            Http.RequestBuilder request = fakeRequest()
                    .method(POST)
                    .uri(LAST_SEEN_URI)
                    .bodyJson(json)
                    .session(AUTHORIZED, testContext.getLoggedInId());
            Result result = route(testContext.getApplication(), request);
            testContext.setStatusCode(result.status());

            Assert.assertEquals(OK, testContext.getStatusCode());
        } else {
            fail("Profile not found");
        }
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
        testContext.setTargetId(testContext.getLoggedInId());
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            convertDataTableTripJson(dataTable, i);
        }
    }


    @When("the trip has a destination with the following values")
    public void theTripHasADestinationWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            convertDataTableToObjectiveJson(dataTable, i);
            tripJson.putArray(TRIP_DESTINATIONS).addAll(tripDestinations);
        }
    }


    @When("I request to retrieve all badges")
    public void iRequestToRetrieveAllBadges() {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(BADGES_URI)
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        testContext.setResponseBody(Helpers.contentAsString(result));
    }


    @When("I create the trip")
    public void ICreateTheTrip() {
        createTripRequest(tripJson);
    }


    @When("I try to view my points")
    public void iTryToViewMyPoints() {
        String userToView = testContext.getLoggedInId();
        getPointsRequest(userToView);
    }


    @When("I try to view another user's points value")
    public void iTryToViewAnotherUsersPointsValue() {
        String userToView = Long.toString(OTHER_USER_ID);
        getPointsRequest(userToView);
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

        Assert.assertEquals(OK, testContext.getStatusCode());
    }


    @Then("^the response contains quest (\\d+)$")
    public void theResponseContainsQuest(Integer questId) throws IOException {
        JsonNode responseBody = mapper.readTree(testContext.getResponseBody());
        boolean hasQuest = false;
        for (JsonNode quest : responseBody) {
            if (quest.get("id").asInt() == questId) {
                hasQuest = true;
            }
        }
        assertTrue("Quest not in list of completed quests", hasQuest);
    }


    @Then("I have gained points")
    public void iHaveGainedPoints() throws IOException {
        String userToView = testContext.getLoggedInId();
        getPointsRequest(userToView);

        JsonNode responseBody = mapper.readTree(testContext.getResponseBody());
        currentPoints = responseBody.get("userPoints").asInt();
        Assert.assertTrue("Current points is not greater than starting points",currentPoints > startingPoints);
    }


    @Then("^the user with id (\\d+) has gained points$")
    public void theUserWithIdHasGainedPoints(Integer userId) throws IOException {
        getPointsRequest(userId.toString());
        JsonNode responseBody = mapper.readTree(testContext.getResponseBody());
        currentPoints = responseBody.get("userPoints").asInt();
        System.out.println("CURRENT POINTS: " + currentPoints);
        Assert.assertTrue("Current points is not greater than starting points", currentPoints > startingPoints);
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


    @Then("I am given their total number of points")
    public void iAmGivenTheirTotalNumberOfPoints() throws IOException {
        JsonNode responseBody = mapper.readTree(testContext.getResponseBody());
        Assert.assertNotNull("No userPoints JSON value", responseBody.get("userPoints"));

        currentPoints =  responseBody.get("userPoints").asInt();

        // Points should never be negative, so something has gone wrong.
        Assert.assertTrue("Points value is negative", currentPoints >= 0);
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
        getBadgeProgressAndLevelForLoggedInUser(obtainedBadge);
        assertEquals(badgeLevel, currentBadgeLevel);

        // Need to reset after each test.
        currentBadgeLevel = 0;
        currentBadgeProgress = 0;
    }


    @Then("^the response contains (\\d+) badges$")
    public void theResponseContainsBadges(int numberOfBadges) throws Exception {
        // Write code here that turns the phrase above into concrete actions
        JsonNode responseJson = mapper.readTree(testContext.getResponseBody());

        Assert.assertEquals(numberOfBadges, responseJson.size());
    }


    @Then("^my current streak is (\\d+)$")
    public void myCurrentStreakIs(int currentStreakTest) {
        getProfileStreakInformation(testContext.getLoggedInId());
        assertEquals(currentStreakTest, currentStreak);
    }


    @Then("^my last login was (\\d+) days ago$")
    public void myLastLoginWasHoursAgo(int days) throws IOException {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(PROFILE_URI);
        Result result = route(testContext.getApplication(), request);
        ObjectNode profile = mapper.readTree(Helpers.contentAsString(result)).deepCopy();

        Date profileLastLogin = new Date(profile.get(LAST_SEEN_DATE).asLong());

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -days);
        Date daysAgoDate = cal.getTime();

        SimpleDateFormat formatNew = new SimpleDateFormat("yyyy-MM-dd");

        String daysAgoDateString = (formatNew.format(daysAgoDate));

        String profileLastLoginDate = (formatNew.format(profileLastLogin));

        Assert.assertEquals(daysAgoDateString, profileLastLoginDate);
    }


    @Then("^the current progress towards the \"(.*)\" badge is still (\\d+)$")
    public void theCurrentProgressTowardsTheBadgeIsStill(String obtainedBadge, int progressTest) {
        getBadgeProgressAndLevelForLoggedInUser(obtainedBadge);
        assertEquals(progressTest, currentBadgeProgress);
    }

}
