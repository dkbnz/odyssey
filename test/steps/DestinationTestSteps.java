package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.Before;
import models.destinations.Destination;
import org.apache.xpath.operations.Bool;
import org.junit.*;
import play.Application;
import play.api.libs.json.Json;
import play.db.Database;

import play.db.evolutions.Evolutions;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import repositories.DestinationRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static controllers.destinations.DestinationController.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.CREATED;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class DestinationTestSteps {


    /**
     * Variable to hold the status code of the result.
     */
    private int statusCode;


    /**
     * The Json body of the response.
     */
    private String responseBody;


    /**
     * The destination endpoint uri.
     */
    private static final String DESTINATION_URI = "/v1/destinations";

    /**
     * The search by user endpoint uri.
     */
    private static final String SEARCH_BY_USER_URI = "/v1/destinations/";

    /**
     * Authorisation token for sessions
     */
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
     * Valid login credentials for an admin user.
     */
    private static final String ADMIN_USERNAME = "admin@travelea.com";
    private static final String ADMIN_AUTHPASS = "admin1";
    private static final String ADMIN_ID = "1";

    /**
     * Valid login credentials for a regular user.
     */
    private static final String REG_USERNAME = "guestUser@travelea.com";
    private static final String REG_AUTHPASS = "guest123";
    private static final String REG_ID = "2";

    /**
     * Currently logged-in user
     */
    private String LOGGED_IN_ID;

    /**
     * Target user for destination changes
     */
    private String TARGET_ID;

    /**
     * String to add the equals character (=) to build a query string.
     */
    private static final String EQUALS = "=";


    /**
     * String to add the ampersand character (&) to build a query string.
     */
    private static final String AND = "&";


    /**
     * String to add the question mark character (?) to build a query string.
     */
    private static final String QUESTION_MARK = "?";

    private static final String DISTRICT_STRING = "District";
    private static final String LATITUDE_STRING = "Latitude";
    private static final String LONGITUDE_STRING = "Longitude";
    private static final String COUNTRY_STRING = "Country";
    private static final String TYPE_STRING = "Type";
    private static final String NAME_STRING = "Name";
    private static final String IS_PUBLIC_STRING = "is_public";

    /**
     * The fake application.
     */

    private Application application;


    /**
     * Database instance for the fake application.
     */
    private Database database;

    /**
     * Runs before each test scenario.
     * Sets up a fake application for testing.
     * Applies configuration settings to use an in memory database for the fake application.
     * Starts the application.
     * Calls apply evolutions to set up the database state.
     */
    @Before
    public void setUp() {
        Map<String, String> configuration = new HashMap<>();
        configuration.put("play.db.config", "db");
        configuration.put("play.db.default", "default");
        configuration.put("db.default.driver", "org.h2.Driver");
        configuration.put("db.default.url", "jdbc:h2:mem:testDBDestination;MODE=MYSQL;");
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
     * Sends a request to create a destination with values from the given json node.
     * @param json      A JsonNode containing the values for a new destination object.
     */
    private void createDestinationRequest(JsonNode json) {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(DESTINATION_URI + "/" + TARGET_ID)
                .session(AUTHORIZED, LOGGED_IN_ID);
        Result result = route(application, request);
        statusCode = result.status();
    }


    /**
     * Sends a request to search for a destination with the given query string.
     * @param query     A String containing the query parameters for the search.
     */
    private void searchDestinationsRequest(String query) {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, LOGGED_IN_ID)
                .uri(DESTINATION_URI + query);
        Result result = route(application, request);
        statusCode = result.status();

        responseBody = Helpers.contentAsString(result);
    }


    /**
     * Asserts the fake application is in test mode.
     */
    @Given("I have a running application")
    public void iHaveARunningApplication() {
        Assert.assertTrue(application.isTest());
    }


    /**
     * Attempts to send a log in request with user credentials from constants VALID_USERNAME
     * and VALID_AUTHPASS.
     *
     * Asserts the login was successful with a status code of OK (200).
     */
    @Given("I am logged in")
    public void iAmLoggedIn() {
        loginRequest(REG_USERNAME, REG_AUTHPASS);
        assertEquals(OK, statusCode);
        LOGGED_IN_ID = REG_ID;
    }

    /**
     * Attempts to send a log in request with user credentials from constants ADMIN_USERNAME
     * and ADMIN_AUTHPASS.
     *
     * Asserts the login was successful with a status code of OK (200).
     */
    @Given("I am logged in as an admin user")
    public void iAmLoggedInAsAnAdminUser() {
        loginRequest(ADMIN_USERNAME, ADMIN_AUTHPASS);
        assertEquals(OK, statusCode);
        LOGGED_IN_ID = ADMIN_ID;
    }


    /**
     * Sends a logout request.
     */
    @Given("I am not logged in")
    public void iAmNotLoggedIn() {
        logoutRequest();
    }


    /**
     * Sends a request to create a new destination with valid values given in the data table to
     * ensure a destination already exists in the database.
     * @param dataTable     The data table containing values to create the new destination.
     */
    @And("a destination already exists with the following values")
    public void aDestinationExistsWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        TARGET_ID = LOGGED_IN_ID;
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode json = convertDataTableToJsonNode(dataTable, i);
            createDestinationRequest(json);
        }
    }


    @Given("a destination already exists for user {int} with the following values")
    public void aDestinationAlreadyExistsForUserWithTheFollowingValues(Integer userId, io.cucumber.datatable.DataTable dataTable) {
        TARGET_ID = userId.toString();

        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode json = convertDataTableToJsonNode(dataTable, i);
            createDestinationRequest(json);
        }
    }


    @Given("a destination with the following values is part of a trip")
    public void aDestinationWithTheFollowingValuesIsPartOfATrip(io.cucumber.datatable.DataTable dataTable) {
        // Get destination id from values given
        //TODO: still working this out
        // Create new trip with that destination and some others
        throw new cucumber.api.PendingException();
    }


    /**
     * Sends a request to get all destinations.
     */
    @When("I send a GET request to the destinations endpoint")
    public void iSendAGetRequestToTheDestinationsEndpoint() {
        TARGET_ID = LOGGED_IN_ID;
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, LOGGED_IN_ID)
                .uri(DESTINATION_URI);
        Result result = route(application, request);
        statusCode = result.status();
    }


    /**
     * Sends a request to create a new destination with values given in the data table.
     * @param dataTable     The data table containing values to create the new destination.
     */
    @When("I create a new destination with the following values")
    public void iCreateANewDestinationWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        TARGET_ID = LOGGED_IN_ID;
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode json = convertDataTableToJsonNode(dataTable, i);
            createDestinationRequest(json);
        }
    }


    @When("I create a new destination with the following values for another user")
    public void iCreateANewDestinationWithTheFollowingValuesForAnotherUser(io.cucumber.datatable.DataTable dataTable) {
        TARGET_ID = LOGGED_IN_ID;
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode json = convertDataTableToJsonNode(dataTable, i);
            createDestinationRequest(json);
        }
    }


    /**
     * Converts a given data table of destination values to a json node object of this destination.
     * @param dataTable     The data table containing values of a destination.
     * @return              A JsonNode of a destination containing information from the data table.
     */
    private JsonNode convertDataTableToJsonNode(io.cucumber.datatable.DataTable dataTable, int index) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String name         = list.get(index).get(NAME_STRING);
        String type         = list.get(index).get(TYPE_STRING);
        String district     = list.get(index).get(DISTRICT_STRING);
        String latitude     = list.get(index).get(LATITUDE_STRING);
        String longitude    = list.get(index).get(LONGITUDE_STRING);
        String country      = list.get(index).get(COUNTRY_STRING);
        String is_public    = list.get(index).get(IS_PUBLIC_STRING);

        //Test destinations are public by default
        Boolean publicity = (is_public == null ||
                !is_public.equalsIgnoreCase("false"));

        //Add values to a JsonNode
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put(NAME, name);
        json.put(TYPE, type);
        json.put(LATITUDE, latitude);
        json.put(LONGITUDE, longitude);
        json.put(DISTRICT, district);
        json.put(COUNTRY, country);
        json.put(IS_PUBLIC, publicity);

        return json;
    }


    /**
     * Sends a request to search for a destination with name value given in the data table.
     * @param dataTable     The data table containing the field, name, and value for a destination search.
     */
    @When("I search for a destination with name")
    public void iSearchForADestinationWithName(io.cucumber.datatable.DataTable dataTable) {
        // Set up the search fields with given name
        String value = getValueFromDataTable("Name", dataTable);
        String query = createSearchDestinationQueryString(NAME, value);

        //Send search destinations request
        searchDestinationsRequest(query);
    }


    @When("I search for a destination with district")
    public void iSearchForADestinationWithDistrict(io.cucumber.datatable.DataTable dataTable) {
        // Set up the search fields with given district
        String value = getValueFromDataTable(DISTRICT_STRING, dataTable);
        String query = createSearchDestinationQueryString(DISTRICT, value);

        //Send search destinations request
        searchDestinationsRequest(query);
    }


    @When("I search for a destination with latitude")
    public void iSearchForADestinationWithLatitude(io.cucumber.datatable.DataTable dataTable) {
        // Set up the search fields with given district
        String value = getValueFromDataTable(LATITUDE_STRING, dataTable);
        String query = createSearchDestinationQueryString(LATITUDE, value);

        //Send search destinations request
        searchDestinationsRequest(query);
    }


    @When("I search for all destinations")
    public void iSearchForAllDestinations() {
        String query = createSearchDestinationQueryString("", "");

        //Send search destinations request
        searchDestinationsRequest(query);
    }


    @When("I search for all destinations by user {int}")
    public void iSearchForAllDestinationsByUser(Integer userId) {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(SEARCH_BY_USER_URI + userId)
                .session(AUTHORIZED, LOGGED_IN_ID);
        Result result = route(application, request);
        statusCode = result.status();

        responseBody = Helpers.contentAsString(result);
    }


    @When("I attempt to delete the destination with the following values")
    public void iAttemptToDeleteTheDestinationWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        // TODO: Still working on how to do this
        // Get destination id from values given
        JsonNode json = convertDataTableToJsonNode(dataTable, 0);
        // Create search query based on values given?

        // Send the delete request

        throw new cucumber.api.PendingException();
    }


    /**
     * Gets a value associated with a given field from the given data table.
     * @param field         The title of the data table column to extract.
     * @param dataTable     The data table containing the value to extract.
     * @return              A String of the value extracted.
     */
    private String getValueFromDataTable(String field, io.cucumber.datatable.DataTable dataTable) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        return list.get(0).get(field);
    }


    /**
     * Creates a query string for the search destination request.
     * Builds this query string with empty values except for the given search value associated
     * with the given search field.
     * @param searchField       The search field name for the given value.
     * @param searchValue       The given search value for associated field.
     * @return                  The complete query string.
     */
    private String createSearchDestinationQueryString(String searchField, String searchValue) {
        String name = getValue(NAME, searchField, searchValue);
        String type = getValue(TYPE, searchField, searchValue);
        String latitude = getValue(LATITUDE, searchField, searchValue);
        String longitude = getValue(LONGITUDE, searchField, searchValue);
        String district = getValue(DISTRICT, searchField, searchValue);
        String country = getValue(COUNTRY, searchField, searchValue);


        StringBuilder stringBuilder = new StringBuilder()
                .append(QUESTION_MARK)

                .append(NAME)
                .append(EQUALS)
                .append(name)

                .append(AND)
                .append(TYPE)
                .append(EQUALS)
                .append(type)

                .append(AND)
                .append(LATITUDE)
                .append(EQUALS)
                .append(latitude)

                .append(AND)
                .append(LONGITUDE)
                .append(EQUALS)
                .append(longitude)

                .append(AND)
                .append(DISTRICT)
                .append(EQUALS)
                .append(district)

                .append(AND)
                .append(COUNTRY)
                .append(EQUALS)
                .append(country)

                .append(AND)
                .append(IS_PUBLIC)
                .append(EQUALS)
                .append("1");

        return stringBuilder.toString();
    }

    /**
     * Returns a string that is either empty or containing the given value.
     * Checks if the given field matches the search field. If so, returns the given value to search.
     * @param searchField       The search field name as defined by the application.
     * @param givenField        The field name given to the test.
     * @param givenValue        The value to search for if the search and given fields match.
     * @return
     */
    private String getValue(String searchField, String givenField, String givenValue) {
        return searchField.equals(givenField) ? givenValue : "";
    }


    /**
     * Checks if the response body from the previous query contains at least one destination with a given name.
     * @param dataTable     The data table containing the name of the destination that should exist in the
     *                      response.
     */
    @Then("the response contains at least one destination with name")
    public void theResponseContainsAtLeastOneDestinationWithName(io.cucumber.datatable.DataTable dataTable) throws IOException {
        String value = getValueFromDataTable("Name", dataTable);
        String arrNode = new ObjectMapper().readTree(responseBody).get(0).get(NAME).asText();

        Assert.assertEquals(value, arrNode);
    }


    @Then("the response contains at least one destination with district")
    public void theResponseContainsAtLeastOneDestinationWithDistrict(io.cucumber.datatable.DataTable dataTable) throws IOException {
        String value = getValueFromDataTable(DISTRICT_STRING, dataTable);
        String arrNode = new ObjectMapper().readTree(responseBody).get(0).get(DISTRICT).asText();

        Assert.assertEquals(value, arrNode);
    }


    @Then("the response contains at least one destination with latitude")
    public void theResponseContainsAtLeastOneDestinationWithLatitude(io.cucumber.datatable.DataTable dataTable) throws IOException {
        String value = getValueFromDataTable(LATITUDE_STRING, dataTable);
        String arrNode = new ObjectMapper().readTree(responseBody).get(0).get(LATITUDE).asText();

        Assert.assertEquals(value, arrNode);
    }


    @Then("the response is empty")
    public void theResponseIsEmpty() throws IOException {
        JsonNode arrNode = new ObjectMapper().readTree(responseBody);

        Assert.assertEquals(0, arrNode.size());
    }


    @Then("the response contains only public destinations")
    public void theResponseContainsOnlyPublicDestinations() throws IOException {
        JsonNode arrNode = new ObjectMapper().readTree(responseBody);
        for (int i = 0 ; i < arrNode.size() ; i++) {
            assertTrue(arrNode.get(i).get("public").asBoolean());
        }
    }


    @Then("the response contains only destinations owned by the user with id {int}")
    public void theResponseContainsOnlyDestinationsOwnedByTheUserWithId(Integer id) throws IOException {
        Long userId = id.longValue();
        JsonNode arrNode = new ObjectMapper().readTree(responseBody);
        Long ownerId;
        for (int i = 0 ; i < arrNode.size() ; i++) {
            DestinationRepository destinationRepo = new DestinationRepository();
            ownerId = destinationRepo.fetch(arrNode.get(i).get("id").asLong()).getOwner().getId();  //Gets owner id of destination
            assertEquals(userId, ownerId);
        }
    }


    /**
     * Checks if the status code received is OK (200).
     */
    @Then("the status code received is OK")
    public void theStatusCodeReceivedIsOk() {
        assertEquals(OK, statusCode);
    }


    /**
     * Checks if the status code received is Created (201).
     */
    @Then("the status code received is Created")
    public void theStatusCodeReceivedIsCreated() {
        assertEquals(CREATED, statusCode);
    }


    /**
     * Checks if the status code received is Bad Request (400).
     */
    @Then("the status code received is Bad Request")
    public void theStatusCodeReceivedIsBadRequest() {
        assertEquals(BAD_REQUEST, statusCode);
    }


    /**
     * Checks if the status code received is Unauthorised (401).
     */
    @Then("the status code received is Unauthorised")
    public void theStatusCodeReceivedIsUnauthorised() {
        assertEquals(UNAUTHORIZED, statusCode);
    }


    /**
     * Checks if the status code received is Not Found (404).
     */
    @Then("the status code received is Not Found")
    public void theStatusCodeReceivedIsNotFound() {
        assertEquals(NOT_FOUND, statusCode);
    }


    /**
     * Checks if the status code received is Forbidden (403).
     */
    @Then("the status code received is Forbidden")
    public void theStatusCodeReceivedIsForbidden() {
        assertEquals(FORBIDDEN, statusCode);
    }
}
