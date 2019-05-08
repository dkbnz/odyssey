package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import org.junit.Assert;
import org.springframework.beans.BeansException;
import play.Application;
import play.db.Database;
import play.db.evolutions.Evolutions;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;

public class ProfileTestSteps {

    @Inject
    Application application;
    protected Database database;

    private Http.RequestBuilder request;

    private int statusCode;
    private int loginStatusCode;
    private static final String PROFILES_URI = "/v1/profiles";
    private static final String TRAVTYPES_URI = "/v1/travtypes";
    private static final String NATIONALITIES_URI = "/v1/nationalities";
    private static final String LOGIN_URI = "/v1/login";
    private static final String LOGOUT_URI = "/v1/logout";

    private static final String VALID_USERNAME = "admin@travelea.com";
    private static final String VALID_PASSWORD = "admin1";
    private static final int NUMBER_OF_TRAVELTYPES = 7;
    private static final int NUMBER_OF_NATIONALITIES = 108;




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
    public void tearDown() {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .uri(LOGOUT_URI);
        route(application, request);
        Helpers.stop(application);
    }


    @Given("the application is running")
    public void theApplicationIsRunning() throws BeansException {
        Assert.assertTrue(application.isTest());
    }

    @And("I have logged in")
    public void i_have_logged_in() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.createObjectNode();

        ((ObjectNode) json).put("username", VALID_USERNAME);
        ((ObjectNode) json).put("password", VALID_PASSWORD);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(LOGIN_URI);
        Result loginResult = route(application, request);

        statusCode = loginResult.status();
    }


    @When("I send a GET request to the profiles endpoint")
    public void iSendAGETRequestToTheProfilesEndpoint() throws BeansException {

        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(PROFILES_URI);
        Result result = route(application, request);
        statusCode = result.status();
        String json = Helpers.contentAsString(result);
        Assert.assertEquals("", json);
    }


    @Then("the status code is OK")
    public void theReceivedStatusCodeIs() throws BeansException{
        Assert.assertEquals(OK, statusCode);
    }


    @When("I send a GET request to the \\/travtypes endpoint")
    public void iSendAGETRequestToTheTravtypesEndpoint() throws BeansException {
        // Does the request to back end
        request = fakeRequest()
                .method(GET)
                .uri(TRAVTYPES_URI);
        Result result = route(application, request);

        // Gets the response
        Iterator<JsonNode> iterator = getTheResponseIterator(Helpers.contentAsString(result));

        // Checks the response for Holidaymaker and length of 7 traveller types
        boolean passTraveltypes = false;
        int count = 0;
        while (iterator.hasNext()) {
            JsonNode jsonTravellerType = iterator.next();
            count++;
            if (jsonTravellerType.get("id").asText().equals("5")) {
                if (jsonTravellerType.get("travellerType").asText().equals("Holidaymaker")) {
                    passTraveltypes = true;
                }
            }
        }
        if (count != NUMBER_OF_TRAVELTYPES) {
            passTraveltypes = false;
        }
        statusCode = result.status();
        Assert.assertEquals(true, passTraveltypes);
    }

    @When("I send a GET request to the \\/nationalities endpoint")
    public void iSendAGETRequestToTheNationalitiesEndpoint() {
        // Does the fake request to back end
        request = fakeRequest()
                .method(GET)
                .uri(NATIONALITIES_URI);
        Result result = route(application, request);

        // Gets the response
        Iterator<JsonNode> iterator = getTheResponseIterator(Helpers.contentAsString(result));

        // Checks the response for the nationality
        boolean passNationalities = false;
        int count = 0;
        while (iterator.hasNext()) {
            JsonNode jsonTravellerType = iterator.next();
            count++;
            if (jsonTravellerType.get("id").asText().equals("16")) {
                if (jsonTravellerType.get("nationality").asText().equals("Chinese")) {
                    passNationalities = true;
                }
            }
        }
        if (count != NUMBER_OF_NATIONALITIES) {
            passNationalities = false;
        }

        statusCode = result.status();
        Assert.assertEquals(true, passNationalities);
    }

    /**
     * Gets the response as an iterator array Node from any fake request so that you can iterate over the response data
     * @param content the string of the result using helper content as string
     * @return an Array node iterator
     */
    private Iterator<JsonNode> getTheResponseIterator(String content) {
        JsonNode arrNode = null;
        try {
            arrNode = new ObjectMapper().readTree(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrNode.elements();
    }

    @Given("The following profile exists with username {string} within the TravelEA database:")
    public void theFollowingProfileExistsWithUsernameWithinTheTravelEADatabase(String username) {
        // Sends the fake request
        request = fakeRequest()
                .method(GET)
                .uri(PROFILES_URI);
        Result result = route(application, request);
        statusCode = result.status();

        // Gets the response
        Iterator<JsonNode> iterator = getTheResponseIterator(Helpers.contentAsString(result));

        // Finds profile from the iterator
        boolean foundProfile = false;
        while (iterator.hasNext() && !foundProfile) {
            JsonNode jsonProfile = iterator.next();
            if (jsonProfile.get("username").equals(username)) {
                foundProfile = true;
            }
        }

        Assert.assertTrue(foundProfile);

    }


    @When("A user attempts to create a profile with the following fields:")
    public void aUserAttemptsToCreateAProfileWithTheFollowingFields(io.cucumber.datatable.DataTable dataTable) {
        // Creates the json for the profile
        JsonNode json = convertDataTableToJsonNode(dataTable);

        // Sending the fake request to the back end
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(PROFILES_URI);
        Result result = route(application, request);
        statusCode = result.status();
    }

    /**
     * Converts given data table information and creates a profile json for creating a profile
     * @param dataTable the data table from cucumber
     * @return the json formatted string of the profile
     */
    private JsonNode convertDataTableToJsonNode(io.cucumber.datatable.DataTable dataTable) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String username = list.get(0).get("username");
        String password = list.get(0).get("password");
        String firstName = list.get(0).get("first_name");
        String middleName = list.get(0).get("middle_name");
        String lastName = list.get(0).get("last_name");
        String gender = list.get(0).get("gender");
        String dateOfBirth = list.get(0).get("date_of_birth");
        String nationality = list.get(0).get("nationality");
        String traveller_type = list.get(0).get("traveller_type");

        //Add values to a JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.createObjectNode();

        ((ObjectNode) json).put("username", username);
        ((ObjectNode) json).put("password", password);
        ((ObjectNode) json).put("first_name", firstName);
        ((ObjectNode) json).put("middle_name", middleName);
        ((ObjectNode) json).put("last_name", lastName);
        ((ObjectNode) json).put("gender", gender);
        ((ObjectNode) json).put("date_of_birth", dateOfBirth);
        ((ObjectNode) json).put("nationality", nationality);
        ((ObjectNode) json).put("traveller_type", traveller_type);

        return json;
    }

}
