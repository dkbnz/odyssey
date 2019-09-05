package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import models.points.AchievementTracker;
import models.profiles.Profile;
import org.junit.Assert;
import org.springframework.beans.BeansException;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import repositories.profiles.ProfileRepository;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static play.test.Helpers.*;

public class ProfileTestSteps {

    /**
     * Singleton class which stores generally used variables
     */
    private TestContext testContext = TestContext.getInstance();


    /**
     * Authorised string variable.
     */
    private static final String AUTHORIZED = "authorized";

    /**
     * The profiles uri.
     */
    private static final String PROFILES_URI = "/v1/profiles";


    /**
     * The profiles uri.
     */
    private static final String SINGLE_PROFILE_URI = "/v1/profile";

    /**
     * The single profile uri.
     */
    private static final String PROFILES_UPDATE_URI = "/v1/profile/";

    /**
     * The traveller types uri.
     */
    private static final String TRAVELLER_TYPES_URI = "/v1/travtypes";

    /**
     * The nationalities uri.
     */
    private static final String NATIONALITIES_URI = "/v1/nationalities";

    /**
     * The username string variable.
     */
    private static final String USERNAME = "username";

    /**
     * The password string variable.
     */
    private static final String PASS_FIELD = "password";

    /**
     * A valid username for login credentials for admin user.
     */
    private static final String VALID_USERNAME = "admin@travelea.com";

    /**
     * The number of traveller types expected.
     */
    private static final int NUMBER_OF_TRAVELLER_TYPES = 7;

    /**
     * The number of nationalities expected.
     */
    private static final int NUMBER_OF_NATIONALITIES = 108;

    /**
     * The number of profiles expected.
     */
    private static final int NUMBER_OF_PROFILES = 6;

    private static final String NAME = "name";
    private static final String NATIONALITY = "nationalities";
    private static final String GENDER = "gender";
    private static final String TRAVELLER_TYPE = "travellerTypes";
    private static final String RANK = "rank";
    private static final String MIN_AGE = "min_age";
    private static final String MAX_AGE = "max_age";
    private static final String MIN_POINTS = "min_points";
    private static final String MAX_POINTS = "max_points";
    private static final String SORT_BY = "sortBy";
    private static final String SORT_ORDER = "sortOrder";

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
     * Logs any errors in the tests.
     */
    private static final Logger LOGGER = Logger.getLogger( ProfileTestSteps.class.getName() );


    private ProfileRepository profileRepository =
            testContext.getApplication().injector().instanceOf(ProfileRepository.class);


    /**
     * Gets the response as an iterator array Node from any fake request so that you can iterate over the response data.
     *
     * @param content   the string of the result using helper content as string.
     * @return          an Array node iterator.
     */
    private Iterator<JsonNode> getTheResponseIterator(String content) {
        JsonNode arrNode = null;
        try {
            arrNode = new ObjectMapper().readTree(content);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to get response iterator for fake request.", e);
        }
        return arrNode.elements();
    }


    /**
     * Converts given data table information and creates a profile json for creating a profile.
     *
     * @param dataTable     the data table from cucumber.
     * @return              the json formatted string of the profile.
     */
    private JsonNode convertDataTableToJsonNode(io.cucumber.datatable.DataTable dataTable) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String username = list.get(0).get("username");
        String password = list.get(0).get("password");
        String firstName = list.get(0).get("first_name");
        String middleName = list.get(0).get("middle_name");
        String lastName = list.get(0).get("last_name");
        String gender = list.get(0).get("gender");
        String dateOfBirth = list.get(0).get("date_of_birth");

        // complex json
        ObjectMapper mapper = new ObjectMapper();

        //Add values to a JsonNode
        ObjectNode json = mapper.createObjectNode();

        ObjectNode nationalityNode = mapper.createObjectNode();
        nationalityNode.put("id", Integer.valueOf(list.get(0).get("nationality")));

