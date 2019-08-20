package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.ebean.ExpressionList;
import models.profiles.TravellerType;
import models.destinations.Destination;
import models.photos.PersonalPhoto;
import models.objectives.Objective;
import models.trips.Trip;
import org.junit.*;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import repositories.trips.TripRepository;
import repositories.destinations.DestinationRepository;
import repositories.destinations.DestinationTypeRepository;
import repositories.destinations.TravellerTypeRepository;
import repositories.objectives.ObjectiveRepository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;
import static play.mvc.Http.Status.CREATED;
import static play.test.Helpers.*;
import static util.QueryUtil.queryComparator;

public class DestinationTestSteps {

    /**
     * Singleton class which stores generally used variables
     */
    private TestContext testContext = TestContext.getInstance();


    /**
     * The ID of the destination to be updated.
     */
    private Long destinationId;


    /**
     * The id of the newly created destination, used for destination usage retrieval.
     */
    private Long createdDestinationId;


    /**
     * The size of the list of trips received.
     */
    private int tripCountReceived;


    /**
     * User who owns the objective
     */
    private String targetUserId;


    /**
     * The destination endpoint uri.
     */
    private static final String DESTINATION_URI = "/v1/destinations";


    /**
     * The destination photos endpoint uri.
     */
    private static final String DESTINATION_PHOTO_URI = "/v1/destinationPhotos/";


    /**
     * The traveller types endpoint uri.
     */
    private static final String TRAVELLER_TYPES = "/travellerTypes";


    /**
     * The trips endpoint uri.
     */
    private static final String TRIPS_URI = "/v1/trips/";


    /**
     * The endpoint uri for checking which trips a destination is used in.
     */
    private static final String DESTINATION_CHECK_URI = "/checkDuplicates";


    /**
     * The endpoint uri for checking duplicate destinations.
     */
    private static final String DESTINATION_EDIT_CHECK = "/v1/destinationsCheckEdit";


    /**
     * The objective uri.
     */
    private static final String TREASURE_HUNT_URI = "/v1/objectives";


    /**
     * Authorisation token for sessions
     */
    private static final String AUTHORIZED = "authorized";


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


    private static final String DISTRICT_STRING = "District";
    private static final String LATITUDE_STRING = "Latitude";
    private static final String LONGITUDE_STRING = "Longitude";
    private static final String COUNTRY_STRING = "Country";
    private static final String TYPE_STRING = "Type";
    private static final String NAME_STRING = "Name";
    private static final String IS_PUBLIC_STRING = "is_public";
    private static final String TRIP_COUNT = "trip_count";
    private static final String PHOTO_COUNT = "photo_count";
    private static final String DESTINATION_COUNT = "destination_count";
    private static final String MATCHING_TRIPS = "matching_trips";
    private static final String TRIP_NAME_FIELD = "name";


    private static final String DISTRICT = "district";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String COUNTRY = "country";
    private static final String TYPE = "type_id";
    private static final String NAME = "name";
    private static final String IS_PUBLIC = "is_public";

    private static final String RIDDLE_STRING = "Riddle";
    private static final String START_DATE_STRING = "Start Date";
    private static final String END_DATE_STRING = "End Date";
    private static final String OWNER_STRING = "Owner";
    private static final String DESTINATION = "destination";
    private static final String RIDDLE = "riddle";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String ID = "id";

    private static final int START_DATE_BUFFER = -10;
    private static final int END_DATE_BUFFER = 10;

    private DestinationRepository destinationRepository = testContext.getApplication().injector().instanceOf(DestinationRepository.class);
    private TravellerTypeRepository travellerTypeRepository = testContext.getApplication().injector().instanceOf(TravellerTypeRepository.class);
    private DestinationTypeRepository destinationTypeRepository = testContext.getApplication().injector().instanceOf(DestinationTypeRepository.class);
    private ObjectiveRepository objectiveRepository = testContext.getApplication().injector().instanceOf(ObjectiveRepository.class);
    private TripRepository tripRepository = testContext.getApplication().injector().instanceOf(TripRepository.class);


    /**
     * Sends a request to create a destination with values from the given json node.
     *
     * @param json      a JsonNode containing the values for a new destination object.
     */
    private void createDestinationRequest(JsonNode json) {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(DESTINATION_URI + "/" + testContext.getTargetId())
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

        if (testContext.getStatusCode() < 400) {
            createdDestinationId = Long.parseLong(Helpers.contentAsString(result));
        }
    }


