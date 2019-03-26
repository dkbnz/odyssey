package steps;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import play.Application;
import play.ApplicationLoader;
import play.Environment;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

public class DestinationTestSteps {

    @Inject
    Application application;

    private int statusCode;

    @Before
    public void setup() {
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

    @Given("I have a running application")
    public void i_have_a_running_application() {
        Assert.assertNotNull(application);
    }

    @When("I send a GET request to the destinations endpoint")
    public void i_send_a_GET_request_to_the_destinations_endpoint() {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri("/api/destinations");
        Result result = route(application, request);
        statusCode = result.status();
    }

    @Then("the status code received is OK")
    public void the_status_code_received_is_OK() {
        assertEquals(OK, statusCode);
    }

    @When("I create a new destination with the following values")
    public void iCreateANewDestinationWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        throw new cucumber.api.PendingException();
    }
}
