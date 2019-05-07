import cucumber.api.java.After;
import cucumber.api.java.Before;
import play.Application;
import play.db.Database;
import play.db.evolutions.Evolutions;
import play.test.Helpers;

import java.util.HashMap;
import java.util.Map;

import static play.test.Helpers.fakeApplication;

public class BaseTest {

    protected Application application;
    protected Database database;

    @Before
    public void setUp() {
//        Map<String, String> configuration = new HashMap<>();
//        configuration.put("db.default.driver", "org.h2.Driver");
//        configuration.put("db.default.url", "jdbc:h2:mem:testDB;MODE=MYSQL;");
//        configuration.put("play.evolutions.db.default.enabled", "true");
//        configuration.put("play.evolutions.autoApply", "false");
//
//        //Set up the fake application to use the in memory database config
//        application = fakeApplication(configuration);
//        Helpers.start(application);
//        database = application.injector().instanceOf(Database.class);
//
//        Evolutions.applyEvolutions(
//                database,
//                Evolutions.fromClassLoader(
//                        getClass().getClassLoader(),
//                        "test/"
//                )
//        );
    }

    @After
    public void cleanEvolutions() {
//        Evolutions.cleanupEvolutions(database);
//        Helpers.stop(application);
    }
}
