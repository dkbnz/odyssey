package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import org.junit.Assert;
import org.springframework.beans.BeansException;
import play.Application;
import play.db.Database;
import play.db.evolutions.Evolutions;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;

public class ProfileTestSteps {

    @Inject
    Application application;
    protected Database database;

    private Http.RequestBuilder request;

    private int statusCode;
    private int loginStatusCode;
    private List<Map<String,String>> profile;
    private static final String PROFILES_URI = "/v1/profiles";
    private static final String TRAVTYPES_URI = "/v1/travtypes";
    private static final String NATIONALITIES_URI = "/v1/nationalities";
    private static final String LOGIN_URI = "/v1/login";
    private static final String LOGOUT_URI = "/v1/logout";

    private static final String VALID_USERNAME = "admin@travelea.com";
    private static final String VALID_PASSWORD = "admin1";



    @Before
    public void setUp() {
        Map<String, String> configuration = new HashMap<>();
        configuration.put("db.default.driver", "org.h2.Driver");
        configuration.put("db.default.url", "jdbc:h2:mem:testDB;MODE=MYSQL;");
        configuration.put("play.evolutions.db.default.enabled", "true");
        configuration.put("play.evolutions.autoApply", "false");

        //Set up the fake application to use the in memory database config
        application = fakeApplication(configuration);
        Helpers.start(application);

        database = application.injector().instanceOf(Database.class);

        Evolutions.applyEvolutions(
                database,
                Evolutions.fromClassLoader(
                        getClass().getClassLoader(),
                        "test/evolutions/default/"
                )
        );
    }


    @After
    public void tearDown() {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .uri(LOGOUT_URI);
        route(application, request);
        Helpers.stop(application);
    }


    @Given("the application is running")
    public void theApplicationIsRunning() throws BeansException {
        Assert.assertTrue(application.isTest());
    }

    @And("I have logged in")
    public void i_have_logged_in() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.createObjectNode();

        ((ObjectNode) json).put("username", VALID_USERNAME);
        ((ObjectNode) json).put("password", VALID_PASSWORD);


        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(LOGIN_URI);
        Result loginResult = route(application, request);
        loginStatusCode = loginResult.status();
        assertEquals(OK, loginStatusCode);
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


    @When("I send a GET request to the \\/travtypes endpoint")
    public void iSendAGETRequestToTheTravtypesEndpoint() throws BeansException {
        request = fakeRequest()
                .method(GET)
                .uri(TRAVTYPES_URI);
        Result result = route(application, request);

        String json = Helpers.contentAsString(result);

        statusCode = result.status();
        Assert.assertEquals("", json);
    }

    @When("I send a GET request to the \\/nationalities endpoint")
    public void iSendAGETRequestToTheNationalitiesEndpoint() {
        request = fakeRequest()
                .method(GET)
                .uri(NATIONALITIES_URI);
        Result result = route(application, request);
        statusCode = result.status(); //Assert.assertTrue(request instanceof Http.RequestBuilder);
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
        request = fakeRequest()
                .method(GET)
                .uri(PROFILES_URI);
        //Response responses = route(application, request);
        //String restultJSON = request.body();
        //Assert.assertEquals("", restultJSON);

    }


    @When("A user attempts to create a profile with the following fields:")
    public void aUserAttemptsToCreateAProfileWithTheFollowingFields(io.cucumber.datatable.DataTable dataTable) {
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
