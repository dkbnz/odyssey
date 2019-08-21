package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import models.destinations.Destination;
import play.mvc.Http;
import play.mvc.Result;
import repositories.TripRepository;
import repositories.destinations.DestinationRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.HttpVerbs.PATCH;
import static play.test.Helpers.*;

public class TripTestSteps {

    /**
     * Singleton class which stores generally used variables
     */
    private TestContext testContext = TestContext.getInstance();


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


    private DestinationRepository destinationRepository =
            testContext.getApplication().injector().instanceOf(DestinationRepository.class);
    private TripRepository tripRepository =
            testContext.getApplication().injector().instanceOf(TripRepository.class);


    /**
     * Sends a request to create a trip with the given trip data.
     *
     * @param docString     the string containing the trip data.
     * @throws IOException  if the docString is formatted incorrectly.
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


    /**
     * Converts a string value of a trip to a Json format using an ObjectMapper.
     *
     * @param docString     the string value of the trip to be converted.
     * @return              the Json string of a trip.
     * @throws IOException  if an error happens in conversion.
     */
    private JsonNode convertTripStringToJson(String docString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(docString);
    }


    /**
     * Gets the trip name from a given data table.
     *
     * @param dataTable     the data table containing the trip name.
     * @return              a string of the trip name.
     */
    private String getTripNameFromDataTable(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        return list.get(0).get("Name");
    }


    /**
     * Creates a new trip with the data given for the currently logged in user.
     *
     * @param trip              the string containing the trip data.
     * @throws IOException      if the trip is formatted incorrectly.
     */
    @Given("I own the trip with the following data")
    public void iOwnTheTripWithTheFollowingData(String trip) throws IOException {
        createTripGenericRequest(trip, testContext.getLoggedInId());
    }


    /**
     * Creates a new trip with the data given for another user.
     *
     * @param trip              the string containing the trip data.
     * @throws IOException      if the trip is formatted incorrectly.
     */
    @Given("I do not own the trip with the following data")
    public void iDoNotOwnTheTripWithTheFollowingName(String trip) throws IOException {
        String ownerId = "3";
        createTripGenericRequest(trip, ownerId);
    }


    /**
     * Creates a trip with the given data and for the given user.
     *
     * @param tripData          the data for the new trip to create.
     * @param ownerId           the id for the owner of the new trip.
     * @throws IOException      if the trip is formatted incorrectly.
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
     *
     * @param dataTable     the data table containing the trip name to delete.
     */
    @When("I delete the trip with the following name")
    public void iDeleteTheTripWithTheFollowingName(io.cucumber.datatable.DataTable dataTable) {
        String tripName = getTripNameFromDataTable(dataTable);
        Integer tripId = getTripIdFromTripName(tripName).intValue();
        deleteTripRequest(tripId);
    }


    /**
     * Gets the trip id for the trip with the given name.
     *
     * @param tripName  the name of the trip to get the id for.
     * @return          the id of the trip.
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
     *
     * @param tripId    the id for the trip to be deleted.
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
     *
     * @param tripId        the id of the trip being edited.
     * @param tripData      the Json body of the edit request in the form of a string.
     * @throws IOException  the exception thrown.
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


    @When("^I change the trip, \"(.*)\" to contain the following data$")
    public void iChangeTheTripToContainTheFollowingData(String tripName, String tripData) throws IOException {
        Integer tripId = getTripIdFromTripName(tripName).intValue();
        editTripRequest(tripId, tripData);
    }


    @Then("^the destination with id (\\d+) ownership changes to the user with id (\\d+)$")
    public void theDestinationOwnershipChangesToTheGlobalAdminWithId(Integer destinationId, Integer profileId) {
        Destination destination = destinationRepository.findById(destinationId.longValue());
        assertEquals(profileId.longValue(), destination.getOwner().getId().longValue());
    }

}
