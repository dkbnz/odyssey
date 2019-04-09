package unit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.ProfileController;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import static play.mvc.Results.ok;
import static play.test.Helpers.POST;
import static play.test.Helpers.fakeRequest;

public class ProfileControllerTest {


    @Before
    public void setUp() {
        //TODO: add profile to the database
    }


    @After
    public void tearDown() {
        //TODO: remove profile from the database
    }


    @Test
    public void checkUsernameTest1() {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put("username", "TestUser123");
        json.put("password", "TestPass");
        json.put("first_name", "Test");
        json.put("middle_name", "");
        json.put("last_name", "Dummy");
        json.put("date_of_birth", "2000-01-01");
        json.put("gender", "other");


        //json.putArray();

        Http.Request request = fakeRequest(POST, "/api/checkUsername").bodyJson(json).build();

        ProfileController controller = new ProfileController();

        Assert.assertEquals(ok(), controller.checkUsername(request));
    }
}
