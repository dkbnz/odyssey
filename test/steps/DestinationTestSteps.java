package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.*;
import play.Application;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class DestinationTestSteps extends BaseTest {

    private Application application = fakeApplication();

    private int statusCode;
    private static final String DUPLICATE_NAME = "Duplicate";
    private static final String DUPLICATE_DISTRICT = "Nelson";
    private static final String DESTINATION_URI = "/v1/destinations";


    @Given("I have a running application")
    public void i_have_a_running_application() {
        Assert.assertTrue(application.isTest());
    }

    @Then("the status code received is OK")
    public void the_status_code_received_is_OK() {
        assertEquals(OK, statusCode);
    }

    @Then("the status code received is BadRequest")
    public void the_status_code_received_is_BadRequest() {
        assertEquals(BAD_REQUEST, statusCode);
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

    @And("I am logged in")
    public void i_am_logged_in() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.createObjectNode();

        ((ObjectNode) json).put("username", "TestUser");
        ((ObjectNode) json).put("password", "Password123");


        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri("/api/login");
        route(application, request);
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
        ((ObjectNode) json).put("type", type);
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
}
