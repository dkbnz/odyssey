package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import models.destinations.Destination;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import repositories.destinations.DestinationRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static play.mvc.Http.HttpVerbs.PATCH;
import static play.test.Helpers.*;

public class TripTestSteps {

    /**
     * Singleton class which stores generally used variables
     */
    private TestContext testContext = TestContext.getInstance();


    /**
     * New instance of the general test steps.
     */
    private GeneralTestSteps generalTestSteps = new GeneralTestSteps();


    /**
     * Indicates the authorized parameter when sending a request.
     */
    private static final String AUTHORIZED = "authorized";


    /**
     * The trips endpoint uri.
     */
    private static final String TRIPS_URI = "/v1/trips/";


    /**
     * The trips count endpoint uri.
     */
    private static final String TRIPS_COUNT_URI = "/count";


    /**
     * String to add the equals character (=) to build a query string.
     */
    private static final String EQUALS = "=";


    /**
     * String to add the ampersand character (&) to build a query string.
     */
    private static final String AND = "&";


    /**
     * String to add the question mark character (?) to build a query string.
     */
    private static final String QUESTION_MARK = "?";


    /**
     * The static Json variable keys for a trip.
     */
    private static final String NAME = "trip_name";
    private static final String TRIP_DESTINATIONS = "trip_destinations";
    private static final String DESTINATION = "destination_id";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";


    private static final String NAME_STRING = "Name";
    private static final String DESTINATION_STRING = "Destination";
    private static final String START_DATE_STRING = "Start Date";
    private static final String END_DATE_STRING = "End Date";

    private static final String NEW_TRIP_ID = "newTripId";

    private static final String PAGE_FUTURE = "pageFuture";
    private static final String PAGE_PAST = "pagePast";
    private static final String PAGE_SIZE_FUTURE = "pageSizeFuture";
    private static final String PAGE_SIZE_PAST = "pageSizePast";
    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_PAGE_SIZE = "50";


    private DestinationRepository destinationRepository =
            testContext.getApplication().injector().instanceOf(DestinationRepository.class);

    /**
     * Used to construct the trip and it's destinations when creating and editing trips.
     */
    private ObjectNode tripJson;
    private List<ObjectNode> tripDestinations = new ArrayList<>();

    /**
     * The id of the newly created trip.
     */
    private Long tripId;


    /**
     * Converts a given data table of trip values to a Json node object of this trip.
     *
     * @param dataTable     the data table containing values of a trip.
     */
    private void convertDataTableTripJson(io.cucumber.datatable.DataTable dataTable, int index) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String name       = list.get(index).get(NAME_STRING);

