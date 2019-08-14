package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import models.destinations.Destination;
import org.junit.Assert;
import play.Application;
import play.db.Database;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.HttpVerbs.PATCH;
import static play.mvc.Http.Status.CREATED;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import play.db.evolutions.Evolutions;
import repositories.TripRepository;
import repositories.destinations.DestinationRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static play.test.Helpers.fakeApplication;

public class TripTestSteps {

    /**
     * Singleton class which stores generally used variables
     */
    private TestContext testContext;

    /**
     * Test file with test steps common over different scenarios
     */
    private GeneralSteps generalSteps;



    private static final String AUTHORIZED = "authorized";



    /**
     * The trips endpoint uri.
     */
    private static final String TRIPS_URI = "/v1/trips/";



    /**
     * The field for the trip id.
     */
    private static final String TRIP_ID_FIELD = "id";


    /**
     * The field for the trip name.
     */
    private static final String TRIP_NAME_FIELD = "name";

    private DestinationRepository destinationRepository;
    private TripRepository tripRepository;


    @Before
    public void setUp() {

        testContext = TestContext.getInstance();

        generalSteps = new GeneralSteps();
        generalSteps.setUp();

        destinationRepository = testContext.getApplication().injector().instanceOf(DestinationRepository.class);
        tripRepository = testContext.getApplication().injector().instanceOf(TripRepository.class);

    }


    /**
     * Runs after each test scenario.
     * Sends a logout request.
     * Cleans up the database by cleaning up evolutions and shutting it down.
     * Stops running the fake application.
     */
    @After
    public void tearDown() {
        generalSteps.tearDown();
    }


    /**
     * Sends a request to create a trip with the given trip data.
     * @param docString     The string containing the trip data.
     * @throws IOException  If the docString is formatted incorrectly.
     */
    @When("the following json containing a trip is sent:")
    public void theFollowingJsonContainingATripIsSent(String docString) throws IOException {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .bodyJson(convertTripStringToJson(docString))
                .uri(TRIPS_URI + testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    private JsonNode convertTripStringToJson(String docString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(docString);
    }


    /**
     * Gets the trip name from a given data table.
     * @param dataTable     The data table containing the trip name.
     * @return              A string of the trip name.
     */
    private String getTripNameFromDataTable(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        return list.get(0).get("Name");
    }


    /**
     * Creates a new trip with the data given for the currently logged in user.
     * @param trip              The string containing the trip data.
     * @throws IOException      If the trip is formatted incorrectly.
     */
    @Given("I own the trip with the following data")
    public void iOwnTheTripWithTheFollowingData(String trip) throws IOException {
        createTripGenericRequest(trip, testContext.getLoggedInId());
    }


    /**
     * Creates a new trip with the data given for another user.
     * @param trip              The string containing the trip data.
     * @throws IOException      If the trip is formatted incorrectly.
     */
    @Given("I do not own the trip with the following data")
    public void iDoNotOwnTheTripWithTheFollowingName(String trip) throws IOException {
        String ownerId = "3";
        createTripGenericRequest(trip, ownerId);
    }


    /**
     * Creates a trip with the given data and for the given user.
     * @param tripData          The data for the new trip to create.
     * @param ownerId           The id for the owner of the new trip.
     * @throws IOException      If the trip is formatted incorrectly.
     */
    private void createTripGenericRequest(String tripData, String ownerId) throws IOException {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .session(AUTHORIZED, ownerId)
                .bodyJson(convertTripStringToJson(tripData))
                .uri(TRIPS_URI + ownerId);
        route(testContext.getApplication(), request);
    }


    /**
     * Sends a request to delete the trip with the name specified in the data table.
     * @param dataTable     The data table containing the trip name to delete.
     */
    @When("I delete the trip with the following name")
    public void iDeleteTheTripWithTheFollowingName(io.cucumber.datatable.DataTable dataTable) {
        String tripName = getTripNameFromDataTable(dataTable);
        Integer tripId = getTripIdFromTripName(tripName).intValue();
        deleteTripRequest(tripId);
    }


    /**
     * Gets the trip id for the trip with the given name.
     * @param tripName  The name of the trip to get the id for.
     * @return          The id of the trip.
     */
    private Long getTripIdFromTripName(String tripName) {
        return tripRepository
                .getExpressionList()
                .select(TRIP_ID_FIELD)
                .where().eq(TRIP_NAME_FIELD, tripName)
                .findSingleAttribute();
    }


    /**
     * Sends a delete trip request for a given trip id. The session is set to the currently logged in user.
     * @param tripId    The id for the trip to be deleted.
     */
    private void deleteTripRequest(Integer tripId) {
        Http.RequestBuilder request = fakeRequest()
                .method(DELETE)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(TRIPS_URI + tripId.toString());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    /**
     * Sends an edit trip request for a given trip id. The session is set to the currently logged in user.
     * @param tripId        The id of the trip being edited.
     * @param tripData      The json body of the edit request in the form of a string.
     * @throws IOException  The exception thrown
     */
    private void editTripRequest(Integer tripId, String tripData) throws IOException {
        Http.RequestBuilder request = fakeRequest()
                .method(PATCH)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .bodyJson(convertTripStringToJson(tripData))
                .uri(TRIPS_URI + tripId.toString());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }

    @When("I change the trip, {string} to contain the following data")
    public void iChangeTheTripToContainTheFollowingData(String tripName, String tripData) throws IOException {
        Integer tripId = getTripIdFromTripName(tripName).intValue();
        editTripRequest(tripId, tripData);
    }


    @Then("the destination with id {int} ownership changes to the user with id {int}")
    public void theDestinationOwnershipChangesToTheGlobalAdminWithId(Integer destinationId, Integer profileId) {
        Destination destination = destinationRepository.findById(destinationId.longValue());
        assertEquals(profileId.longValue(), destination.getOwner().getId().longValue());
    }

}
