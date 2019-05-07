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

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.CREATED;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class DestinationTestSteps {

    private int statusCode;
    private int loginStatusCode;
    private static final String DUPLICATE_NAME = "Duplicate";
    private static final String DUPLICATE_DISTRICT = "Nelson";
    private static final String DESTINATION_URI = "/v1/destinations";
    private static final String LOGIN_URI = "/v1/login";
    private static final String LOGOUT_URI = "/v1/logout";

    private static final String VALID_USERNAME = "admin@travelea.com";
    private static final String VALID_PASSWORD = "admin1";

    //private static final String VALID_USERNAME = "guestUser@travelea.com";
    //private static final String VALID_PASSWORD = "guest123";

    protected Application application;
    protected Database database;

    @Before
    public void setUp() {
        Map<String, String> configuration = new HashMap<>();
        configuration.put("db.default.driver", "org.h2.Driver");
        configuration.put("db.default.url", "jdbc:h2:mem:testDB;MODE=MYSQL;");
        configuration.put("play.evolutions.db.default.enabled", "true");
        configuration.put("play.evolutions.autoApply", "false");

        //Set up the fake application to use the in memory database config
        application = fakeApplication(configuration);
        Helpers.start(application);

        database = application.injector().instanceOf(Database.class);

        Evolutions.applyEvolutions(
                database,
                Evolutions.fromClassLoader(
                        getClass().getClassLoader(),
                        "test/evolutions/default/"
                )
        );
    }

    @After
    public void cleanEvolutions() {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .uri(LOGOUT_URI);
        route(application, request);
        Evolutions.cleanupEvolutions(database);
        database.shutdown();
        Helpers.stop(application);
    }

    @Given("I have a running application")
    public void i_have_a_running_application() {
        Assert.assertTrue(application.isTest());
    }

    @And("a destination already exists with the name \"Duplicate\" and district \"Nelson\"")
    public void a_destination_exists_with_name_and_district() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.createObjectNode();

        ((ObjectNode) json).put("name", DUPLICATE_NAME);
        ((ObjectNode) json).put("type", "BANK");
        ((ObjectNode) json).put("latitude", "10");
        ((ObjectNode) json).put("longitude", "20");
        ((ObjectNode) json).put("district", DUPLICATE_DISTRICT);
        ((ObjectNode) json).put("country", "New Zealand");

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(DESTINATION_URI);
        route(application, request);
    }

    @Given("I am logged in")
    public void i_am_logged_in() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.createObjectNode();

        ((ObjectNode) json).put("username", VALID_USERNAME);
        ((ObjectNode) json).put("password", VALID_PASSWORD);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(LOGIN_URI);
        Result loginResult = route(application, request);
        loginStatusCode = loginResult.status();
        assertEquals(OK, loginStatusCode);
    }

    @When("I send a GET request to the destinations endpoint")
    public void i_send_a_GET_request_to_the_destinations_endpoint() {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(DESTINATION_URI);
        Result result = route(application, request);
        statusCode = result.status();
    }

    @When("I create a new destination with the following valid values")
    public void i_create_a_new_destination_with_the_following_valid_values(io.cucumber.datatable.DataTable dataTable) {
        JsonNode json = convertDataTableToJsonNode(dataTable);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(DESTINATION_URI);
        Result result = route(application, request);
        statusCode = result.status();
    }

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

        ((ObjectNode) json).put("name", name);
        ((ObjectNode) json).put("type_id", type);
        ((ObjectNode) json).put("latitude", latitude);
        ((ObjectNode) json).put("longitude", longitude);
        ((ObjectNode) json).put("district", district);
        ((ObjectNode) json).put("country", country);

        return json;
    }

    @When("I create a new destination with the following invalid values")
    public void i_create_a_new_destination_with_the_following_invalid_values(io.cucumber.datatable.DataTable dataTable) {
        JsonNode json = convertDataTableToJsonNode(dataTable);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(DESTINATION_URI);
        Result result = route(application, request);
        statusCode = result.status();
    }

    @When("I create a new destination with the following duplicated values")
    public void i_create_a_new_destination_with_the_following_duplicated_values(io.cucumber.datatable.DataTable dataTable) {
        JsonNode json = convertDataTableToJsonNode(dataTable);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(DESTINATION_URI);
        Result result = route(application, request);
        statusCode = result.status();
    }

    @Then("the status code received is OK")
    public void the_status_code_received_is_OK() {
        assertEquals(OK, statusCode);
    }

    @Then("the status code received is Created")
    public void the_status_code_received_is_Created() {
        assertEquals(CREATED, statusCode);
    }

    @Then("the status code received is BadRequest")
    public void the_status_code_received_is_BadRequest() {
        assertEquals(BAD_REQUEST, statusCode);
    }

    @Then("the status code received is Unauthorised")
    public void the_status_code_received_is_Unauthorised() {
        assertEquals(UNAUTHORIZED, statusCode);
    }

}
