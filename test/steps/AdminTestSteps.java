package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
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
import java.util.*;

import static play.test.Helpers.*;

public class AdminTestSteps {

    @Inject
    Application application;
    protected Database database;

    private static final String AUTHORIZED = "authorized";
    private int statusCode;
    private static final String PROFILES_URI = "/v1/profiles";
    private static final String LOGIN_URI = "/v1/login";

    /**
     * A valid username for login credentials for admin user.
     */
    private static final String VALID_USERNAME = "admin@travelea.com";


    /**
     * A valid password for login credentials for admin user.
     */
    private static final String VALID_PASSWORD = "admin1";


    @Before
    public void setUp() {
        Map<String, String> configuration = new HashMap<>();
        configuration.put("play.db.config", "db");
        configuration.put("play.db.default", "default");
        configuration.put("db.default.driver", "org.h2.Driver");
        configuration.put("db.default.url", "jdbc:h2:mem:testDBAdmin;MODE=MYSQL;");
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
     * Runs after each test scenario.
     * Cleans up the database by cleaning up evolutions and shutting it down.
     * Stops running the fake application.
     */
    @After
    public void tearDown() {
        cleanEvolutions();
        database.shutdown();
        Helpers.stop(application);
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


    @Given("The following profile does not exist with username {string} within the TravelEA database")
    public void theFollowingProfileDoesNotExistWithUsernameWithinTheTravelEADatabase(String username) {
        // Sends the fake request
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, "1")
                .uri(PROFILES_URI);
        Result result = route(application, request);
        statusCode = result.status();

        // Gets the response
        Iterator<JsonNode> iterator = getTheResponseIterator(Helpers.contentAsString(result));

        // Finds profile from the iterator
        boolean foundProfile = false;
        while (iterator.hasNext() && !foundProfile) {
            JsonNode jsonProfile = iterator.next();
            if (jsonProfile.get("username").asText().equals(username)) {
                foundProfile = true;
            }
        }

        Assert.assertFalse(foundProfile);

    }

    @Given("An admin is logged in")
    public void anAdminIsLoggedIn() {
        loginRequest(VALID_USERNAME, VALID_PASSWORD);
        Assert.assertEquals(OK, statusCode);
    }

    @When("An admin attempts to create a profile with the following fields:")
    public void anAdminAttemptsToCreateAProfileWithTheFollowingFields(io.cucumber.datatable.DataTable dataTable) {
        // Creates the json for the profile
        JsonNode json = convertDataTableToJsonNode(dataTable);

        // Sending the fake request to the back end
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(PROFILES_URI);
        Result result = route(application, request);
        statusCode = result.status();
        ;
    }

    @Then("the status code is Created")
    public void theStatusCodeIsCreated() {
        Assert.assertEquals(CREATED, statusCode);
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

        // complex json
        ObjectMapper mapper = new ObjectMapper();

        List<String> nationality = new ArrayList<String>();
        List<String> traveller_type = new ArrayList<String>();
        List<String> passport = new ArrayList<String>();

        nationality.add(list.get(0).get("nationality"));
        traveller_type.add(list.get(0).get("traveller_type"));
        passport.add(list.get(0).get("passport_country"));

        ArrayNode nationalityNode = mapper.valueToTree(nationality);
        ArrayNode traveller_typeNode = mapper.valueToTree(traveller_type);
        ArrayNode passportNode = mapper.valueToTree(passport);

        //Add values to a JsonNode
        JsonNode json = mapper.createObjectNode();
        ((ObjectNode) json).put("username", username);
        ((ObjectNode) json).put("password", password);
        ((ObjectNode) json).put("firstName", firstName);
        ((ObjectNode) json).put("middleName", middleName);
        ((ObjectNode) json).put("lastName", lastName);
        ((ObjectNode) json).put("gender", gender);
        ((ObjectNode) json).put("dateOfBirth", dateOfBirth);
        ((ObjectNode) json).putArray("nationalities").addAll(nationalityNode);
        ((ObjectNode) json).putArray("travellerTypes").addAll(traveller_typeNode);
        ((ObjectNode) json).putArray("passports").addAll(passportNode);

        return json;
    }


}
