package steps;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import models.TravellerType;
import models.destinations.Destination;
import org.junit.Assert;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import repositories.destinations.DestinationRepository;
import repositories.destinations.TravellerTypeRepository;
import java.util.ArrayList;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;

public class DestinationTravellerTypeTestSteps {
    /**
     * Singleton class which stores generally used variables
     */
    private TestContext testContext = TestContext.getInstance();

    /**
     * Test file with test steps common over different scenarios
     */
    private GeneralSteps generalSteps;


    /**
     * Authorisation token for sessions
     */
    private static final String AUTHORIZED = "authorized";


    /**
     * The login endpoint uri.
     */
    private static final String TRAVELLER_TYPE_PROPOSE_URI = "/travellerTypes/propose";


    /**
     * The destinations propose endpoint uri.
     */
    private static final String DESTINATIONS_GET_PROPOSE_URI = "/v1/destinations/proposals";


    /**
     * The destination endpoint uri.
     */
    private static final String DESTINATION_URI = "/v1/destinations/";


    /**
     * The logout endpoint uri.
     */
    private static final String TRAVELLER_TYPES = "/travellerTypes";


    /**
     * A destination that exists in the database.
     */
    private static final String DESTINATION_ID = "119";


    /**
     * Repository to access the destinations in the running application.
     */
    private DestinationRepository destinationRepository = testContext.getApplication().injector().instanceOf(DestinationRepository.class);


    /**
     * Repository to access the destinations in the running application.
     */
    private TravellerTypeRepository travellerTypeRepository = testContext.getApplication().injector().instanceOf(TravellerTypeRepository.class);


    @Given("There is a destination with one traveller type to add")
    public void thereIsADestinationWithOneTravellerTypeToAdd() {
        List<TravellerType> travellerTypeList = new ArrayList<>();
        travellerTypeList.add(travellerTypeRepository.findAll().get(0));
        JsonNode json = Json.toJson(travellerTypeList);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .bodyJson(json)
                .uri(DESTINATION_URI + DESTINATION_ID + TRAVELLER_TYPE_PROPOSE_URI);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

        testContext.setResponseBody(Helpers.contentAsString(result));

    }


    @Given("There is a destination with one traveller type assigned")
    public void thereIsADestinationWithOneTravellerTypeAssigned() {
        List<TravellerType> travellerTypeList = new ArrayList<>();
        travellerTypeList.add(travellerTypeRepository.findAll().get(1));
        JsonNode json = Json.toJson(travellerTypeList);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .bodyJson(json)
                .uri(DESTINATION_URI + DESTINATION_ID + TRAVELLER_TYPES);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

        testContext.setResponseBody(Helpers.contentAsString(result));
    }


    @Given("There is a destination with one traveller type to remove")
    public void thereIsADestinationWithOneTravellerTypeToRemove() {
        List<TravellerType> travellerTypeList = new ArrayList<>();
        JsonNode json = Json.toJson(travellerTypeList);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .bodyJson(json)
                .uri(DESTINATION_URI + 119 + TRAVELLER_TYPE_PROPOSE_URI);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

        testContext.setResponseBody(Helpers.contentAsString(result));
    }


    @When("A request for proposed destinations is sent")
    public void aRequestForProposedDestinationsIsSent() {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(DESTINATIONS_GET_PROPOSE_URI);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

        testContext.setResponseBody(Helpers.contentAsString(result));
    }


    @Then("There is a destination to update")
    public void thereIsDestinationToUpdate() throws IOException {
        Integer receivedAmount = new ObjectMapper().readTree(testContext.getResponseBody()).size();
        Assert.assertEquals(1, receivedAmount.intValue());
    }


    @And("^I (.*)own destination with id (\\d+) and it is (.*)$")
    public void iOwnDestinationWithIdAndItIs(String ownOrNot, int destinationId, String publicOrPrivate) {
        Destination destinationOfInterest = destinationRepository.findById(Long.valueOf(destinationId));

        // Ensure we can find a destination
        Assert.assertNotNull(destinationOfInterest);

        // If we are wanting public destination then get public should be true too. false otherwise
        assertEquals(
                destinationOfInterest.getPublic(),
                publicOrPrivate.equals("public")
        );

        // If we own the destination, logged in should be equal. If not equal and we want it to be then throw assertion
        assertEquals(
                destinationOfInterest.getOwner().getId().toString().equals(testContext.getLoggedInId()),
                ownOrNot.equals("")
        );
    }


    @When("^I (.*) the following traveller types for destination id (.*)$")
    public void iTheFollowingTravellerTypesForDestinationId(String suggestOrSet,
                                                            String destinationId, DataTable travellerTypeIds) {

        List<TravellerType> travellerTypeList = new ArrayList<>();

        for (String travellerTypeId : travellerTypeIds.asList()) {
            travellerTypeList.add(
                    travellerTypeRepository.findById(
                            Long.valueOf(travellerTypeId)
                    )
            );
        }

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .bodyJson(Json.toJson(travellerTypeList))
                .uri(DESTINATION_URI + destinationId + TRAVELLER_TYPES
                        + (suggestOrSet.equals("suggest") ? "/propose" : ""));
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }
}