        //Add values to a JsonNode
        ObjectMapper mapper = new ObjectMapper();
        tripJson = mapper.createObjectNode();
        tripJson.put(NAME, name);
    }


    /**
     * Converts a given data table of trip destination values to a Json node object of this trip.
     *
     * @param dataTable     the data table containing values of a trip destination.
     */
    private void convertDataTableToTripDestinationJson(io.cucumber.datatable.DataTable dataTable, int index) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String destination         = list.get(index).get(DESTINATION_STRING);
        String startDate           = list.get(index).get(START_DATE_STRING);
        String endDate             = list.get(index).get(END_DATE_STRING);

        // If there is already destinations in the trip, then we need the dates to be spaced out.
        int dateBuffer = 0;
        if (!tripDestinations.isEmpty()) {
            dateBuffer += 10;
        }

        if (startDate.isEmpty()) {
            startDate = generalTestSteps.getDateBuffer(true, dateBuffer);
        }

        if (endDate.isEmpty()) {
            endDate = generalTestSteps.getDateBuffer(false, dateBuffer);
        }


        //Add values to a JsonNode
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put(DESTINATION, destination);
        json.put(START_DATE, startDate);
        json.put(END_DATE, endDate);
        tripDestinations.add(json);
    }


    /**
     * Sends the backend request to create a trip.
     *
     * @param json  the given Json body containing a trip.
     */
    private void createTripRequest(JsonNode json) throws IOException {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .bodyJson(json)
                .uri(TRIPS_URI + testContext.getTargetId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        tripDestinations.clear();

        if (testContext.getStatusCode() < 400) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(Helpers.contentAsString(result));
            tripId = Long.parseLong(actualObj.get(NEW_TRIP_ID).toString());
        }
    }


    /**
     * Creates a trip for the target user.
     *
     * @param json  the given Json body containing a trip.
     */
    private void createGenericTripRequest(JsonNode json) throws IOException {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .session(AUTHORIZED, testContext.getTargetId())
                .bodyJson(json)
                .uri(TRIPS_URI + testContext.getTargetId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        tripDestinations.clear();

        if (testContext.getStatusCode() < 400) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(Helpers.contentAsString(result));
            tripId = Long.parseLong(actualObj.get(NEW_TRIP_ID).toString());
        }
    }


    /**
     * Sends an edit trip request for a given trip id. The session is set to the currently logged in user.
     *
     * @param json          the given Json body containing the changed trip values.
     */
    private void editTripRequest(JsonNode json) {
        Http.RequestBuilder request = fakeRequest()
                .method(PATCH)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .bodyJson(json)
                .uri(TRIPS_URI + tripId.toString());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    /**
     * Sends a request for the trips for a given user. The session is set to the currently logged in user.
     */
    private void requestTrips() {
        String queryString =
                QUESTION_MARK + PAGE_FUTURE + EQUALS + DEFAULT_PAGE
                + AND + PAGE_SIZE_FUTURE + EQUALS + DEFAULT_PAGE_SIZE
                + AND + PAGE_PAST + EQUALS + DEFAULT_PAGE
                + AND + PAGE_SIZE_PAST + EQUALS + DEFAULT_PAGE_SIZE;
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(TRIPS_URI + testContext.getTargetId() + queryString);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    /**
     * Sends to retrieve the total number of trips a given user has.
     */
    private void requestTripCount() {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(TRIPS_URI + testContext.getTargetId() + TRIPS_COUNT_URI);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    @Given("the trip exists with the following values")
    public void theTripExistsWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(testContext.getLoggedInId());
        iCreateANewTripWithTheFollowingValues(dataTable);
    }


    @Given("^the trip exists with the following values for user with id (.*)$")
    public void theTripExistsWithTheFollowingValuesForUserWithId(String userId, io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(userId);
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            convertDataTableTripJson(dataTable, i);
        }
    }


    @Given("^I create the trip with the following values for user with id (.*)$")
    public void iCreateTheTripWithTheFollowingValuesForUserWithId(String userId, io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(userId);
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            convertDataTableTripJson(dataTable, i);
        }
    }


    @When("I create a new trip with the following values")
    public void iCreateANewTripWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(testContext.getLoggedInId());
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            convertDataTableTripJson(dataTable, i);
        }
    }


    @When("the trip has a destination with the following values")
    public void theTripHasADestinationWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            convertDataTableToTripDestinationJson(dataTable, i);
            tripJson.putArray(TRIP_DESTINATIONS).addAll(tripDestinations);
        }
    }


    @When("I create the trip")
    public void iCreateTheTrip() {
        try {
            createTripRequest(tripJson);
        } catch (IOException e) {
            fail();
        }
    }


    @When("the trip exists")
    public void theTripExists() {
        try {
            createGenericTripRequest(tripJson);
        } catch (IOException e) {
            fail();
        }
    }


    @When("I delete the trip")
    public void whenIDeleteTheTrip() {
        Http.RequestBuilder request = fakeRequest()
                .method(DELETE)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(TRIPS_URI + tripId.toString());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    @When("I change the trip to the following trip")
    public void iChangeTheTripToTheFollowingTrip(io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(testContext.getLoggedInId());
        iCreateANewTripWithTheFollowingValues(dataTable);
    }


    @When("I update the trip")
    public void iUpdateTheTrip() {
        editTripRequest(tripJson);
    }


    @When("^I request all trips for user with id (.*)$")
    public void requestAllTripsForUserWithId(String requestedUserId) {
        testContext.setTargetId(requestedUserId);
        requestTrips();
    }


    @When("^I request the number of trips for user with id (.*)$")
    public void requestTheNumberOfTripsForUserWithId(String requestedUserId) {
        testContext.setTargetId(requestedUserId);
        requestTripCount();
    }


    @Then("^the destination with id (\\d+) ownership changes to the user with id (\\d+)$")
    public void theDestinationOwnershipChangesToTheGlobalAdminWithId(Integer destinationId, Integer profileId) {
        Destination destination = destinationRepository.findById(destinationId.longValue());
        assertNotNull(destination);
        assertEquals(profileId.longValue(), destination.getOwner().getId().longValue());
    }
}
