package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import models.destinations.Destination;
import org.junit.Assert;
import play.Application;
import play.db.Database;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.HttpVerbs.PATCH;
import static play.mvc.Http.Status.CREATED;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import play.db.evolutions.Evolutions;
import repositories.TripRepository;
import repositories.destinations.DestinationRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static play.test.Helpers.fakeApplication;

public class TripTestSteps {


    /**
     * Variable to hold the status code of the result.
     */
    private int statusCode;

    private static final String AUTHORIZED = "authorized";


    /**
     * The login endpoint uri.
     */
    private static final String LOGIN_URI = "/v1/login";


    /**
     * The logout endpoint uri.
     */
    private static final String LOGOUT_URI = "/v1/logout";


    /**
     * The trips endpoint uri.
     */
    private static final String TRIPS_URI = "/v1/trips/";


    /**
     * The fake application.
     */
    private Application application;


    /**
     * Database instance for the fake application.
     */
    private Database database;


    /**
     * The id of the currently logged in user to send with the request.
     */
    private Integer loggedInUserId;


    /**
     * The username for the default admin user.
     */
    private static final String ADMIN_USERNAME = "admin@travelea.com";


    /**
     * The username for the default guest user.
     */
    private static final String GUEST_USERNAME = "guestUser@travelea.com";


    /**
     * The admin user id.
     */
    private static final Integer ADMIN_ID = 1;


    /**
     * The guest user id.
     */
    private static final Integer GUEST_ID = 2;


    /**
     * The field for the trip id.
     */
    private static final String TRIP_ID_FIELD = "id";


    /**
     * The field for the trip name.
     */
    private static final String TRIP_NAME_FIELD = "name";

    private DestinationRepository destinationRepository;
    private TripRepository tripRepository;


    @Before
    public void setUp() {

        Map<String, String> configuration = new HashMap<>();
        configuration.put("play.db.config", "db");
        configuration.put("play.db.default", "default");
        configuration.put("db.default.driver", "org.h2.Driver");
        configuration.put("db.default.url", "jdbc:h2:mem:testDBTrip;MODE=MYSQL;");
        configuration.put("ebean.default", "models.*");
        configuration.put("play.evolutions.db.default.enabled", "true");
        configuration.put("play.evolutions.autoApply", "false");

        //Set up the fake application to use the in memory database config
        application = fakeApplication(configuration);

        database = application.injector().instanceOf(Database.class);
        destinationRepository = application.injector().instanceOf(DestinationRepository.class);
        tripRepository = application.injector().instanceOf(TripRepository.class);

        applyEvolutions();

        Helpers.start(application);
    }


    /**
     * Runs after each test scenario.
     * Sends a logout request.
     * Cleans up the database by cleaning up evolutions and shutting it down.
     * Stops running the fake application.
     */
    @After
    public void tearDown() {
        logoutRequest();
        cleanEvolutions();
        database.shutdown();
        Helpers.stop(application);
    }


    /**
     * Applies down evolutions to the database from the test/evolutions/default directory.
     *
     * This drops tables and data from the database.
     */
    private void applyEvolutions() {
        Evolutions.applyEvolutions(
                database,
                Evolutions.fromClassLoader(
                        getClass().getClassLoader(),
                        "test/"
                )
        );
    }


    /**
     * Applies up evolutions to the database from the test/evolutions/default directory.
     *
     * This populates the database with necessary tables and values.
     */
    private void cleanEvolutions() {
        Evolutions.cleanupEvolutions(database);
    }


    /**
     * Sends a fake request to the application to login.
     * @param username      The string of the username to complete the login with.
     * @param password      The string of the password to complete the login with.
     */
    private void loginRequest(String username, String password) {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();


        json.put("username", username);
        json.put("password", password);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(LOGIN_URI);
        Result loginResult = route(application, request);

        statusCode = loginResult.status();
    }


    /**
     * Sends a fake request to the application to logout.
     */
    private void logoutRequest() {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .uri(LOGOUT_URI);
        route(application, request);
    }


    /**
     * Asserts the fake application is in test mode.
     */
    @Given("I have an application running")
    public void iHaveAnApplicationRunning() {
        Assert.assertTrue(application.isTest());
    }


    /**
     * Gets log in credentials from the information in the data table and sends a log in request.
     * @param dataTable     the data table containing the log in credentials
     */
    @Given("I am logged in with credentials")
    public void iAmLoggedInWithCredentials(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String username = list.get(0).get("Username");
        String pass = list.get(0).get("Password");
        loginRequest(username, pass);
    }


    /**
     * Gets log in credentials from the information in the data table and sends a log in request.
     * @param dataTable     the data table containing the log in credentials
     */
    @Given("I am logged as the following user")
    public void iAmLoggedAsTheFollowingUser(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String username = list.get(0).get("Username");
        switch (username) {
            case ADMIN_USERNAME:
                loggedInUserId = ADMIN_ID;
                break;
            case GUEST_USERNAME:
                loggedInUserId = GUEST_ID;
                break;
            default:
                throw new Error();
        }
    }


