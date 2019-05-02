import com.google.inject.Inject;
import controllers.ProfileController;
import org.junit.*;
import play.Application;
import play.db.Database;
import play.db.evolutions.Evolutions;
import play.mvc.Result;
import play.test.Helpers;

import static play.test.Helpers.*;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import play.mvc.Http.RequestBuilder;

import java.util.HashMap;
import java.util.Map;

public class ProfileControllerTest {

    private static Application application;
    private static Database database;

    /**
     * Sets up an application with a configuration for an in memory database for testing.
     * Starts this application before all tests are run in this class.
     */
    @BeforeClass
    public static void setUp() {
        //Set up a config to switch to using an h2 in memory database for testing
        Map<String, String> configuration = new HashMap<>();
        configuration.put("db.default.driver", "org.h2.Driver");
        configuration.put("db.default.url", "jdbc:h2:mem:play;MODE=MYSQL;");

        //Set up the fake application to use the in memory database config
        application = Helpers.fakeApplication(configuration);
        //database = application.injector().instanceOf(Database.class);

        //Start the application
        Helpers.start(application);
    }

    /**
     * Stops the application after all tests have been run in this class
     */
    @AfterClass
    public static void tearDown() {
        Helpers.stop(application);
    }

    /**
     * Applies the required initial set up for the tests.
     * Applies the evolutions to the test database.
     */
//    @Before
//    public void init() {
//        Evolutions.applyEvolutions(database);
//
////        Evolutions.applyEvolutions(database,
////                Evolutions.fromClassLoader(
////                        getClass().getClassLoader(), "testdatabase/")
////        );
//    }
//
//    /**
//     * Cleans up database following each test.
//     */
//    @After
//    public void cleanUp() {
//        Evolutions.cleanupEvolutions(database);
//    }

    @Test
    public void exampleTest() {
        RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri("/xx/Kiwi");

        Result result = route(application, request);
        assertEquals(NOT_FOUND, result.status());
    }
}
