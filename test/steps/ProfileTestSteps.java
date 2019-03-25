package steps;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import controllers.ProfileController;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import models.Profile;
import org.junit.Assert;
import org.junit.runner.Request;
import play.ApplicationLoader;
import play.Environment;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static play.mvc.Results.ok;
import static play.test.Helpers.*;

public class ProfileTestSteps {


    @Inject
    Application application;

//    private Connection connection;
//
//    private ProfileController controller = null;

    private Http.RequestBuilder request;


    @Before
    public void setUp() throws SQLException {
        Module testModule = new AbstractModule() { };

        GuiceApplicationBuilder builder = new GuiceApplicationLoader()
                .builder(new ApplicationLoader.Context(Environment.simple()))
                .overrides(testModule);
        Guice.createInjector(builder.applicationModule()).injectMembers(this);

        Helpers.start(application);

//        connection = DriverManager.getConnection("jdbc:mysql:seng302-2019-team100-test");

    }


    @After
    public void tearDown() throws SQLException {
        Helpers.stop(application);
//        connection.close();
    }


    @Given("the application is running")
    public void theApplicationIsRunning() {
        Assert.assertTrue(application.isTest());
    }


    @When("I send a GET request to the \\/profiles endpoint")
    public void iSendAGETRequestToTheProfilesEndpoint() {
        request = fakeRequest()
                .method(GET)
                .uri("/profiles");
        Assert.assertTrue(request instanceof Http.RequestBuilder);
    }


    @Then("the received status code is ok()")
    public void theReceivedStatusCodeIs() {
        Result result = route(application, request);
        Assert.assertEquals(ok(), result.status());
    }



//    @Given("I am connected to the TravelEA database")
//    public void iAmConnectedToTheTravelEADatabase() {
//        //throw new cucumber.api.PendingException();
//        Assert.assertFalse(connection == null);
//    }
//
//
//    @Given("The username {string} exists within the TravelEA database")
//    public void theUsernameExistsWithinTheTravelEADatabase(String existingUsername) {
//        boolean usernameExists = controller.profileExists(existingUsername);
//        Assert.assertTrue(usernameExists);
//    }
//
//
//    @When("A new username, {string}, is passed through the ProfileController")
//    public void aNewUsernameIsPassedThroughTheProfileController(String newUsername) {
//        request = fakeRequest()
//                .method(POST)
//                .uri("api/checkUsername");
//    }
//
//    @Then("the status code received is {int}")
//    public void theStatusCodeReceivedIs(Integer statusCode) {
//
//        throw new cucumber.api.PendingException();
//    }


}