        ObjectNode travellerTypeNode = mapper.createObjectNode();
        travellerTypeNode.put("id", Integer.valueOf(list.get(0).get("traveller_type")));

        ObjectNode passportNode = mapper.createObjectNode();
        passportNode.put("id", Integer.valueOf(list.get(0).get("passport_country")));

        json.put("username", username);
        json.put("password", password);
        json.put("firstName", firstName);
        json.put("middleName", middleName);
        json.put("lastName", lastName);
        json.put("gender", gender);
        json.put("dateOfBirth", dateOfBirth);
        json.putArray("nationalities").add(nationalityNode);
        json.putArray("travellerTypes").add(travellerTypeNode);
        json.putArray("passports").add(passportNode);
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
    private String getValue(String searchField, List<String> givenFields, List<String> givenValues) {
        int index = 0;
        for (String givenField : givenFields) {
            if (searchField.equals(givenField)){
                return givenValues.get(index);
            }
            index += 1;
        }
        return "";
    }


    /**
     * Creates a query string for the search destination request.
     * Builds this query string with empty values except for the given search value associated
     * with the given search field.
     *
     * @param searchField       the search field name for the given value.
     * @param searchValue       the given search value for associated field.
     * @return                  the complete query string.
     */
    private String createSearchProfileQueryString(List<String> givenFields, List<String> givenValues) {
        String name = getValue(NAME, givenFields, givenValues);
        String nationality = getValue(NATIONALITY, givenFields, givenValues);
        String gender = getValue(GENDER, givenFields, givenValues);
        String travellerType = getValue(TRAVELLER_TYPE, givenFields, givenValues);
        String minAge = getValue(MIN_AGE, givenFields, givenValues);
        String maxAge = getValue(MAX_AGE, givenFields, givenValues);
        String minPoints = getValue(MIN_POINTS, givenFields, givenValues);
        String maxPoints = getValue(MAX_POINTS, givenFields, givenValues);

        minAge = minAge.equals("") ? "0"    : minAge;
        maxAge = maxAge.equals("") ? "120"  : maxAge;

        return QUESTION_MARK
                + NATIONALITY + EQUALS + nationality
                + AND
                + GENDER + EQUALS + gender
                + AND
                + MIN_AGE + EQUALS + minAge
                + AND
                + MAX_AGE + EQUALS + maxAge
                + AND
                + TRAVELLER_TYPE + EQUALS + travellerType
                + AND
                + NAME + EQUALS + name
                + AND
                + MIN_POINTS + EQUALS + minPoints
                + AND
                + MAX_POINTS + EQUALS + maxPoints
                + AND
                + SORT_BY + EQUALS + ""
                + AND
                + SORT_ORDER + EQUALS + "";

    }


    @Given("^The following profile exists with username \"(.*)\" within the TravelEA database:$")
    public void theFollowingProfileExistsWithUsernameWithinTheTravelEADatabase(String username) {
        // Sends the fake request
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, "1")
                .uri(PROFILES_URI);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

        // Gets the response
        Iterator<JsonNode> iterator = getTheResponseIterator(Helpers.contentAsString(result));

        // Finds profile from the iterator
        boolean foundProfile = false;
        while (iterator.hasNext() && !foundProfile) {
            JsonNode jsonProfile = iterator.next();
            if (jsonProfile.get(USERNAME).asText().equals(username)) {
                foundProfile = true;
            }
        }

