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
import org.junit.*;
import play.Application;
import play.db.Database;
import play.db.evolutions.Evolutions;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static controllers.destinations.DestinationController.*;
import static org.junit.Assert.assertEquals;
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
     * The destination endpoint uri.
     */
    private static final String DESTINATION_URI = "/v1/destinations";


    /**
     * The login endpoint uri.
     */
    private static final String LOGIN_URI = "/v1/login";


    /**
     * The logout endpoint uri.
     */
    private static final String LOGOUT_URI = "/v1/logout";


    /**
     * A valid username for login credentials for admin user.
     */
    private static final String VALID_USERNAME = "admin@travelea.com";


    /**
     * A valid password for login credentials for admin user.
     */
    private static final String VALID_PASSWORD = "admin1";


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
        configuration.put("db.default.driver", "org.h2.Driver");
        configuration.put("db.default.url", "jdbc:h2:mem:testDB;MODE=MySQL;");
        configuration.put("play.evolutions.db.default.enabled", "true");
        configuration.put("play.evolutions.autoApply", "false");

        //Set up the fake application to use the in memory database config
        application = fakeApplication(configuration);
        Helpers.start(application);

        database = application.injector().instanceOf(Database.class);
        applyEvolutions();

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
                        "test/evolutions/default/"
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
     * Sends a request to create a destination with values from the given json node.
     * @param json      A JsonNode containing the values for a new destination object.
     */
    private void createDestinationRequest(JsonNode json) {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(DESTINATION_URI);
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
                .uri(DESTINATION_URI + query);
        Result result = route(application, request);
        statusCode = result.status();
    }


    /**
     * Asserts the fake application is in test mode.
     */
    @Given("I have a running application")
    public void i_have_a_running_application() {
        Assert.assertTrue(application.isTest());
    }


    /**
     * Attempts to send a log in request with user credentials from constants VALID_USERNAME
     * and VALID_PASSWORD.
     *
     * Asserts the login was successful with a status code of OK (200).
     */
    @Given("I am logged in")
    public void i_am_logged_in() {
        loginRequest(VALID_USERNAME, VALID_PASSWORD);
        assertEquals(OK, statusCode);
    }


    /**
     * Sends a request to create a new destination with valid values given in the data table to
     * ensure a destination already exists in the database.
     * @param dataTable     The data table containing values to create the new destination.
     */
    @And("a destination already exists with the following values")
    public void a_destination_exists_with_the_following_values(io.cucumber.datatable.DataTable dataTable) {
        JsonNode json = convertDataTableToJsonNode(dataTable);
        createDestinationRequest(json);
    }


    /**
     * Sends a request to get all destinations.
     */
    @When("I send a GET request to the destinations endpoint")
    public void i_send_a_GET_request_to_the_destinations_endpoint() {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(DESTINATION_URI);
        Result result = route(application, request);
        statusCode = result.status();
    }


    /**
     * Sends a request to create a new destination with valid values given in the data table.
     * @param dataTable     The data table containing values to create the new destination.
     */
    @When("I create a new destination with the following valid values")
    public void i_create_a_new_destination_with_the_following_valid_values(io.cucumber.datatable.DataTable dataTable) {
        JsonNode json = convertDataTableToJsonNode(dataTable);
        createDestinationRequest(json);
    }


    /**
     * Converts a given data table of destination values to a json node object of this destination.
     * @param dataTable     The data table containing values of a destination.
     * @return              A JsonNode of a destination containing information from the data table.
     */
    private JsonNode convertDataTableToJsonNode(io.cucumber.datatable.DataTable dataTable) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String name = list.get(0).get("Name");
        String type = list.get(0).get("Type");
        String district = list.get(0).get("District");
        String latitude = list.get(0).get("Latitude");
        String longitude = list.get(0).get("Longitude");
        String country = list.get(0).get("Country");

        //Add values to a JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.createObjectNode();

        ((ObjectNode) json).put(NAME, name);
        ((ObjectNode) json).put(TYPE, type);
        ((ObjectNode) json).put(LATITUDE, latitude);
        ((ObjectNode) json).put(LONGITUDE, longitude);
        ((ObjectNode) json).put(DISTRICT, district);
        ((ObjectNode) json).put(COUNTRY, country);

        return json;
    }


    /**
     * Sends a request to create a new destination with invalid values given in the data table.
     * @param dataTable     The data table containing values to create the new destination.
     */
    @When("I create a new destination with the following invalid values")
    public void i_create_a_new_destination_with_the_following_invalid_values(io.cucumber.datatable.DataTable dataTable) {
        JsonNode json = convertDataTableToJsonNode(dataTable);
        createDestinationRequest(json);
    }


    /**
     * Sends a request to create a new destination with duplicated values given in the data table.
     * @param dataTable     The data table containing values to create the new destination.
     */
    @When("I create a new destination with the following duplicated values")
    public void i_create_a_new_destination_with_the_following_duplicated_values(io.cucumber.datatable.DataTable dataTable) {
        JsonNode json = convertDataTableToJsonNode(dataTable);
        createDestinationRequest(json);
    }


    /**
     * Sends a request to search for a destination with name value given in the data table.
     * @param dataTable     The data table containing the field, name, and value for a destination search.
     */
    @When("I search for a destination with name")
    public void i_search_for_a_destination_with_name(io.cucumber.datatable.DataTable dataTable) {
        // Set up the search fields with given name
        String value = getValueFromDataTable("Name", dataTable);
        String query = createSearchDestinationQueryString(NAME, value);

        //Send search destinations request
        searchDestinationsRequest(query);
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
                .append(country);

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
    public void the_response_contains_at_least_one_destination_with_name(io.cucumber.datatable.DataTable dataTable) {
        throw new cucumber.api.PendingException();
    }


    /**
     * Checks if the status code received is OK (200).
     */
    @Then("the status code received is OK")
    public void the_status_code_received_is_OK() {
        assertEquals(OK, statusCode);
    }


    /**
     * Checks if the status code received is Created (201).
     */
    @Then("the status code received is Created")
    public void the_status_code_received_is_Created() {
        assertEquals(CREATED, statusCode);
    }


    /**
     * Checks if the status code received is BadRequest (400).
     */
    @Then("the status code received is BadRequest")
    public void the_status_code_received_is_BadRequest() {
        assertEquals(BAD_REQUEST, statusCode);
    }


    /**
     * Checks if the status code received is Unauthorised (401).
     */
    @Then("the status code received is Unauthorised")
    public void the_status_code_received_is_Unauthorised() {
        assertEquals(UNAUTHORIZED, statusCode);
    }
}
