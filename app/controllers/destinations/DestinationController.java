package controllers.destinations;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.ExpressionList;
import models.destinations.*;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;
import java.util.Map;

public class DestinationController extends Controller {

    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String COUNTRY = "country";
    private static final String DISTRICT = "district";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    /**
     * Fetches all destinations.
     * @return HTTP response containing the destinations found in the response body.
     */
    private Result fetch() {
        List<Destination> destinations = Destination.find.all();
        return ok(views.html.viewDestinations.tableDestinations.render(destinations));
    }

    private boolean checkDestination(JsonNode json) {
        List<Destination> destinations;
        String name = json.get("name").toString();
        ExpressionList<Destination> expressionList = Destination.find.query().where();

        expressionList.eq(NAME, name);

        System.out.println(expressionList);

        destinations = expressionList.findList();
        System.out.println(destinations);
        return (destinations.isEmpty());
    }

    /**
     * Fetches all destinations based on HTTP request query parameters.
     * @param request HTTP request containing query parameters to filter results.
     * @return HTTP response containing the destinations found in the response body.
     */
    public Result fetch(Http.Request request) {

        //If there are no query parameters, return all destinations.
        if (request.queryString().isEmpty()) {
            return fetch();
        }

        //Filter destinations based on query parameters.
        Map<String, String[]> queryString = request.queryString();
        List<Destination> destinations;

        ExpressionList<Destination> expressionList = Destination.find.query().where();
        String name = queryString.get(NAME)[0];
        String type = queryString.get(TYPE)[0];
        String latitude = queryString.get(LATITUDE)[0];
        String longitude = queryString.get(LONGITUDE)[0];
        String district = queryString.get(DISTRICT)[0];
        String country = queryString.get(COUNTRY)[0];

        if (name.length() != 0) {
            expressionList.ilike(NAME, queryComparator(name));
        }
        if (type.length() != 0) {
            expressionList.ilike(TYPE, type);
        }
        if (latitude.length() != 0) {
            expressionList.eq(LATITUDE, Double.parseDouble(latitude));
        }
        if (longitude.length() != 0) {
            expressionList.eq(LONGITUDE, Double.parseDouble(longitude));
        }
        if (district.length() != 0) {
            expressionList.ilike(DISTRICT, queryComparator(district));
        }
        if (country.length() != 0) {
            expressionList.ilike(COUNTRY, queryComparator(country));
        }

        destinations = expressionList.findList();

        return ok(views.html.viewDestinations.tableDestinations.render(destinations));
    }

    /**
     * This function builds a string to use in an sql query in a like clause. It places percentage signs on either
     * side of the given string parameter.
     * @param field The string to be concatenated with percentage signs on either side of the field.
     * @return A string containing the field wrapped in percentage signs.
     */
    private String queryComparator(String field) {
        return "%" + field + "%";
    }

    /**
     * Saves a new destination.
     * @param request HTTP request containing a json body of the new destination details.
     * @return HTTP response ok when the destination is saved.
     */
    public Result save(Http.Request request) {
        JsonNode json = request.body().asJson();
        boolean checkName = checkDestination(json);
        System.out.println(checkName);
        if (checkName) {
            return badRequest("Duplicate Destination Name Found!");
        } else {
            Destination destination = createNewDestination(json);
            destination.save();
            return ok("Created");
        }
        //TODO: Validate fields

    }

    /**
     * Creates a new destination object given a json object.
     * @param json The json of the destination object.
     * @return the new destination object.
     */
    private Destination createNewDestination(JsonNode json) {
        Destination destination = new Destination();
        destination.setName(json.get(NAME).asText());
        destination.setCountry(json.get(COUNTRY).asText());
        destination.setDistrict(json.get(DISTRICT).asText());
        destination.setLatitude(json.get(LATITUDE).asDouble());
        destination.setLongitude(json.get(LONGITUDE).asDouble());
        destination.setType(DestinationType.valueOf(json.get(TYPE).asText().toUpperCase()));
        return destination;
    }

    /**
     * Deletes a destination.
     * @param id The id of the destination.
     * @return HTTP response not found response if destination could not be found, ok if deleted.
     */
    public Result destroy(Long id) {
        Destination destination = Destination.find.byId(id.intValue());

        if (destination == null) {
            return notFound();
        }

        destination.delete();
        return ok("Deleted");
    }

    /**
     * Updates a destination based on input in the HTTP request body.
     * @param id The id of the destination.
     * @param request HTTP request containing a json body of fields to update in the destination.
     * @return HTTP response not found if destination could not be found, ok if updated.
     */
    public Result edit(Long id, Http.Request request) {
        JsonNode json = request.body().asJson();

        Destination oldDestination = Destination.find.byId(id.intValue());

        if (oldDestination == null) {
            return notFound();
        }

        //TODO: only update given fields

        oldDestination.setName(json.get(NAME).asText());
        oldDestination.setCountry(json.get(COUNTRY).asText());
        oldDestination.setDistrict(json.get(DISTRICT).asText());
        oldDestination.setLatitude(json.get(LATITUDE).asDouble());
        oldDestination.setLongitude(json.get(LONGITUDE).asDouble());
        oldDestination.setType(DestinationType.valueOf(json.get(TYPE).asText().toUpperCase()));

        oldDestination.update();

        return ok("Updated");
    }
}
