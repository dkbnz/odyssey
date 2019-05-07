package steps;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import org.junit.Assert;
import org.springframework.beans.BeansException;
import play.ApplicationLoader;
import play.Environment;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static play.mvc.Results.ok;
import static play.test.Helpers.*;

public class ProfileTestSteps {


    @Inject
    Application application;

    private Http.RequestBuilder request;

    private int statusCode;
    private static final String PROFILES_URI = "/v1/profiles";


    @Before
    public void setUp() {
        Module testModule = new AbstractModule() { };

        GuiceApplicationBuilder builder = new GuiceApplicationLoader()
                .builder(new ApplicationLoader.Context(Environment.simple()))
                .overrides(testModule);
        Guice.createInjector(builder.applicationModule()).injectMembers(this);

        Helpers.start(application);
    }


    @After
    public void tearDown() {
        Helpers.stop(application);
    }


    @Given("the application is running")
    public void theApplicationIsRunning() throws BeansException {
        Assert.assertTrue(application.isTest());
    }


    @When("I send a GET request to the profiles endpoint")
    public void iSendAGETRequestToTheProfilesEndpoint() throws BeansException {

        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(PROFILES_URI);
        Result result = route(application, request);
        statusCode = result.status();
    }


    @Then("the status code is OK")
    public void theReceivedStatusCodeIs() throws BeansException{
        Assert.assertEquals(OK, statusCode);
    }



    @Given("the application is running [2]")
    public void theApplicationIsRunning2() throws BeansException {
        Assert.assertTrue(application.isTest());
    }


    @When("I send a GET request to the \\/travtypes endpoint")
    public void iSendAGETRequestToTheTravtypesEndpoint() throws BeansException {
        request = fakeRequest()
                .method(GET)
                .uri("/travtypes");
        Assert.assertTrue(request instanceof Http.RequestBuilder);
    }


    @Then("the received status code is ok() [2]")
    public void theReceivedStatusCodeIs2() throws BeansException {
        Result result = route(application, request);
        Assert.assertEquals(ok(), result.status());
    }






    private List<Map<String,String>> profile;

    @Given("I am connected to the TravelEA database")
    public void iAmConnectedToTheTravelEADatabase() {
        // Write code here that turns the phrase above into concrete actions

    }


    @Given("The following profile exists within the TravelEA database:")
    public void theFollowingProfileExistsWithinTheTravelEADatabase(DataTable dataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        this.profile = dataTable.asMaps();

    }


    @When("A user attempts to create a profile with the following field:")
    public void aUserAttemptsToCreateAProfileWithTheFollowingField(DataTable dataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        throw new cucumber.api.PendingException();
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
