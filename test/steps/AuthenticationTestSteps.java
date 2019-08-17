package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.en.When;
import play.mvc.Http;
import play.mvc.Result;
import java.util.List;
import java.util.Map;

import static play.test.Helpers.*;

public class AuthenticationTestSteps {

    /**
     * Singleton class which stores generally used variables
     */
    private TestContext testContext = TestContext.getInstance();

    private GeneralTestSteps generalTestSteps = new GeneralTestSteps();

    /**
     * The username string variable.
     */
    private static final String USERNAME = "username";

    /**
     * The password string variable.
     */
    private static final String PASS_FIELD = "password";


    /**
     * The login uri.
     */
    private static final String LOGIN_URI = "/v1/login";


    /**
     * Converts a given data table of destination values to a json node object of this destination.
     * @param dataTable     The data table containing values of a destination.
     * @return              A JsonNode of a profile containing information from the data table.
     */
    private JsonNode convertDataTableToJsonNode(io.cucumber.datatable.DataTable dataTable) {
        //Get all input from the data table
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        String username = list.get(0).get("Username");
        String password = list.get(0).get("Password");

        //Add values to a JsonNode
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put(USERNAME, username);
        json.put(PASS_FIELD, password);

        return json;
    }


    @When("I log in")
    public void iLogIn() {
        generalTestSteps.iAmLoggedIn();
    }


    @When("I log out")
    public void iLogOut() {
        generalTestSteps.iAmNotLoggedIn();
    }



    /**
     * Logs in using a login that is not stored in the database.
     * @param dataTable contains the invalid username and password.
     */
    @When("I send a POST request to the login endpoint with the following credentials")
    public void iSendAPOSTRequestToTheLoginEndpointWithTheFollowingCredentials(io.cucumber.datatable.DataTable dataTable) {
        JsonNode json = convertDataTableToJsonNode(dataTable);

        Http.RequestBuilder request = fakeRequest()
                .bodyJson(json)
                .method(POST)
                .uri(LOGIN_URI);
        Result loginResult = route(testContext.getApplication(), request);

        testContext.setStatusCode(loginResult.status());
    }
}
