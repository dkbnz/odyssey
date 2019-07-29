package steps;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import play.Application;
import play.db.Database;
import play.db.evolutions.Evolutions;
import play.test.Helpers;
import repositories.treasureHunts.TreasureHuntRepository;

import java.util.HashMap;
import java.util.Map;

import static play.test.Helpers.fakeApplication;

public class TreasureHuntTestSteps {

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
     * The treasure hunt uri.
     */
    private static final String TREASURE_HUNT_URI = "/treasureHunt";


    /**
     * Valid login credentials for an admin user.
     */
    private static final String ADMIN_ID = "1";


    /**
     * Valid login credentials for a regular user.
     */
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
    private TreasureHuntRepository treasureHuntRepository = new TreasureHuntRepository();



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
        configuration.put("db.default.url", "jdbc:h2:mem:testDBTreasureHunt;MODE=MYSQL;");
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
}
