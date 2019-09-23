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
import repositories.trips.TripRepository;
import repositories.destinations.DestinationRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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


    /**
     * The static Json variable keys for a trip.
     */
    private static final String NAME = "trip_name";
    private static final String ID = "id";
    private static final String TRIP_DESTINATIONS = "trip_destinations";
    private static final String DESTINATION = "destination_id";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";


    private static final String NAME_STRING = "Name";
    private static final String DESTINATION_STRING = "Destination";
    private static final String START_DATE_STRING = "Start Date";
    private static final String END_DATE_STRING = "End Date";

    private static final String NEW_TRIP_ID = "newTripId";


    private DestinationRepository destinationRepository =
            testContext.getApplication().injector().instanceOf(DestinationRepository.class);
    private TripRepository tripRepository =
            testContext.getApplication().injector().instanceOf(TripRepository.class);


    private ObjectNode tripJson;
    private List<ObjectNode> tripDestinations = new ArrayList<>();

    private Long tripId;


//    /**
//     * Sends a request to create a trip with the given trip data.
//     *
//     * @param docString     the string containing the trip data.
//     * @throws IOException  if the docString is formatted incorrectly.
//     */
//    @When("the following json containing a trip is sent:")
//    public void theFollowingJsonContainingATripIsSent(String docString) throws IOException {
//        Http.RequestBuilder request = fakeRequest()
//                .method(POST)
//                .session(AUTHORIZED, testContext.getLoggedInId())
//                .bodyJson(convertTripStringToJson(docString))
//                .uri(TRIPS_URI + testContext.getLoggedInId());
//        Result result = route(testContext.getApplication(), request);
//        testContext.setStatusCode(result.status());
//    }


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
                .uri(TRIPS_URI + testContext.getLoggedInId());
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


//    /**
//     * Converts a string value of a trip to a Json format using an ObjectMapper.
//     *
//     * @param docString     the string value of the trip to be converted.
//     * @return              the Json string of a trip.
//     * @throws IOException  if an error happens in conversion.
//     */
//    private JsonNode convertTripStringToJson(String docString) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.readTree(docString);
//    }
//
//
//    /**
//     * Gets the trip name from a given data table.
//     *
//     * @param dataTable     the data table containing the trip name.
//     * @return              a string of the trip name.
//     */
//    private String getTripNameFromDataTable(io.cucumber.datatable.DataTable dataTable) {
//        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
//        return list.get(0).get("Name");
//    }


    @Given("the trip exists with the following values")
    public void theTripExistsWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(testContext.getLoggedInId());
        iCreateANewTripWithTheFollowingValues(dataTable);
    }


    @Given("^the trip exists with the following values for user with id (.*)$")
    public void theTripExistsWithTheFollowingValuesForAnotherUser(String userId, io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(userId);
        iCreateANewTripWithTheFollowingValues(dataTable);
    }




//    /**
//     * Creates a new trip with the data given for another user.
//     *
//     * @param trip              the string containing the trip data.
//     * @throws IOException      if the trip is formatted incorrectly.
//     */
//    @Given("I do not own the trip with the following data")
//    public void iDoNotOwnTheTripWithTheFollowingName(String trip) throws IOException {
//        String ownerId = "3";
//        createTripGenericRequest(trip, ownerId);
//    }


//    /**
//     * Creates a trip with the given data and for the given user.
//     *
//     * @param tripData          the data for the new trip to create.
//     * @param ownerId           the id for the owner of the new trip.
//     * @throws IOException      if the trip is formatted incorrectly.
//     */
//    private void createTripGenericRequest(String tripData, String ownerId) throws IOException {
//        Http.RequestBuilder request = fakeRequest()
//                .method(POST)
//                .session(AUTHORIZED, ownerId)
//                .bodyJson(convertTripStringToJson(tripData))
//                .uri(TRIPS_URI + ownerId);
//        route(testContext.getApplication(), request);
//    }


//    /**
//     * Sends a request to delete the trip with the name specified in the data table.
//     *
//     * @param dataTable     the data table containing the trip name to delete.
//     */
//    @When("I delete the trip with the following name")
//    public void iDeleteTheTripWithTheFollowingName(io.cucumber.datatable.DataTable dataTable) {
//        String tripName = getTripNameFromDataTable(dataTable);
//        Integer tripId = getTripIdFromTripName(tripName).intValue();
//        deleteTripRequest(tripId);
//    }


    @When("I create a new trip with the following values")
    public void iCreateANewTripWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
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
    public void ICreateTheTrip() {
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


//    /**
//     * Gets the trip id for the trip with the given name.
//     *
//     * @param tripName  the name of the trip to get the id for.
//     * @return          the id of the trip.
//     */
//    private Long getTripIdFromTripName(String tripName) {
//        return tripRepository
//                .getExpressionList()
//                .select(TRIP_ID_FIELD)
//                .where().eq(TRIP_NAME_FIELD, tripName)
//                .findSingleAttribute();
//    }

    /**
     * Sends a delete trip request for a given trip id. The session is set to the currently logged in user.
     */
    @When("I delete the trip")
    public void whenIDeleteTheTrip() {
        Http.RequestBuilder request = fakeRequest()
                .method(DELETE)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(TRIPS_URI + tripId.toString());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
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


    @When("I change the trip to the following trip")
    public void iChangeTheTripToTheFollowingTrip(io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(testContext.getLoggedInId());
        iCreateANewTripWithTheFollowingValues(dataTable);
    }


    @When("I update the trip")
    public void iUpdateTheTrip() {
        editTripRequest(tripJson);
    }


    @Then("^the destination with id (\\d+) ownership changes to the user with id (\\d+)$")
    public void theDestinationOwnershipChangesToTheGlobalAdminWithId(Integer destinationId, Integer profileId) {
        Destination destination = destinationRepository.findById(destinationId.longValue());
        assertEquals(profileId.longValue(), destination.getOwner().getId().longValue());
    }
}
