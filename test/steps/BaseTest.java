package steps;

import org.junit.After;
import org.junit.Before;
import play.Application;
import play.test.Helpers;

import java.util.HashMap;
import java.util.Map;

import static play.test.Helpers.fakeApplication;

public class BaseTest {

    protected Application application = fakeApplication();

    @Before
    public void setUp() {
        Map<String, String> configuration = new HashMap<>();
        configuration.put("db.default.driver", "org.h2.Driver");
        configuration.put("db.default.url", "jdbc:h2:mem:play;MODE=MYSQL;");
        configuration.put("play.evolutions.db.default.enabled", "true");
        configuration.put("play.evolutions.autoApply", "true");

        //Set up the fake application to use the in memory database config
        application = Helpers.fakeApplication(configuration);
        Helpers.start(application);
    }

    @After
    public void tearDown() {
        Helpers.stop(application);
    }
}
