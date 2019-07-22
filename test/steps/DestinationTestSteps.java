package steps;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.Before;
import io.ebean.ExpressionList;
import models.destinations.Destination;
import models.photos.PersonalPhoto;
import models.trips.Trip;
import org.junit.*;
import play.Application;
import play.db.Database;

import play.db.evolutions.Evolutions;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import repositories.destinations.DestinationRepository;

import java.io.IOException;
import java.util.*;

import static controllers.destinations.DestinationController.*;
import static org.junit.Assert.*;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.CREATED;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;
import static util.QueryUtil.queryComparator;

public class DestinationTestSteps {


    /**
     * Variable to hold the status code of the result.
     */
    private int statusCode;


    /**
     * The Json body of the response.
     */
    private String responseBody;


    /**
     * The ID of the destination to be updated.
     */
    private Long destinationId;


    /**
     * The size of the list of trips recieved.
     */
    private int tripCountRecieved;


    /**
     * The destination endpoint uri.
     */
    private static final String DESTINATION_URI = "/v1/destinations";


    /**
     * The destination photos endpoint uri.
     */
    private static final String DESTINATION_PHOTO_URI = "/v1/destinationPhotos/";


    /**
     * The trips endpoint uri.
     */
    private static final String TRIPS_URI = "/v1/trips/";


    /**
     * The endpoint uri for checking which trips a destination is used in.
     */
    private static final String DESTINATION_CHECK_URI = "/v1/destinationCheck/";


    /**
     * Authorisation token for sessions
     */
    private static final String AUTHORIZED = "authorized";


    /**
     * The login endpoint uri.
     */
    private static final String LOGIN_URI = "/v1/login";


    /**
     * The logout endpoint uri.
     */
    private static final String LOGOUT_URI = "/v1/logout";


    /**
     * Valid login credentials for an admin user.
     */
    private static final String ADMIN_USERNAME = "admin@travelea.com";
    private static final String ADMIN_AUTHPASS = "admin1";
    private static final String ADMIN_ID = "1";


    /**
     * Valid login credentials for a regular user.
     */
    private static final String REG_USERNAME = "guestUser@travelea.com";
    private static final String REG_AUTHPASS = "guest123";
    private static final String REG_ID = "2";


    /**
     * Valid login credentials for an alternate user.
     */
    private static final String ALT_USERNAME = "testuser1@email.com";
    private static final String ALT_AUTHPASS = "guest123";
    private static final String ALT_ID = "3";


    /**
     * Currently logged-in user
     */
    private String loggedInId;


    /**
     * Target user for destination changes
     */
    private String targetId;

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
    private static final String TRIP_COUNT = "tripCount";
    private static final String PHOTO_COUNT = "photoCount";
    private static final String MATCHING_TRIPS = "matchingTrips";

    /**
     * The fake application.
     */

    private Application application;


    /**
     * Database instance for the fake application.
     */
    private Database database;

    /**
     * Runs before each test scenario.
     * Sets up a fake application for testing.
     * Applies configuration settings to use an in memory database for the fake application.
     * Starts the application.
     * Calls apply evolutions to set up the database state.
     */
    @Before
    public void setUp() {
        Map<String, String> configuration = new HashMap<>();
        configuration.put("play.db.config", "db");
        configuration.put("play.db.default", "default");
        configuration.put("db.default.driver", "org.h2.Driver");
        configuration.put("db.default.url", "jdbc:h2:mem:testDBDestination;MODE=MYSQL;");
        configuration.put("ebean.default", "models.*");
        configuration.put("play.evolutions.db.default.enabled", "true");
        configuration.put("play.evolutions.autoApply", "false");

        //Set up the fake application to use the in memory database config
        application = fakeApplication(configuration);

        database = application.injector().instanceOf(Database.class);
        applyEvolutions();

        Helpers.start(application);
    }


    /**
     * Runs after each test scenario.
     * Sends a logout request.
     * Cleans up the database by cleaning up evolutions and shutting it down.
     * Stops running the fake application.
     */
    @After
    public void tearDown() {
        logoutRequest();
        cleanEvolutions();
        database.shutdown();
        Helpers.stop(application);
    }