    /**
     * Sends a request to search for a destination with the given query string.
     *
     * @param query     A String containing the query parameters for the search.
     */
    private void searchDestinationsRequest(String query) {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(DESTINATION_URI + query);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

        testContext.setResponseBody(Helpers.contentAsString(result));
    }


    /**
     * Sends a put request to the application to edit the values of the destination.
     *
     * @param json  the date that the destination will be updated with.
     */
    private void editDestinationRequest(JsonNode json) {
        Http.RequestBuilder request = fakeRequest()
                .method(PUT)
                .bodyJson(json)
                .uri(DESTINATION_URI + "/" + destinationId)
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    /**
     * Sends a request to delete a destination with the given destination id.
     *
     * @param destinationId     the destination id as a Long.
     */
    private void deleteDestinationRequest(Long destinationId) {
        Http.RequestBuilder request = fakeRequest()
                .method(DELETE)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(DESTINATION_URI + "/" + destinationId);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    /**
     * Creates a Json object from the given trip values.
     *
     * @param tripName  the name of the trip.
     * @return          a Json object containing the trip information.
     */
    private JsonNode createNewTripJson(String tripName) {
        //Add values to a JsonNode
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put("trip_name", tripName);
        ArrayNode destinationsNode = json.putArray("trip_destinations");

        ObjectNode destinationNode1 = destinationsNode.addObject(); //Adding destinations to trip
        destinationNode1.put("destination_id", "1155");
        destinationNode1.put("start_date", "1990-12-12");
        destinationNode1.put("end_date", "1991-12-12");

        ObjectNode destinationNode2 = destinationsNode.addObject(); //Adding destinations to trip
        destinationNode2.put("destination_id", "567");
        destinationNode2.put("start_date", "1992-12-12");
        destinationNode2.put("end_date", "1993-12-12");

        ObjectNode destinationNode3 = destinationsNode.addObject(); //Adding destinations to trip
        destinationNode3.put("destination_id", destinationId.toString());
        destinationNode3.put("start_date", "1994-12-12");
        destinationNode3.put("end_date", "1995-12-12");

        return json;
    }


    /**
     * Sends a request to create a objective with values from the given Json node.
     *
     * @param json      a JsonNode containing the values for a new objective object.
     */
    private void createObjectiveRequest(JsonNode json) {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(TREASURE_HUNT_URI + "/" + targetUserId)
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        assertEquals(CREATED, testContext.getStatusCode());
    }


    /**
     * Converts a trip Json body from the data table to a Json.
     *
     * @param docString     the string body of a Json from the data table.
     * @return              the string body as a Json object.
     * @throws IOException  error if parsing to Json fails.
     */
    private JsonNode convertTripStringToJson(String docString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(docString);
    }


    /**
     * Adds a photo with the given photoId to a destination with the given destinationId.
     *
     * @param photoId           id of the photo to be added.
     * @param destinationId     id of the destination to be added.
     */
    private void addDestinationPhoto(int photoId, Long destinationId) {
        JsonNode json = createDestinationPhotoJson(photoId);
        Http.RequestBuilder request =
                Helpers.fakeRequest()
                        .uri(DESTINATION_PHOTO_URI + destinationId)
                        .method(POST)
                        .bodyJson(json)
                        .session(AUTHORIZED, testContext.getLoggedInId());

        Result addDestinationPhotoResult = route(testContext.getApplication(), request);
        testContext.setStatusCode(addDestinationPhotoResult.status());
    }


    /**
     * Creates a Json ObjectNode to be used for the destination photo.
     *
     * @param photoId the photoId of the photo to be added to the Json node.
     * @return        the Json ObjectNode for the new destination photo.
     */
    private JsonNode createDestinationPhotoJson(int photoId) {
        // complex json
        ObjectMapper mapper = new ObjectMapper();

        //Add values to a JsonNode
        ObjectNode json = mapper.createObjectNode();

        json.put("id", photoId);

        return json;
    }


    /**
     * Converts a given data table of destination values to a json node object of this destination.
     *
     * @param dataTable     the data table containing values of a destination.
     * @return              a JsonNode of a destination containing information from the data table.
     */
    private JsonNode convertDataTableToDestinationJson(io.cucumber.datatable.DataTable dataTable, int index) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String name         = list.get(index).get(NAME_STRING);
        String type         = list.get(index).get(TYPE_STRING);
        String district     = list.get(index).get(DISTRICT_STRING);
        String latitude     = list.get(index).get(LATITUDE_STRING);
        String longitude    = list.get(index).get(LONGITUDE_STRING);
        String country      = list.get(index).get(COUNTRY_STRING);
        String is_public    = list.get(index).get(IS_PUBLIC_STRING);

        //Test destinations are public by default
        Boolean publicity = (is_public == null ||
                !is_public.equalsIgnoreCase("false"));

        //Add values to a JsonNode
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put(NAME, name);
        json.put(TYPE, type);
        json.put(LATITUDE, latitude);
        json.put(LONGITUDE, longitude);
        json.put(DISTRICT, district);
        json.put(COUNTRY, country);
        json.put(IS_PUBLIC, publicity);

        return json;
    }


    /**
     * Returns a string that is either empty or containing the given value.
     * Checks if the given field matches the search field. If so, returns the given value to search.
     *
     * @param searchField       the search field name as defined by the application.
     * @param givenField        the field name given to the test.
     * @param givenValue        the value to search for if the search and given fields match.
     * @return                  a string that contains the given value or an empty string.
     */
    private String getValue(String searchField, String givenField, String givenValue) {
        return searchField.equals(givenField) ? givenValue : "";
    }


    /**
     * Converts the values of a cucumber DataTable to a Json node object, using only the information that was given.
     *
     * @param dataTable     the cucumber DataTable holding the values to be converted to Json.
     * @return              the Json representation of the DataTable
     */
    private JsonNode convertDataTableToEditDestination(io.cucumber.datatable.DataTable dataTable) {
        int valueIndex = 0;
        Destination editDestination = destinationRepository.findById(destinationId);
        if (editDestination == null) {
            editDestination = new Destination();
        }
        List<Map<String, String>> valueList = dataTable.asMaps(String.class, String.class);
        Map<String, String> valueMap = valueList.get(valueIndex);

        for (Map.Entry<String, String> entry : valueMap.entrySet()) {
            String value = entry.getValue();

            switch (entry.getKey()) {
                case TYPE_STRING:
                    editDestination.setType(destinationTypeRepository.findById(Long.valueOf(value)));
                    break;
                case DISTRICT_STRING:
                    editDestination.setDistrict(value);
                    break;
                case LATITUDE_STRING:
                    editDestination.setLatitude(Double.valueOf(value));
                    break;
                case LONGITUDE_STRING:
                    editDestination.setLongitude(Double.valueOf(value));
                    break;
                case COUNTRY_STRING:
                    editDestination.setCountry(value);
                    break;
                case IS_PUBLIC_STRING:
                    editDestination.setPublic(Boolean.valueOf(value));
                    break;
            }
        }

        return Json.toJson(editDestination);
    }

    /**
     * Converts a given data table of destination values to a json node object of this destination.
     *
     * @param dataTable     the data table containing values of a destination.
     * @return              a JsonNode of a destination containing information from the data table.
     */
    private JsonNode convertDataTableToObjectiveJson(io.cucumber.datatable.DataTable dataTable, int index) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String riddle                  = list.get(index).get(RIDDLE_STRING);
        String startDate               = list.get(index).get(START_DATE_STRING);
        String endDate                 = list.get(index).get(END_DATE_STRING);


        targetUserId = list.get(index).get(OWNER_STRING);

        if (startDate.equals("")) {
            startDate = getObjectiveDateBuffer(true);
        }

        if (endDate.equals("")) {
            endDate = getObjectiveDateBuffer(false);
        }

        //Add values to a JsonNode
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        ObjectNode jsonDestination = json.putObject(DESTINATION);

        if(!(destinationId == null)) {
            jsonDestination.put(ID,  destinationId.intValue());
        }

        json.put(RIDDLE, riddle);
        json.put(START_DATE, startDate);
        json.put(END_DATE, endDate);

        return json;
    }


