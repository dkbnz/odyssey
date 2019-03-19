package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Destination;
import models.DestinationType;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;

import views.html.destinations.listDestinations;

public class DestinationController extends Controller {

    /**
     * Lists all destinations
     * @return
     */
    public Result index() {
        List<Destination> destinations = Destination.find.all();
        return ok(listDestinations.render(destinations));
    }

    /**
     * Creates a new destination
     * @return ok when page created
     */
    public Result create() {
        return ok("create page");
    }

    /**
     * Saves a new destination
     * @return ok when destination saved
     */
    public Result save(Http.Request request) {
        JsonNode json = request.body().asJson();

        Destination destination = new Destination();
        destination.setId(json.get("id").asLong());
        destination.setName(json.get("name").asText());
        destination.setCountry(json.get("country").asText());
        destination.setDistrict(json.get("district").asText());
        destination.setLatitude(json.get("crd_latitude").asDouble());
        destination.setLongitude(json.get("crd_longitude").asDouble());
        destination.setType(DestinationType.valueOf(json.get("type").asText().toUpperCase()));

        destination.save();
        return ok("created");
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
        return ok("deleted");
    }

    public Result edit(Http.Request request) {
        JsonNode json = request.body().asJson();

        Long id = json.get("id").asLong();
        Destination oldDestination = Destination.find.byId(id.intValue());

        if (oldDestination == null) {
            return notFound();
        }


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
