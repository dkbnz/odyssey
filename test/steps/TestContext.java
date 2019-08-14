package steps;

import com.google.inject.Inject;
import play.Application;
import play.db.Database;

public class TestContext {

    private static final TestContext INSTANCE = new TestContext();


    @Inject
    Application application;
    private Database database;

    /**
     * Stores the status code of a response.
     */
    private int statusCode;


    /**
     * ID of currently logged in user
     */
    private String loggedInId;


    /**
     * The Json body of the response.
     */
    private String responseBody;


    /**
     * Target user for changes
     */
    private String targetId;



    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getLoggedInId() {
        return loggedInId;
    }

    public void setLoggedInId(String loggedInId) {
        this.loggedInId = loggedInId;
    }


    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public static TestContext getInstance() {
        return INSTANCE;
    }
}
