package unit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.trips.TripController;
import org.junit.Assert;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;

import static play.mvc.Results.ok;
import static play.test.Helpers.POST;
import static play.test.Helpers.fakeRequest;

import java.util.ArrayList;

public class TripControllerTest {

    @Test
    public void createTest1() {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        ObjectNode destination1 = mapper.createObjectNode();
        destination1.put("destination_id", "15040");
        destination1.put("start_date", "2020-05-21");
        destination1.put("end_date","2020-06-06");

        ObjectNode destination2 = mapper.createObjectNode();
        destination2.put("destination_id", "15042");
        destination2.put("start_date", "2021-06-06");
        destination2.put("end_date","2020-06-23");

        ArrayList<ObjectNode> destinations = new ArrayList<>();
        destinations.add(destination1);
        destinations.add(destination2);

        json.put("trip_name", "TestName1");
        json.putArray("trip_destinations").addAll(destinations);

        Http.Request request = fakeRequest(POST, "api/trips").bodyJson(json).build();

        TripController controller = new TripController();

        Result expectedResult = ok();
        Result actualResult = controller.create(request);

        Assert.assertEquals(expectedResult, actualResult);

    }

}
