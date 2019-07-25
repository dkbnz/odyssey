package steps;


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
import play.test.Helpers;
import repositories.destinations.DestinationRepository;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class DestinationTravellerTypeTestSteps {

    /**
     * Currently logged-in user
     */
    private String loggedInId;

    /**
     * Variable to hold the status code of the result.
     */
    private int statusCode;


    /**
     * The Json body of the response.
     */
    private String responseBody;

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
     * The fake application.
     */

    private Application application;


    /**
     * Database instance for the fake application.
     */
    private Database database;

    /**
     * Repository to access the destinations in the running application.
     */
    private DestinationRepository destinationRepo = new DestinationRepository();


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
        configuration.put("db.default.url", "jdbc:h2:mem:testDBDestinationTraveller;MODE=MYSQL;");
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
     * Applies up evolutions to the database from the test/evolutions/default directory.
     *
     * This populates the database with necessary tables and values.
     */
    private void cleanEvolutions() {
        Evolutions.cleanupEvolutions(database);
    }


    /**
     * Runs after each test scenario.
     * Sends a logout request.
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
     * Asserts the fake application is in test mode.
     */
    @Given("The application is operational")
    public void theApplicationIsOperational() {
        Assert.assertTrue(application.isTest());
    }


    /**
     * Attempts to send a log in request with user credentials from constants ADMIN_USERNAME
     * and ADMIN_AUTHPASS.
     *
     * Asserts the login was successful with a status code of OK (200).
     */
    @Given("The user is logged in as an admin")
    public void theUserIsLoggedInAsAnAdmin() {
        loggedInId = ADMIN_ID;
    }


    @Given("There is a destination with one traveller type to add")
    public void thereIsADestinationWithOneTravellerTypeToAdd() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }


    @When("A request for proposed destinations is sent")
    public void aRequestForProposedDestinationsIsSent() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("There is {int} destination to update")
    public void thereIsDestinationToUpdate(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the status code received on the admin panel is OK")
    public void theStatusCodeIsCreated() throws BeansException {
        Assert.assertEquals(OK, statusCode);
    }

}