        Assert.assertTrue(foundProfile);
    }


    @Given("^The following profile does not exist with the username \"(.*)\" within the TravelEA database$")
    public void theFollowingProfileDoesNotExistWithTheUsernameWithinTheTravelEADatabase(String username) {
        // Sends the fake request
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, "1")
                .uri(PROFILES_URI);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

        // Gets the response
        Iterator<JsonNode> iterator = getTheResponseIterator(Helpers.contentAsString(result));

        // Finds profile from the iterator
        boolean foundProfile = false;
        while (iterator.hasNext() && !foundProfile) {
            JsonNode jsonProfile = iterator.next();
            if (jsonProfile.get(USERNAME).asText().equals(username)) {
                foundProfile = true;
            }
        }

        Assert.assertFalse(foundProfile);
    }


    @Given("^a user exists in the database with the id (\\d+) and username \"(.*)\"$")
    public void aUserExistsInTheDatabaseWithTheIdAndUsername(Integer id, String username) {
        Profile profile = profileRepository.findById(id.longValue());
        Assert.assertNotNull(profile);
        Assert.assertEquals(profile.getUsername(), username);
    }


    @Given("^a user does not exist with the username \"(.*)\"$")
    public void aUserDoesNotExistWithTheUsername(String username) {
        Assert.assertNull(profileRepository.getExpressionList()
                .like(USERNAME, username)
                .findOne());
    }


    @Given("^the user (\\d+) has (\\d+) points$")
    public void theUserHasPoints(Integer userId, Integer points) {
        Profile profile = profileRepository.findById(userId.longValue());
        AchievementTracker achievementTracker = profile.getAchievementTracker(); //Null profile fails test, which is fine
        achievementTracker.addPoints(points);
        profileRepository.update(profile);
        Assert.assertEquals(points.longValue(), achievementTracker.getPoints());
    }


    @When("I send a GET request to the profiles endpoint")
    public void iSendAGETRequestToTheProfilesEndpoint() throws BeansException {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .uri(PROFILES_URI);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

        Iterator<JsonNode> iterator = getTheResponseIterator(Helpers.contentAsString(result));

        // Checks the response for admin profile and length of 2 users
        boolean passProfiles = false;
        int count = 0;
        while (iterator.hasNext()) {
            JsonNode jsonProfile = iterator.next();
            count++;
            if (jsonProfile.get("id").asText().equals("1") && jsonProfile.get(USERNAME).asText().equals(VALID_USERNAME)) {
                passProfiles = true;
            }
        }
        if (count != NUMBER_OF_PROFILES) {
            passProfiles = false;
        }
        testContext.setStatusCode(result.status());
        Assert.assertTrue(passProfiles);
    }


    @When("I send a GET request to the /travtypes endpoint")
    public void iSendAGETRequestToTheTravTypesEndpoint() throws BeansException {
        // Does the request to back end
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(TRAVELLER_TYPES_URI);
        Result result = route(testContext.getApplication(), request);

        // Gets the response
        Iterator<JsonNode> iterator = getTheResponseIterator(Helpers.contentAsString(result));

        // Checks the response for Holidaymaker and length of 7 traveller types
        boolean passTravelTypes = false;
        int count = 0;
        while (iterator.hasNext()) {
            JsonNode jsonTravellerType = iterator.next();
            count++;
            if (jsonTravellerType.get("id").asText().equals("5")
                    && jsonTravellerType.get("travellerType").asText().equals("Holidaymaker")) {
                passTravelTypes = true;
            }
        }
        if (count != NUMBER_OF_TRAVELLER_TYPES) {
            passTravelTypes = false;
        }
        testContext.setStatusCode(result.status());
        Assert.assertTrue(passTravelTypes);
    }


    @When("I send a GET request to the /nationalities endpoint")
    public void iSendAGETRequestToTheNationalitiesEndpoint() {
        // Does the fake request to back end
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(NATIONALITIES_URI);
        Result result = route(testContext.getApplication(), request);

        // Gets the response
        Iterator<JsonNode> iterator = getTheResponseIterator(Helpers.contentAsString(result));

        // Checks the response for the nationality
        boolean passNationalities = false;
        int count = 0;
        while (iterator.hasNext()) {
            JsonNode jsonTravellerType = iterator.next();
            count++;
            if (jsonTravellerType.get("id").asText().equals("16")
                    && (jsonTravellerType.get("nationality").asText().equals("Chinese"))) {
                passNationalities = true;
            }
        }
        if (count != NUMBER_OF_NATIONALITIES) {
            passNationalities = false;
        }

        testContext.setStatusCode(result.status());
        Assert.assertTrue(passNationalities);
    }


    @When("A user attempts to create a profile with the following fields:")
    public void aUserAttemptsToCreateAProfileWithTheFollowingFields(io.cucumber.datatable.DataTable dataTable) {
        // Creates the json for the profile
        JsonNode json = convertDataTableToJsonNode(dataTable);

        // Sending the fake request to the back end
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(PROFILES_URI);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    @When("The user attempts to update their profile information within the TravelEA database:")
    public void theUserAttemptsToUpdateTheirProfileInformationWithinTheTravelEADatabase(DataTable dataTable) {
        // Creates the json for the profile
        JsonNode json = convertDataTableToJsonNode(dataTable);

        // Sending the fake request to the back end for updating
        Http.RequestBuilder request = fakeRequest()
                .method(PUT)
                .session(AUTHORIZED, "2")
                .bodyJson(json)
                .uri(PROFILES_UPDATE_URI + 2); // Adding the id number to the uri, which is a string
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    @When("An admin attempts to create a profile with the following fields:")
    public void anAdminAttemptsToCreateAProfileWithTheFollowingFields(io.cucumber.datatable.DataTable dataTable) {
        // Creates the json for the profile
        JsonNode json = convertDataTableToJsonNode(dataTable);

        // Sending the fake request to the back end
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(PROFILES_URI);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

    }


    @When("^I search for profiles by \"([^\"]*)\" with value \"([^\"]*)\"$")
    public void iSearchForProfilesByFieldWithValue(String searchField, String searchValue) {
        searchValue = searchValue.replace(" ", "%20");
        List<String> searchFields = new ArrayList<>();
        List<String> searchValues = new ArrayList<>();

        searchFields.add(searchField);
        searchValues.add(searchValue);

        String searchQuery = createSearchProfileQueryString(searchFields, searchValues);

        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, TestContext.getInstance().getLoggedInId())
                .uri(PROFILES_URI + searchQuery);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        testContext.setResponseBody(Helpers.contentAsString(result));
    }


    @When("^I search for profiles by \"(.*)\" with value \"(.*)\" and by \"(.*)\" with value \"(.*)\"$")
    public void iSearchForProfilesByWithValueAndByWithValue(String searchField1, String searchValue1, String searchField2, String searchValue2) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }


    @When("^I change the username of the user with id (\\d+) to \"(.*)\"$")
    public void iChangeTheUsernameOfTheUserWithIdTo(Integer idToChange, String newUsername) {
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, String.valueOf(idToChange))
                .uri(SINGLE_PROFILE_URI);
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());

        Assert.assertEquals(OK, testContext.getStatusCode());

        ObjectNode profileToEdit = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode profileJson = mapper.readTree(Helpers.contentAsString(result));
            profileToEdit = profileJson.deepCopy();
            profileToEdit.put(USERNAME, newUsername);
            profileToEdit.put(PASS_FIELD, "");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error converting string to Json", e);
        }

        // Sending the fake request to the back end for updating
        request = fakeRequest()
                .method(PUT)
                .session(AUTHORIZED, testContext.getLoggedInId())
                .bodyJson(profileToEdit)
                .uri(PROFILES_UPDATE_URI + idToChange);

        result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
    }


    @Then("^the response contains the profile with username \"(.*)\"$")
    public void theResponseContainsProfile(String username) {
        Assert.assertTrue(testContext.getResponseBody().contains(username));
    }


    @Then("^the response does not contain the profile with username \"(.*)\"$")
    public void theResponseDoesNotContainProfile(String username) {
        Assert.assertFalse(testContext.getResponseBody().contains(username));
    }
}
