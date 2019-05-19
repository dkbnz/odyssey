package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.ast.DataTable;
import gherkin.deps.com.google.gson.Gson;
import jdk.nashorn.internal.parser.JSONParser;
import org.junit.Assert;
import play.Application;
import play.db.Database;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;


import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.CREATED;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import play.db.evolutions.Evolutions;

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
        JsonNode json = mapper.createObjectNode();

        ((ObjectNode) json).put("username", username);
        ((ObjectNode) json).put("password", password);

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


    @When("the following json containing a trip is sent:")
    public void theFollowingJsonContainingATripIsSent(String docString) throws IOException {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .session(AUTHORIZED, "1")
                .bodyJson(convertTripStringToJson(docString))
                .uri(TRIPS_URI + 1);
        Result result = route(application, request);
        statusCode = result.status();
        //Assert.assertEquals("", convertTripStringToJson(docString));
    }





    private JsonNode convertTripStringToJson(String docString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(docString);
        return json;
    }



    @Given("I own the trip with the following name")
    public void iOwnTheTripWithTheFollowingName(io.cucumber.datatable.DataTable dataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        throw new cucumber.api.PendingException();
    }

    @When("I delete the trip with the following name")
    public void iDeleteTheTripWithTheFollowingName(io.cucumber.datatable.DataTable dataTable) {

        throw new cucumber.api.PendingException();
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


    @Given("I do not own the trip with the following name")
    public void iDoNotOwnTheTripWithTheFollowingName(io.cucumber.datatable.DataTable dataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        throw new cucumber.api.PendingException();
    }


    /**
     * Checks if the status code received is Unauthorised (401).
     */
    @Then("the response status code is Unauthorised")
    public void theResponseStatusCodeIsUnauthorised() {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(UNAUTHORIZED, statusCode);
    }


    /**
     * Checks if the status code received is OK (200).
     */
    @Then("the response status code is OK")
    public void theResponseStatusCodeIsOK() {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(OK, statusCode);
    }


    /**
     * Checks if the status code received is Created (201).
     */
    @Then("the response status code is Created")
    public void theResponseStatusCodeIsCreated() {
        assertEquals(CREATED, statusCode);
    }

}
