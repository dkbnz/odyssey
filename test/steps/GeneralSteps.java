package steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.junit.Assert;
import play.db.Database;
import play.db.evolutions.Evolutions;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.*;

public class GeneralSteps {

    /**
     * Singleton to contain the test variables
     */
    private TestContext testContext = TestContext.getInstance();


    //------------------------------------Field Names---------------------------------------------

    /**
     * The username string variable.
     */
    private static final String USERNAME = "username";

    /**
     * The password string variable.
     */
    private static final String PASS_FIELD = "password";



    //--------------------------------------URIs-------------------------------------------

    /**
     * The login uri.
     */
    private static final String LOGIN_URI = "/v1/login";

    /**
     * The logout uri.
     */
    private static final String LOGOUT_URI = "/v1/logout";



    //----------------------------------Login Details---------------------------------------------

    /**
     * Valid login credentials for an admin user.
     */
    private static final String ADMIN_USERNAME = "admin@travelea.com";
    private static final String ADMIN_AUTH_PASS = "admin1";
    private static final String ADMIN_ID = "1";


    /**
     * Valid login credentials for a regular user.
     */
    private static final String REG_USERNAME = "guestUser@travelea.com";
    private static final String REG_AUTH_PASS = "guest123";
    private static final String REG_ID = "2";


    /**
     * Valid login credentials for an alternate user.
     */
    private static final String ALT_USERNAME = "testuser1@email.com";
    private static final String ALT_AUTH_PASS = "guest123";
    private static final String ALT_ID = "3";


    public void setUp() {

        Map<String, String> configuration = new HashMap<>();
        configuration.put("play.db.config", "db");
        configuration.put("play.db.default", "default");
        configuration.put("db.default.driver", "org.h2.Driver");
        configuration.put("db.default.url", "jdbc:h2:mem:testDBProfile;MODE=MYSQL;");
        configuration.put("ebean.default", "models.*");
        configuration.put("play.evolutions.db.default.enabled", "true");
        configuration.put("play.evolutions.autoApply", "false");

        //Set up the fake application to use the in memory database config
        testContext.setApplication(fakeApplication(configuration));

        testContext.setDatabase(testContext.getApplication().injector().instanceOf(Database.class));
        applyEvolutions();

        Helpers.start(testContext.getApplication());
    }


    /**
     * Applies down evolutions to the database from the test/evolutions/default directory.
     * This drops tables and data from the database.
     */
    private void applyEvolutions() {
        Evolutions.applyEvolutions(
                testContext.getDatabase(),
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
        Evolutions.cleanupEvolutions(testContext.getDatabase());
    }


    /**
     * Runs after each test scenario.
     * Sends a logout request.
     * Cleans up the database by cleaning up evolutions and shutting it down.
     * Stops running the fake application.JsonNode.
     */
    public void tearDown() {
        logoutRequest();
        cleanEvolutions();
//        Database database = testContext.getDatabase();
//        database.shutdown();
//        Helpers.stop(testContext.getApplication());
    }


    /**
     * Sends a fake request to the application to login.
     * @param username      the string of the username to complete the login with.
     * @param password      the string of the password to complete the login with.
     */
    private void loginRequest(String username, String password) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put(USERNAME, username);
        json.put(PASS_FIELD, password);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(LOGIN_URI);
        Result loginResult = route(testContext.getApplication(), request);
        testContext.setStatusCode(loginResult.status());
    }


    /**
     * Sends a fake request to the application to logout.
     */
    private void logoutRequest() {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .uri(LOGOUT_URI);
        Result logoutResult = route(testContext.getApplication(), request);
        testContext.setLoggedInId(null);
        testContext.setStatusCode(logoutResult.status());
    }


    @Given("the application is running")
    public void theApplicationIsRunning() {
        Assert.assertTrue(testContext.getApplication().isTest());
    }


    /**
     * Attempts to send a log in request with user credentials from constants VALID_USERNAME
     * and VALID_AUTH_PASS.
     *
     * Asserts the login was successful with a status code of OK (200).
     */
    @Given("I am logged in")
    public void iAmLoggedIn() {
        loginRequest(REG_USERNAME, REG_AUTH_PASS);
        assertEquals(OK, testContext.getStatusCode());
        testContext.setLoggedInId(REG_ID);
    }


    /**
     * Attempts to send a log in request with user credentials from constants ALT_USERNAME
     * and ALT_AUTH_PASS.
     *
     * Asserts the login was successful with a status code of OK (200).
     */
    @Given("I am logged in as an alternate user")
    public void iAmLoggedInAsAnAlternateUser() {
        loginRequest(ALT_USERNAME, ALT_AUTH_PASS);
        assertEquals(OK, testContext.getStatusCode());
        testContext.setLoggedInId(ALT_ID);
    }


    /**
     * Attempts to send a log in request with user credentials from constants ADMIN_USERNAME
     * and ADMIN_AUTH_PASS.
     *
     * Asserts the login was successful with a status code of OK (200).
     */
    @Given("I am logged in as an admin user")
    public void iAmLoggedInAsAnAdminUser() {
        loginRequest(ADMIN_USERNAME, ADMIN_AUTH_PASS);
        assertEquals(OK, testContext.getStatusCode());
        testContext.setLoggedInId(ADMIN_ID);
    }


    /**
     * Sends a logout request to the system.
     *
     * Asserts the value of loggedInId is null.
     */
    @Given("I am not logged in")
    public void iAmNotLoggedIn() {
        logoutRequest();
        assertNull(testContext.getLoggedInId());
    }


    @Then("^the status code received is (\\d+)$")
    public void theStatusCodeReceivedIs(int expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, testContext.getStatusCode());
    }

}