    /**
     * Creates a new datetime object from today's date. This is then used to ensure our tests will always pass, as a
     * buffer is used to make the start date before today and the end date after today.
     *
     * @param isStartDate   boolean value to determine if the date being changed the start or the end date.
     * @return              the start or end date, which is modified by the necessary date buffer.
     */
    private String getObjectiveDateBuffer(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();

        if (isStartDate) {
            calendar.add(Calendar.DATE, START_DATE_BUFFER);
        }
        calendar.add(Calendar.DATE, END_DATE_BUFFER);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:MM:ssZ");
        return sdf.format(calendar.getTime());
    }


    /**
     * Gets a destination id based on values in the data table.
     *
     * @param dataTable         The data table containing values of a destination.
     * @return                  Destination id as a Long, or null if no destination exists.
     */
    private Long getDestinationId(io.cucumber.datatable.DataTable dataTable) {
        List<Destination> destinations = getDestinationList(dataTable);

        Destination destination = destinations.size() > 0 ? destinations.get(destinations.size() - 1) : null;

        return destination == null ? null : destination.getId();
    }


    /**
     * Uses a ExpressionLst query to the destination repository to find the destinations that match the values in
     * the cucumber DataTable.
     *
     * @param dataTable the cucumber DataTable containing the search query values.
     * @return          a list of the matching destinations.
     */
    private List<Destination> getDestinationList(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        int index = 0;
        String name         = list.get(index).get(NAME_STRING);
        String type         = list.get(index).get(TYPE_STRING);
        String district     = list.get(index).get(DISTRICT_STRING);
        String latitude     = list.get(index).get(LATITUDE_STRING);
        String longitude    = list.get(index).get(LONGITUDE_STRING);
        String country      = list.get(index).get(COUNTRY_STRING);
        String publicity    = list.get(index).get(IS_PUBLIC_STRING);

        // Build search query to find destination
        ExpressionList<Destination> expressionList =
                destinationRepository.getExpressionList()
                        .ilike(NAME, queryComparator(name))
                        .eq(TYPE, type)
                        .eq(LATITUDE, latitude)
                        .eq(LONGITUDE, longitude)
                        .ilike(DISTRICT, queryComparator(district))
                        .ilike(COUNTRY, queryComparator(country))
                        .eq(IS_PUBLIC, publicity);

        return expressionList.findList();
    }


