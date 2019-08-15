package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import models.Profile;
import org.junit.Assert;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import repositories.ProfileRepository;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static play.test.Helpers.*;

public class AdminTestSteps {

    /**
     * Singleton class which stores generally used variables
     */
    private TestContext testContext = TestContext.getInstance();

    private static final String AUTHORIZED = "authorized";
    private static final String PROFILES_URI = "/v1/profiles";
    private static final String PROFILES_UPDATE_URI = "/v1/profile/";
    private static final String SINGLE_PROFILE_URI = "/v1/profile";

    /**
     * The username string variable.
     */
    private static final String USERNAME = "username";

    /**
     * The password string variable.
     */
    private static final String PASS_FIELD = "password";


    private static final Logger LOGGER = Logger.getLogger( AdminTestSteps.class.getName() );


    private ProfileRepository profileRepository = testContext.getApplication().injector().instanceOf(ProfileRepository.class);


    /**
     * Converts given data table information and creates a profile json for creating a profile.
     *
     * @param dataTable     the data table from cucumber.
     * @return              the Json formatted string of the profile.
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

        //Add values to a ObjectNode
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

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            profileToEdit = (ObjectNode) objectMapper.readTree(Helpers.contentAsString(result));
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

}