    /**
     * Sends a request to create a trip with the given trip data.
     * @param docString     The string containing the trip data.
     * @throws IOException  If the docString is formatted incorrectly.
     */
    @When("the following json containing a trip is sent:")
    public void theFollowingJsonContainingATripIsSent(String docString) throws IOException {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .session(AUTHORIZED, "1")
                .bodyJson(convertTripStringToJson(docString))
                .uri(TRIPS_URI + 1);
        Result result = route(application, request);
        statusCode = result.status();
    }


    /**
     * Checks if the status code received is BadRequest (400).
     */
    @Then("the response status code is BadRequest")
    public void theResponseStatusCodeIsBadRequest() { assertEquals(BAD_REQUEST, statusCode); }


    private JsonNode convertTripStringToJson(String docString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(docString);
    }


    /**
     * Gets the trip name from a given data table.
     * @param dataTable     The data table containing the trip name.
     * @return              A string of the trip name.
     */
    private String getTripNameFromDataTable(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        return list.get(0).get("Name");
    }


    /**
     * Creates a new trip with the data given for the currently logged in user.
     * @param trip              The string containing the trip data.
     * @throws IOException      If the trip is formatted incorrectly.
     */
    @Given("I own the trip with the following data")
    public void iOwnTheTripWithTheFollowingData(String trip) throws IOException {
        createTripGenericRequest(trip, loggedInUserId);
    }


    /**
     * Creates a new trip with the data given for another user.
     * @param trip              The string containing the trip data.
     * @throws IOException      If the trip is formatted incorrectly.
     */
    @Given("I do not own the trip with the following data")
    public void iDoNotOwnTheTripWithTheFollowingName(String trip) throws IOException {
        createTripGenericRequest(trip, 3);
    }


    /**
     * Creates a trip with the given data and for the given user.
     * @param tripData          The data for the new trip to create.
     * @param ownerId           The id for the owner of the new trip.
     * @throws IOException      If the trip is formatted incorrectly.
     */
    private void createTripGenericRequest(String tripData, Integer ownerId) throws IOException {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .session(AUTHORIZED, ownerId.toString())
                .bodyJson(convertTripStringToJson(tripData))
                .uri(TRIPS_URI + ownerId.toString());
        route(application, request);
    }


    /**
     * Sends a request to delete the trip with the name specified in the data table.
     * @param dataTable     The data table containing the trip name to delete.
     */
    @When("I delete the trip with the following name")
    public void iDeleteTheTripWithTheFollowingName(io.cucumber.datatable.DataTable dataTable) {
        String tripName = getTripNameFromDataTable(dataTable);
        Integer tripId = getTripIdFromTripName(tripName).intValue();
        deleteTripRequest(tripId);
    }


    /**
     * Gets the trip id for the trip with the given name.
     * @param tripName  The name of the trip to get the id for.
     * @return          The id of the trip.
     */
    private Long getTripIdFromTripName(String tripName) {
        return tripRepository
                .getExpressionList()
                .select(TRIP_ID_FIELD)
                .where().eq(TRIP_NAME_FIELD, tripName)
                .findSingleAttribute();
    }


    /**
     * Sends a delete trip request for a given trip id. The session is set to the currently logged in user.
     * @param tripId    The id for the trip to be deleted.
     */
    private void deleteTripRequest(Integer tripId) {
        Http.RequestBuilder request = fakeRequest()
                .method(DELETE)
                .session(AUTHORIZED, loggedInUserId.toString())
                .uri(TRIPS_URI + tripId.toString());
        Result result = route(application, request);
        statusCode = result.status();
    }


    /**
     * Sends an edit trip request for a given trip id. The session is set to the currently logged in user.
     * @param tripId        The id of the trip being edited.
     * @param tripData      The json body of the edit request in the form of a string.
     * @throws IOException  The exception thrown
     */
    private void editTripRequest(Integer tripId, String tripData) throws IOException {
        Http.RequestBuilder request = fakeRequest()
                .method(PATCH)
                .session(AUTHORIZED, loggedInUserId.toString())
                .bodyJson(convertTripStringToJson(tripData))
                .uri(TRIPS_URI + tripId.toString());
        Result result = route(application, request);
        statusCode = result.status();
    }

    @When("I change the trip, {string} to contain the following data")
    public void iChangeTheTripToContainTheFollowingData(String tripName, String tripData) throws IOException {
        Integer tripId = getTripIdFromTripName(tripName).intValue();
        editTripRequest(tripId, tripData);
    }


    /**
     * Checks if the status code received is Forbidden (403).
     */
    @Then("the response status code is Forbidden")
    public void theResponseStatusCodeIsForbidden() {
        assertEquals(FORBIDDEN, statusCode);
    }


    /**
     * Checks if the status code received is OK (200).
     */
    @Then("the response status code is OK")
    public void theResponseStatusCodeIsOK() {
        assertEquals(OK, statusCode);
    }


    /**
     * Checks if the status code received is Created (201).
     */
    @Then("the response status code is Created")
    public void theResponseStatusCodeIsCreated() {
        assertEquals(CREATED, statusCode);
    }

    @Then("the destination with id {int} ownership changes to the user with id {int}")
    public void theDestinationOwnershipChangesToTheGlobalAdminWithId(Integer destinationId, Integer profileId) {
        Destination destination = destinationRepository.findById(destinationId.longValue());
        assertEquals(profileId.longValue(), destination.getOwner().getId().longValue());
    }

}