    /**
     * Gets a value associated with a given field from the given data table.
     *
     * @param field         The title of the data table column to extract.
     * @param dataTable     The data table containing the value to extract.
     * @return              A String of the value extracted.
     */
    private String getValueFromDataTable(String field, io.cucumber.datatable.DataTable dataTable) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        return list.get(0).get(field);
    }


    /**
     * Creates a query string for the search destination request.
     * Builds this query string with empty values except for the given search value associated
     * with the given search field.
     *
     * @param searchField       The search field name for the given value.
     * @param searchValue       The given search value for associated field.
     * @return                  The complete query string.
     */
    private String createSearchDestinationQueryString(String searchField, String searchValue) {
        String name = getValue(NAME, searchField, searchValue);
        String type = getValue(TYPE, searchField, searchValue);
        String latitude = getValue(LATITUDE, searchField, searchValue);
        String longitude = getValue(LONGITUDE, searchField, searchValue);
        String district = getValue(DISTRICT, searchField, searchValue);
        String country = getValue(COUNTRY, searchField, searchValue);
        String publicity = getValue(IS_PUBLIC, searchField, searchValue);


        StringBuilder stringBuilder = new StringBuilder()
                .append(QUESTION_MARK)

                .append(NAME)
                .append(EQUALS)
                .append(name)

                .append(AND)
                .append(TYPE)
                .append(EQUALS)
                .append(type)

                .append(AND)
                .append(LATITUDE)
                .append(EQUALS)
                .append(latitude)

                .append(AND)
                .append(LONGITUDE)
                .append(EQUALS)
                .append(longitude)

                .append(AND)
                .append(DISTRICT)
                .append(EQUALS)
                .append(district)

                .append(AND)
                .append(COUNTRY)
                .append(EQUALS)
                .append(country)

                .append(AND)
                .append(IS_PUBLIC)
                .append(EQUALS)
                .append(publicity);

        return stringBuilder.toString();
    }


    /**
     * Gets an id list of the photos in the given destination
     *
     * @param destination   the destination to check the photos in.
     * @return              a list of the photo ids.
     */
    private List<String> getPhotoIds(Destination destination) {
        List<String> photoIds = new ArrayList<>();
        for (PersonalPhoto photo : destination.getPhotoGallery()) {
            photoIds.add(photo.getId().toString());
        }

        return photoIds;
    }


    /**
     * Retrieves all the names of the trips that are associated with the destination.
     *
     * @return              a list of trip names.
     * @throws IOException  in case of an error.
     */
    private List<String> getTripNames() throws IOException {
        List<String> names = new ArrayList<>();

        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(DESTINATION_URI+ "/"  + destinationId + DESTINATION_CHECK_URI);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

        testContext.setResponseBody(Helpers.contentAsString(result));
        JsonNode matchingTrips = new ObjectMapper().readTree(testContext.getResponseBody()).get(MATCHING_TRIPS);

        for (JsonNode trip : matchingTrips) {
            names.add(trip.get(TRIP_NAME_FIELD).asText());
        }

        return names;
    }


    @Given("^the destination is used in trip \"(.*)\"$")
    public void theDestinationIsUsedInTrip(String tripName) {
        JsonNode json = createNewTripJson(tripName);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .bodyJson(json)
                .uri(TRIPS_URI + testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    @Given("the destination exists in a objective with the following values")
    public void theDestinationExistsInAObjectiveWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode json = convertDataTableToObjectiveJson(dataTable, i);
            createObjectiveRequest(json);
        }
    }


    @Given("a destination has been created with the following values")
    public void aDestinationHasBeenCreatedWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        String destinationName = getValueFromDataTable("Name", dataTable);
        String query = createSearchDestinationQueryString(NAME, destinationName);
        searchDestinationsRequest(query);
        assertTrue(testContext.getResponseBody().contains(destinationName));
    }


    @Given("^the destination has a photo with id (\\d+)$")
    public void theDestinationHasAPhotoWithId(Integer photoId) {
        addDestinationPhoto(photoId, destinationId);
    }


    @Given("^the destination has a (.*) traveller type with id (.*)$")
    public void theDestinationHasTheTravellerTypeWithId(String proposedOrNot, String travellerTypeId) {
        List<TravellerType> travellerTypeList = new ArrayList<>();
        travellerTypeList.add(travellerTypeRepository.findById(Long.parseLong(travellerTypeId)));

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .bodyJson(Json.toJson(travellerTypeList))
                .uri(DESTINATION_URI + "/" + destinationId + TRAVELLER_TYPES
                        + (proposedOrNot.equals("proposed") ? "/propose" : ""));
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    /**
     * Creates one or many destinations under the ownership of the given user.
     * @param userId the user who will be in ownership of the destination(s).
     * @param dataTable the values of the destinations to be added.
     */
    @Given("^a destination already exists for user (\\d+) with the following values$")
    public void aDestinationAlreadyExistsForUserWithTheFollowingValues(Integer userId,
                                                                       io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(userId.toString());

        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode json = convertDataTableToDestinationJson(dataTable, i);
            createDestinationRequest(json);

            // Saves the last created destination id
            Long id = getDestinationId(dataTable);
            Assert.assertNotNull(id);
            destinationId = id;
        }
    }


    /**
     * Sends a request to create a new destination with valid values given in the data table to
     * ensure a destination already exists in the database.
     *
     * @param dataTable     The data table containing values to create the new destination.
     */
    @And("a destination already exists with the following values")
    public void aDestinationExistsWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(testContext.getLoggedInId());
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode json = convertDataTableToDestinationJson(dataTable, i);
            createDestinationRequest(json);

            // Saves the last created destination id
            destinationId = getDestinationId(dataTable);
        }
    }


    /**
     * Sends a request to get all destinations.
     */
    @When("I send a GET request to the destinations endpoint")
    public void iSendAGetRequestToTheDestinationsEndpoint() {
        testContext.setTargetId(testContext.getLoggedInId());
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(DESTINATION_URI);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    /**
     * Sends a request to create a trip with the given trip data.
     *
     * @param docString     The string containing the trip data.
     * @throws IOException  If the docString is formatted incorrectly.
     */
    @When("the following json body containing a trip is sent:")
    public void createTripRequest(String docString) throws IOException {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .session(AUTHORIZED,testContext.getLoggedInId())
                .bodyJson(convertTripStringToJson(docString))
                .uri(TRIPS_URI + testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    /**
     * Sends a request to create a new destination with values given in the data table.
     *
     * @param dataTable     the data table containing values to create the new destination.
     */
    @When("I create a new destination with the following values")
    public void iCreateANewDestinationWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(testContext.getLoggedInId());
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode json = convertDataTableToDestinationJson(dataTable, i);
            createDestinationRequest(json);
        }
    }


    @When("I create a new destination with the following values for another user")
    public void iCreateANewDestinationWithTheFollowingValuesForAnotherUser(io.cucumber.datatable.DataTable dataTable) {
        testContext.setTargetId(testContext.getLoggedInId());
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode json = convertDataTableToDestinationJson(dataTable, i);
            createDestinationRequest(json);
        }
    }


    /**
     * Sends a request to search for a destination with name value given in the data table.
     *
     * @param dataTable     The data table containing the field, name, and value for a destination search.
     */
    @When("I search for a destination with name")
    public void iSearchForADestinationWithName(io.cucumber.datatable.DataTable dataTable) {
        // Set up the search fields with given name
        String value = getValueFromDataTable("Name", dataTable);
        String query = createSearchDestinationQueryString(NAME, value);

        //Send search destinations request
        searchDestinationsRequest(query);
    }


    @When("I search for a destination with district")
    public void iSearchForADestinationWithDistrict(io.cucumber.datatable.DataTable dataTable) {
        // Set up the search fields with given district
        String value = getValueFromDataTable(DISTRICT_STRING, dataTable);
        String query = createSearchDestinationQueryString(DISTRICT, value);

        //Send search destinations request
        searchDestinationsRequest(query);
    }


    @When("I search for a destination with latitude")
    public void iSearchForADestinationWithLatitude(io.cucumber.datatable.DataTable dataTable) {
        // Set up the search fields with given district
        String value = getValueFromDataTable(LATITUDE_STRING, dataTable);
        String query = createSearchDestinationQueryString(LATITUDE, value);

        //Send search destinations request
        searchDestinationsRequest(query);
    }


    @When("I search for all destinations")
    public void iSearchForAllDestinations() {
        String query = createSearchDestinationQueryString("", "");

        //Send search destinations request
        searchDestinationsRequest(query);
    }


    @When("^I search for all destinations by user (\\d+)$")
    public void iSearchForAllDestinationsByUser(Integer userId) {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(DESTINATION_URI + "/" + userId)
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

        testContext.setResponseBody(Helpers.contentAsString(result));
    }


    @When("I attempt to delete the destination")
    public void iAttemptToDeleteTheDestination() {
        // Send the delete request
        deleteDestinationRequest(destinationId);
    }


    @When("^I attempt to delete the destination with id (\\d+)$")
    public void iAttemptToDeleteTheDestinationWithId(Integer destinationId) {
        deleteDestinationRequest(destinationId.longValue());
    }


    @When("^I add a photo with id (\\d+) to the destination$")
    public void iAddAPhotoWithIdToTheDestination(Integer photoId) {
        addDestinationPhoto(photoId, destinationId);
    }


    /**
     * Takes the information provided in the feature, and sends a put request to edit the destination.
     * @param dataTable the data specified in the test feature.
     */
    @When("I attempt to edit the destination using the following values")
    public void iAttemptToEditTheDestinationUsingTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        JsonNode editValues = convertDataTableToEditDestination(dataTable);
        editDestinationRequest(editValues);
    }


    /**
     * Takes the information provided in by the cucumber feature, and sends a put request to edit the destination specified
     * by `destinationId`.
     * @param destinationId the destination to be edited.
     * @param dataTable the information used to edit the destination.
     */
    @When("^I attempt to edit destination (-?\\d+) using the following values$")
    public void iAttemptToEditDestinationUsingTheFollowingValues(Integer destinationId, io.cucumber.datatable.DataTable dataTable) {
        this.destinationId = destinationId.longValue();
        JsonNode editValues = convertDataTableToEditDestination(dataTable);
        editDestinationRequest(editValues);
    }


    @When("^I request the destination usage for destination with id (\\d+)$")
    public void iRequestTheDestinationUsageForDestinationWithId(Integer destinationId) {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(DESTINATION_URI+ "/"  + destinationId + DESTINATION_CHECK_URI)
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        testContext.setResponseBody(Helpers.contentAsString(result));
    }


    @When("^I add a photo with id (\\d+) to an existing destination with id (\\d+)$")
    public void iAddAPhotoToASpecifiedDestination(Integer photoId, Integer destinationId) {
        JsonNode json = createDestinationPhotoJson(photoId);
        Http.RequestBuilder request =
                Helpers.fakeRequest()
                        .uri(DESTINATION_PHOTO_URI + destinationId)
                        .method(POST)
                        .bodyJson(json)
                        .session(AUTHORIZED, testContext.getLoggedInId());

        Result addDestinationPhotoResult = route(testContext.getApplication(), request);
        testContext.setStatusCode(addDestinationPhotoResult.status());
    }

    @When("^I change the value of the destination name to \'(.*)\' and I request the destination usage for edited destination$")
    public void iRequestTheDestinationUsageForEditedDestinationWithId(String name) {
        Destination destination = destinationRepository.findById(createdDestinationId);
        destination.setName(name);

        Http.RequestBuilder request =
                Helpers.fakeRequest()
                        .uri(DESTINATION_EDIT_CHECK)
                        .method(POST)
                        .bodyJson(Json.toJson(destination))
                        .session(AUTHORIZED, testContext.getLoggedInId());

        Result result= route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        testContext.setResponseBody(Helpers.contentAsString(result));
    }

    @Then("^the number of destinations received is at least (\\d+)$")
    public void theNumberOfDestinationsReceivedIs(int destinationsReceived) throws IOException {
        int responseCount = new ObjectMapper().readTree(testContext.getResponseBody()).get(DESTINATION_COUNT).asInt();
        assertTrue(responseCount >= destinationsReceived);
    }



    @Then("there is only one destination with the following values")
    public void thereIsOnlyOneDestinationWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        List<Destination> destinations = getDestinationList(dataTable);
        Assert.assertEquals(1, destinations.size());
    }


    @Then("^the trip count is (\\d+)$")
    public void theTripCountIs(int tripCountExpected) throws IOException {
        int tripCount = new ObjectMapper().readTree(testContext.getResponseBody()).get(TRIP_COUNT).asInt();
        tripCountReceived = new ObjectMapper().readTree(testContext.getResponseBody()).get(MATCHING_TRIPS).size();
        Assert.assertEquals(tripCountExpected, tripCount);
    }


    @Then("^the number of trips received is (\\d+)$")
    public void theNumberOfTripsReceivedIs(int tripListSize) {
        Assert.assertEquals(tripCountReceived, tripListSize);
    }


    @Then("^the photo count is (\\d+)$")
    public void thePhotoCountIs(int photoCountExpected) throws IOException {
        int photoCount = new ObjectMapper().readTree(testContext.getResponseBody()).get(PHOTO_COUNT).asInt();
        Assert.assertEquals(photoCountExpected, photoCount);
    }


    /**
     * Checks if the response body from the previous query contains at least one destination with a given name.
     * @param dataTable     The data table containing the name of the destination that should exist in the
     *                      response.
     */
    @Then("the response contains at least one destination with name")
    public void theResponseContainsAtLeastOneDestinationWithName(io.cucumber.datatable.DataTable dataTable) throws IOException {
        String value = getValueFromDataTable(NAME_STRING, dataTable);
        String arrNode = new ObjectMapper().readTree(testContext.getResponseBody()).get(0).get(NAME).asText();

        Assert.assertEquals(value, arrNode);
    }


    @Then("the response contains at least one destination with district")
    public void theResponseContainsAtLeastOneDestinationWithDistrict(io.cucumber.datatable.DataTable dataTable) throws IOException {
        String value = getValueFromDataTable(DISTRICT_STRING, dataTable);
        String arrNode = new ObjectMapper().readTree(testContext.getResponseBody()).get(0).get(DISTRICT).asText();

        Assert.assertEquals(value, arrNode);
    }


    @Then("the response contains at least one destination with latitude")
    public void theResponseContainsAtLeastOneDestinationWithLatitude(io.cucumber.datatable.DataTable dataTable) throws IOException {
        String value = getValueFromDataTable(LATITUDE_STRING, dataTable);
        String arrNode = new ObjectMapper().readTree(testContext.getResponseBody()).get(0).get(LATITUDE).asText();

        Assert.assertEquals(value, arrNode);
    }


    @Then("the response is empty")
    public void theResponseIsEmpty() throws IOException {
        JsonNode arrNode = new ObjectMapper().readTree(testContext.getResponseBody());

        Assert.assertEquals(0, arrNode.size());
    }


    @Then("the response contains only own or public destinations")
    public void theResponseContainsOnlyOwnOrPublicDestinations() throws IOException {
        JsonNode arrNode = new ObjectMapper().readTree(testContext.getResponseBody());
        for (int i = 0 ; i < arrNode.size() ; i++) {
            assertTrue(arrNode.get(i).get("public").asBoolean() ||
                    arrNode.get(i).get("owner").get("id").asText() == testContext.getLoggedInId());
        }
    }


    @Then("^the response contains only destinations owned by the user with id (\\d+)$")
    public void theResponseContainsOnlyDestinationsOwnedByTheUserWithId(Integer id) throws IOException {
        Long userId = id.longValue();
        JsonNode arrNode = new ObjectMapper().readTree(testContext.getResponseBody());
        Long ownerId;
        for (int i = 0 ; i < arrNode.size() ; i++) {
            ownerId = destinationRepository.findById(arrNode.get(i).get("id").asLong()).getOwner().getId();  //Gets owner id of destination
            assertNotNull(ownerId);
            assertEquals(userId, ownerId);
        }
    }


    /**
     * Tests that the destination's photos contain the given photos.
     *
     * @param dataTable     ids of the photos expected.
     */
    @Then("the destination will have photos with the following ids")
    public void theDestinationWillHavePhotosWithTheFollowingIds(io.cucumber.datatable.DataTable dataTable) {
        Destination destination = destinationRepository.findById(destinationId);
        assertNotNull(destination);

        List<String> photoIds = getPhotoIds(destination);
        List<String> expectedIds = new ArrayList<>(dataTable.asList());
        expectedIds = expectedIds.subList(1, expectedIds.size());

        Collections.sort(expectedIds);
        Collections.sort(photoIds);

        assertEquals(expectedIds, photoIds);
    }


    @Then("^the destination will have the following number of objectives (\\d+)$")
    public void theDestinationWillHaveTheFollowingNumberOfObjectives(Integer expectedSize) {
        Destination destination = destinationRepository.findById(destinationId);

        List<Objective> objectives = objectiveRepository.findAllUsing(destination);

        assertEquals(expectedSize.longValue(), objectives.size());
    }


    /**
     * Checks that the active destination is used in the given trips.
     *
     * @param dataTable     names of the trips the destination is expected to work in.
     * @throws IOException  in case of an error.
     */
    @Then("the destination will be used in the following trips")
    public void theDestinationWillBeUsedInTheFollowingTrips(io.cucumber.datatable.DataTable dataTable) throws IOException {
        List<String> names = getTripNames();
        List<String> expectedNames = new ArrayList<>(dataTable.asList());
        expectedNames = expectedNames.subList(1, expectedNames.size());

        Collections.sort(expectedNames);
        Collections.sort(names);

        assertEquals(expectedNames, names);
    }


    @Then("the destination will have the following traveller types")
    public void theDestinationWillHaveTheFollowingTravellerTypes(io.cucumber.datatable.DataTable dataTable) {
        List<String> expectedTravellerTypes = new ArrayList<>(dataTable.asList());
        expectedTravellerTypes = expectedTravellerTypes.subList(1, expectedTravellerTypes.size());

        List<String> foundTravellerTypes = new ArrayList<>();
        for (TravellerType travellerType : destinationRepository.findById(destinationId).getTravellerTypes()) {
            foundTravellerTypes.add(travellerType.getId().toString());
        }
        assertTrue(expectedTravellerTypes.containsAll(foundTravellerTypes));
    }


    @Then("the destination will have the following proposed traveller types to add")
    public void theDestinationWillHaveTheFollowingProposedTravellerTypesToAdd(io.cucumber.datatable.DataTable dataTable) {
        List<String> expectedTravellerTypesToAdd = new ArrayList<>(dataTable.asList());
        expectedTravellerTypesToAdd = expectedTravellerTypesToAdd.subList(1, expectedTravellerTypesToAdd.size());

        List<String> foundTravellerTypesToAdd = new ArrayList<>();
        for (TravellerType travellerType : destinationRepository.findById(destinationId).getProposedTravellerTypesAdd()) {
            foundTravellerTypesToAdd.add(travellerType.getId().toString());
        }

        assertTrue(foundTravellerTypesToAdd.containsAll(expectedTravellerTypesToAdd));
    }


    @Then("^the trip with name \"(.*)\" is deleted$")
    public void theTripWithNameIsDeleted(String tripName) {
        List<Trip> trips = tripRepository.getExpressionList().ilike(TRIP_NAME_FIELD, queryComparator(tripName)).findList();
        Assert.assertEquals(0, trips.size());
    }


    /**
     * Tests that the owner of the destination is the specified user.
     *
     * @param userId    id of the expected owner.
     */
    @Then("^the owner is user (\\d+)$")
    public void theOwnerIsUser(Integer userId) {
        Destination destination = destinationRepository.findById(destinationId);
        Long expectedId = userId.longValue();
        Long ownerId = destination.getOwner().getId();
        assertNotNull(ownerId);
        assertEquals(expectedId, ownerId);
    }
}
