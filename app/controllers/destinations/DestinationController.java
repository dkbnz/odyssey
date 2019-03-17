package controllers.destinations;

import com.fasterxml.jackson.databind.JsonNode;
import models.destinations.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;

public class DestinationController extends Controller {

    /**
     * Lists all destinations
     * @return
     */
    public Result fetch() {
        List<Destination> destinations = Destination.find.all();
        return ok(Json.toJson(destinations));
    }

    /**
     * Saves a new destination
     * @return ok when destination saved
     */
    public Result save(Http.Request request) {
        JsonNode json = request.body().asJson();

        //TODO: Validate fields

        Destination destination = createNewDestination(json);

        destination.save();
        return ok("Created");
    }

    private Destination createNewDestination(JsonNode json) {
        Destination destination = new Destination();
        destination.setId(json.get("id").asLong());
        destination.setName(json.get("name").asText());
        destination.setCountry(json.get("country").asText());
        destination.setDistrict(json.get("district").asText());
        destination.setLatitude(json.get("crd_latitude").asDouble());
        destination.setLongitude(json.get("crd_longitude").asDouble());
        destination.setType(DestinationType.valueOf(json.get("type").asText().toUpperCase()));
        return destination;
    }

    /**
     * Deletes a destination
     * @param id The id of the
     * @return not found response if destination could not be found, ok if deleted
     */
    public Result destroy(Long id) {
        Destination destination = Destination.find.byId(id.intValue());

        if (destination == null) {
            return notFound();
        }

        destination.delete();
        return ok("Deleted");
    }

    public Result edit(Long id, Http.Request request) {
        JsonNode json = request.body().asJson();

        Destination oldDestination = Destination.find.byId(id.intValue());

        if (oldDestination == null) {
            return notFound();
        }

        //TODO: only update given fields

        oldDestination.setName(json.get("name").asText());
        oldDestination.setCountry(json.get("country").asText());
        oldDestination.setDistrict(json.get("district").asText());
        oldDestination.setLatitude(json.get("crd_latitude").asDouble());
        oldDestination.setLongitude(json.get("crd_longitude").asDouble());
        oldDestination.setType(DestinationType.valueOf(json.get("type").asText().toUpperCase()));

        oldDestination.update();

        return ok("Updated");
    }
}