    /**
     * Applies down evolutions to the database from the test/evolutions/default directory.
     *
     * This drops tables and data from the database.
     */
    private void applyEvolutions() {
        Evolutions.applyEvolutions(
                database,
                Evolutions.fromClassLoader(
                        getClass().getClassLoader(),
                        "test/"
                )
        );
    }


    /**
     * Applies up evolutions to the database from the test/evolutions/default directory.
     *
     * This populates the database with necessary tables and values.
     */
    private void cleanEvolutions() {
        Evolutions.cleanupEvolutions(database);
    }


    /**
     * Sends a fake request to the application to login.
     * @param username      The string of the username to complete the login with.
     * @param password      The string of the password to complete the login with.
     */
    private void loginRequest(String username, String password) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put("username", username);
        json.put("password", password);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(LOGIN_URI);
        Result loginResult = route(application, request);

        statusCode = loginResult.status();
    }


    /**
     * Sends a fake request to the application to logout.
     */
    private void logoutRequest() {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .uri(LOGOUT_URI);
        route(application, request);
        loggedInId = null;
    }


    /**
     * Sends a request to create a destination with values from the given json node.
     * @param json      A JsonNode containing the values for a new destination object.
     */
    private void createDestinationRequest(JsonNode json) {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(DESTINATION_URI + "/" + targetId)
                .session(AUTHORIZED, loggedInId);
        Result result = route(application, request);
        statusCode = result.status();
    }


    /**
     * Sends a request to search for a destination with the given query string.
     * @param query     A String containing the query parameters for the search.
     */
    private void searchDestinationsRequest(String query) {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, loggedInId)
                .uri(DESTINATION_URI + query);
        Result result = route(application, request);
        statusCode = result.status();

        responseBody = Helpers.contentAsString(result);
    }

    /**
     * Sends a put request to the application to edit the values of the destination.
     * @param json the date that the destination will be updated with.
     */
    private void editDestinationRequest(JsonNode json) {
        Http.RequestBuilder request = fakeRequest()
                .method(PUT)
                .bodyJson(json)
                .uri(DESTINATION_URI + "/" + destinationId)
                .session(AUTHORIZED, loggedInId);
        Result result = route(application, request);
        statusCode = result.status();
    }


    /**
     * Sends a request to delete a destination with the given destination id.
     * @param destinationId     The destination id as a Long.
     */
    private void deleteDestinationRequest(Long destinationId) {
        Http.RequestBuilder request = fakeRequest()
                .method(DELETE)
                .session(AUTHORIZED, loggedInId)
                .uri(DESTINATION_URI + "/" + destinationId);
        Result result = route(application, request);
        statusCode = result.status();
    }


    /**
     * Asserts the fake application is in test mode.
     */
    @Given("I have a running application")
    public void iHaveARunningApplication() {
        Assert.assertTrue(application.isTest());
    }


    /**
     * Attempts to send a log in request with user credentials from constants VALID_USERNAME
     * and VALID_AUTHPASS.
     *
     * Asserts the login was successful with a status code of OK (200).
     */
    @Given("I am logged in")
    public void iAmLoggedIn() {
        loginRequest(REG_USERNAME, REG_AUTHPASS);
        assertEquals(OK, statusCode);
        loggedInId = REG_ID;
    }


    /**
     * Attempts to send a log in request with user credentials from constants ALT_USERNAME
     * and ALT_AUTHPASS.
     *
     * Asserts the login was successful with a status code of OK (200).
     */
    @Given("I am logged in as an alternate user")
    public void iAmLoggedInAsAnAlternateUser() {
        loginRequest(ALT_USERNAME, ALT_AUTHPASS);
        assertEquals(OK, statusCode);
        loggedInId = ALT_ID;
    }

    /**
     * Attempts to send a log in request with user credentials from constants ADMIN_USERNAME
     * and ADMIN_AUTHPASS.
     *
     * Asserts the login was successful with a status code of OK (200).
     */
    @Given("I am logged in as an admin user")
    public void iAmLoggedInAsAnAdminUser() {
        loginRequest(ADMIN_USERNAME, ADMIN_AUTHPASS);
        assertEquals(OK, statusCode);
        loggedInId = ADMIN_ID;
    }


    /**
     * Sends a logout request to the system
     *
     * Asserts the value of loggedInId is null.
     */
    @Given("I am not logged in")
    public void iAmNotLoggedIn() {
        logoutRequest();
        assertNull(loggedInId);
    }


    /**
     * Sends a request to create a new destination with valid values given in the data table to
     * ensure a destination already exists in the database.
     * @param dataTable     The data table containing values to create the new destination.
     */
    @And("a destination already exists with the following values")
    public void aDestinationExistsWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        targetId = loggedInId;
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode json = convertDataTableToDestinationJson(dataTable, i);
            createDestinationRequest(json);

            // Saves the last created destination id
            Long id = getDestinationId(dataTable);
            destinationId = id;
        }
    }

    @Given("the destination is used in trip {string}")
    public void theDestinationIsUsedInTrip(String tripName) {
        JsonNode json = createNewTripJson(tripName);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .session(AUTHORIZED, loggedInId)
                .bodyJson(json)
                .uri(TRIPS_URI + loggedInId);
        Result result = route(application, request);
        statusCode = result.status();
    }

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
     * Creates one or many destinations under the ownership of the given user.
     * @param userId the user who will be in ownership of the destination(s).
     * @param dataTable the values of the destinations to be added.
     */
    @Given("a destination already exists for user {int} with the following values")
    public void aDestinationAlreadyExistsForUserWithTheFollowingValues(Integer userId, io.cucumber.datatable.DataTable dataTable) throws IOException {
        targetId = userId.toString();

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
     * Queries the database for the destination described by the values in the dataTable.
     * @param dataTable the information containing the destination(s) select in the database.
     */
    @Given("a destination has been created with the following values")
    public void aDestinationHasBeenCreatedWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        String destinationName = getValueFromDataTable("Name", dataTable);
        String query = createSearchDestinationQueryString(NAME, destinationName);
        searchDestinationsRequest(query);
        assertTrue(responseBody.contains(destinationName));
    }


    @Given("the destination has a photo with id {int}")
    public void theDestinationHasAPhotoWithId(Integer photoId) {
        addDestinationPhoto(photoId, destinationId);
    }


    /**
     * Adds a photo with the given photoId to a destination with the given destinationId.
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
                        .session(AUTHORIZED, loggedInId);

        Result addDestinationPhotoResult = route(application, request);
        statusCode = addDestinationPhotoResult.status();
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
     * Sends a request to get all destinations.
     */
    @When("I send a GET request to the destinations endpoint")
    public void iSendAGetRequestToTheDestinationsEndpoint() {
        targetId = loggedInId;
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, loggedInId)
                .uri(DESTINATION_URI);
        Result result = route(application, request);
        statusCode = result.status();
    }


    /**
     * Sends a request to create a new destination with values given in the data table.
     * @param dataTable     The data table containing values to create the new destination.
     */
    @When("I create a new destination with the following values")
    public void iCreateANewDestinationWithTheFollowingValues(io.cucumber.datatable.DataTable dataTable) {
        targetId = loggedInId;
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode json = convertDataTableToDestinationJson(dataTable, i);
            createDestinationRequest(json);
        }
    }


    @When("I create a new destination with the following values for another user")
    public void iCreateANewDestinationWithTheFollowingValuesForAnotherUser(io.cucumber.datatable.DataTable dataTable) {
        targetId = loggedInId;
        for (int i = 0 ; i < dataTable.height() -1 ; i++) {
            JsonNode json = convertDataTableToDestinationJson(dataTable, i);
            createDestinationRequest(json);
        }
    }


    /**
     * Converts a given data table of destination values to a json node object of this destination.
     * @param dataTable     The data table containing values of a destination.
     * @return              A JsonNode of a destination containing information from the data table.
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
     * Sends a request to search for a destination with name value given in the data table.
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


    @When("I search for all destinations by user {int}")
    public void iSearchForAllDestinationsByUser(Integer userId) {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(DESTINATION_URI + "/" + userId)
                .session(AUTHORIZED, loggedInId);
        Result result = route(application, request);
        statusCode = result.status();

        responseBody = Helpers.contentAsString(result);
    }


    @When("I attempt to delete the destination")
    public void iAttemptToDeleteTheDestination() {
        // Send the delete request
        deleteDestinationRequest(destinationId);
    }


    @When("I attempt to delete the destination with id {int}")
    public void iAttemptToDeleteTheDestinationWithId(Integer destinationId) {
        deleteDestinationRequest(destinationId.longValue());
    }


    /**
     * Gets a destination id based on values in the data table.
     * @param dataTable         The data table containing values of a destination.
     * @return                  Destination id as a Long, or null if no destination exists.
     */
    private Long getDestinationId(io.cucumber.datatable.DataTable dataTable) {
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
                Destination.find.query().where()
                .ilike(NAME, queryComparator(name))
                .eq(TYPE, type)
                .eq(LATITUDE, latitude)
                .eq(LONGITUDE, longitude)
                .ilike(DISTRICT, queryComparator(district))
                .ilike(COUNTRY, queryComparator(country))
                .eq(IS_PUBLIC, publicity);

        List<Destination> destinations = expressionList.findList();
        Destination destination = destinations.size() > 0 ? destinations.get(destinations.size() - 1) : null;

        return destination == null ? null : destination.getId();
    }


    /**
     * Gets a value associated with a given field from the given data table.
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
     * Returns a string that is either empty or containing the given value.
     * Checks if the given field matches the search field. If so, returns the given value to search.
     * @param searchField       The search field name as defined by the application.
     * @param givenField        The field name given to the test.
     * @param givenValue        The value to search for if the search and given fields match.
     * @return                  A string that contains the given value or an empty string.
     */
    private String getValue(String searchField, String givenField, String givenValue) {
        return searchField.equals(givenField) ? givenValue : "";
    }


    /**
     * Converts the values of a cucumber DataTable to a JSON node object, using only the information that was given.
     * @param dataTable the cucumber datatable holding the values to be converted to JSON.
     * @return the JSON representation of the DataTable
     */
    private JsonNode convertDataTableToEditDestination(io.cucumber.datatable.DataTable dataTable) {
        int valueIndex = 0;
        List<Map<String, String>> valueList = dataTable.asMaps(String.class, String.class);
        Map<String, String> valueMap = valueList.get(valueIndex);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        for (Map.Entry<String, String> entry : valueMap.entrySet()) {
            String key;
            String value = entry.getValue();

            switch (entry.getKey()) {
                case TYPE_STRING:
                    key = TYPE;
                    break;
                case DISTRICT_STRING:
                    key = DISTRICT;
                    break;
                case LATITUDE_STRING:
                    key = LATITUDE;
                    break;
                case LONGITUDE_STRING:
                    key = LONGITUDE;
                    break;
                case COUNTRY_STRING:
                    key = COUNTRY;
                    break;
                case IS_PUBLIC_STRING:
                    key = IS_PUBLIC;
                    break;
                default:
                    key = entry.getKey();
            }
            json.put(key, value);
        }

        return json;
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
    @When("I attempt to edit destination {int} using the following values")
    public void iAttemptToEditDestinationUsingTheFollowingValues(Integer destinationId, io.cucumber.datatable.DataTable dataTable) {
        JsonNode editValues = convertDataTableToEditDestination(dataTable);
        this.destinationId = destinationId.longValue();
        editDestinationRequest(editValues);
    }

    @When("I request the destination usage for destination with id {int}")
    public void iRequestTheDestinationUsageForDestinationWithId(Integer destinationId) {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(DESTINATON_CHECK_URI + destinationId)
                .session(AUTHORIZED, loggedInId);
        Result result = route(application, request);
        statusCode = result.status();

        responseBody = Helpers.contentAsString(result);
    }

    @When("I add a photo with id {int} to an existing destination with id {int}")
    public void iAddAPhotoToASpecifiedDestination(Integer photoId, Integer destinationId) {
        JsonNode json = createDestinationPhotoJson(photoId);
        Http.RequestBuilder request =
                Helpers.fakeRequest()
                        .uri(DESTINATION_PHOTO_URI + destinationId)
                        .method(POST)
                        .bodyJson(json)
                        .session(AUTHORIZED, loggedInId);

        Result addDestinationPhotoResult = route(application, request);
        statusCode = addDestinationPhotoResult.status();
    }

    @Then("the trip count is {int}")
    public void theTripCountIs(int tripCountExpected) throws IOException {
        int tripCount = new ObjectMapper().readTree(responseBody).get(TRIP_COUNT).asInt();
        tripCountRecieved = new ObjectMapper().readTree(responseBody).get(MATCHING_TRIPS).size();
        Assert.assertEquals(tripCountExpected, tripCount);
    }

    @Then("the number of trips received is {int}")
    public void theNumberOfTripsReceivedIs(int tripListSize) throws IOException {
        Assert.assertEquals(tripCountRecieved, tripListSize);
    }

    @Then("the photo count is {int}")
    public void thePhotoCountIs(int photoCountExpected) throws IOException {
        int photoCount = new ObjectMapper().readTree(responseBody).get(PHOTO_COUNT).asInt();
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
        String arrNode = new ObjectMapper().readTree(responseBody).get(0).get(NAME).asText();

        Assert.assertEquals(value, arrNode);
    }


    @Then("the response contains at least one destination with district")
    public void theResponseContainsAtLeastOneDestinationWithDistrict(io.cucumber.datatable.DataTable dataTable) throws IOException {
        String value = getValueFromDataTable(DISTRICT_STRING, dataTable);
        String arrNode = new ObjectMapper().readTree(responseBody).get(0).get(DISTRICT).asText();

        Assert.assertEquals(value, arrNode);
    }


    @Then("the response contains at least one destination with latitude")
    public void theResponseContainsAtLeastOneDestinationWithLatitude(io.cucumber.datatable.DataTable dataTable) throws IOException {
        String value = getValueFromDataTable(LATITUDE_STRING, dataTable);
        String arrNode = new ObjectMapper().readTree(responseBody).get(0).get(LATITUDE).asText();

        Assert.assertEquals(value, arrNode);
    }


    @Then("the response is empty")
    public void theResponseIsEmpty() throws IOException {
        JsonNode arrNode = new ObjectMapper().readTree(responseBody);

        Assert.assertEquals(0, arrNode.size());
    }


    @Then("the response contains only own or public destinations")
    public void theResponseContainsOnlyOwnOrPublicDestinations() throws IOException {
        JsonNode arrNode = new ObjectMapper().readTree(responseBody);
        for (int i = 0 ; i < arrNode.size() ; i++) {
            assertTrue(arrNode.get(i).get("public").asBoolean() || arrNode.get(i).get("owner").get("id").asText() == loggedInId);
        }
    }


    @Then("the response contains only destinations owned by the user with id {int}")
    public void theResponseContainsOnlyDestinationsOwnedByTheUserWithId(Integer id) throws IOException {
        Long userId = id.longValue();
        JsonNode arrNode = new ObjectMapper().readTree(responseBody);
        Long ownerId;
        for (int i = 0 ; i < arrNode.size() ; i++) {
            DestinationRepository destinationRepo = new DestinationRepository();
            ownerId = destinationRepo.fetch(arrNode.get(i).get("id").asLong()).getOwner().getId();  //Gets owner id of destination
            assertEquals(userId, ownerId);
        }
    }


    /**
     * Tests that the owner of the destination is the specified user
     * @param userId    id of the expected owner
     */
    @Then("the owner is user {int}")
    public void theOwnerIsUser(Integer userId) {
        DestinationRepository destinationRepo = new DestinationRepository();
        Destination destination = destinationRepo.fetch(destinationId);
        Long expectedId = userId.longValue();
        assertEquals(expectedId, destination.getOwner().getId());
    }


    /**
     * Tests that the destination's photos contain the given photos
     * @param dataTable     ids of the photos expected
     */
    @Then("the destination will have photos with the following ids")
    public void theDestinationWillHavePhotosWithTheFollowingIds(io.cucumber.datatable.DataTable dataTable) {
        DestinationRepository destinationRepo = new DestinationRepository();
        Destination destination = destinationRepo.fetch(destinationId);

        List<String> photoIds = getPhotoIds(destination);
        List<String> expectedIds = dataTable.asList();
        expectedIds = expectedIds.subList(1, expectedIds.size());

        Collections.sort(expectedIds);
        Collections.sort(photoIds);

        assertEquals(expectedIds, photoIds);
    }


    /**
     * Gets an id list of the photos in the given destination
     * @param destination   the destination to check the photos in
     * @return              a list of the photo ids
     */
    private List<String> getPhotoIds(Destination destination) {
        List<String> photoIds = new ArrayList<>();
        for (PersonalPhoto photo : destination.getPhotoGallery()) {
            photoIds.add(photo.getId().toString());
        }

        return photoIds;
    }


    /**
     * Checks that the active destination is used in the given trips
     * @param dataTable     names of the trips the destination is expected to work in
     * @throws IOException
     */
    @Then("the destination will be used in the following trips")
    public void theDestinationWillBeUsedInTheFollowingTrips(io.cucumber.datatable.DataTable dataTable) throws IOException {
        DestinationRepository destinationRepo = new DestinationRepository();
        Destination destination = destinationRepo.fetch(destinationId);

        List<String> names = getTripNames();
        List<String> expectedNames = new ArrayList<>(dataTable.asList());
        expectedNames = expectedNames.subList(1, expectedNames.size());

        Collections.sort(expectedNames);
        Collections.sort(names);

        assertEquals(expectedNames, names);
    }

    private List<String> getTripNames() throws IOException {
        List<String> names = new ArrayList<>();

        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, loggedInId)
                .uri(DESTINATION_CHECK_URI + destinationId);
        Result result = route(application, request);
        statusCode = result.status();

        JsonNode arrNode = new ObjectMapper().readTree(Helpers.contentAsString(result));
        ArrayNode trips = (ArrayNode) arrNode.get("matchingTrips");

        for (JsonNode trip : trips) {
            names.add(trip.get("tripName").asText());
        }

        return names;
    }


    /**
     * Checks if the status code received is OK (200).
     */
    @Then("the status code received is OK")
    public void theStatusCodeReceivedIsOk() {
        assertEquals(OK, statusCode);
    }


    /**
     * Checks if the status code received is Created (201).
     */
    @Then("the status code received is Created")
    public void theStatusCodeReceivedIsCreated() {
        assertEquals(CREATED, statusCode);
    }


    /**
     * Checks if the status code received is Bad Request (400).
     */
    @Then("the status code received is Bad Request")
    public void theStatusCodeReceivedIsBadRequest() {
        assertEquals(BAD_REQUEST, statusCode);
    }


    /**
     * Checks if the status code received is Unauthorised (401).
     */
    @Then("the status code received is Unauthorised")
    public void theStatusCodeReceivedIsUnauthorised() {
        assertEquals(UNAUTHORIZED, statusCode);
    }


    /**
     * Checks if the status code received is Not Found (404).
     */
    @Then("the status code received is Not Found")
    public void theStatusCodeReceivedIsNotFound() {
        assertEquals(NOT_FOUND, statusCode);
    }


    /**
     * Checks if the status code received is Forbidden (403).
     */
    @Then("the status code received is Forbidden")
    public void theStatusCodeReceivedIsForbidden() {
        assertEquals(FORBIDDEN, statusCode);
    }
}
