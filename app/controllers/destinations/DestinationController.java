package controllers.destinations;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.ExpressionList;
import models.destinations.Destination;
import models.destinations.DestinationType;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import java.util.List;
import java.util.Map;

public class DestinationController extends Controller {

    private static final String NAME = "name";
    private static final String TYPE = "type_id";
    private static final String COUNTRY = "country";
    private static final String DISTRICT = "district";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    /**
     * Return a Json object listing all destination types in the database
     * @return ok() (Http 200) response containing all the different types of destinations.
     */
    public Result getTypes() {
        List<DestinationType> destinationTypes = DestinationType.find.all();
        return ok(Json.toJson(destinationTypes));
    }

    /**
     * Fetches all destinations.
     * @return ok() (Http 200) response containing the destinations found in the response body.
     */
    private Result fetch() {
        List<Destination> destinations = Destination.find.all();
        return ok(Json.toJson(destinations));
    }

    /**
     * Fetches all destinations based on Http request query parameters.
     * @param request   Http request containing query parameters to filter results.
     * @return          ok() (Http 200) response containing the destinations found in the response body.
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

        return ok(Json.toJson(destinations));
    }

    /**
     * This function builds a string to use in an sql query in a like clause. It places percentage signs on either
     * side of the given string parameter.
     * @param field     The string to be concatenated with percentage signs on either side of the field.
     * @return          A string containing the field wrapped in percentage signs.
     */
    private static String queryComparator(String field) {
        return "%" + field + "%";
    }

    /**
     * Looks at all the input fields for creating a destination and determines if the input is valid or not.
     * @param json      the Json of the destination inputs.
     * @return          a boolean true if the input is valid.
     */
    private boolean validInput(JsonNode json) {
        String name = json.get(NAME).asText();
        String country = json.get(COUNTRY).asText();
        String district = json.get(DISTRICT).asText();
        String latitude = json.get(LATITUDE).asText();
        String longitude = json.get(LONGITUDE).asText();

        // Checks all fields contain data
        if (name.length() == 0 || country.length() == 0 || district.length() == 0 || latitude.length() == 0 || longitude.length() == 0) {
            return false;
        }

        //Ensure latitude and longitude can be converted to doubles.
        try {
            Double.parseDouble(latitude);
            Double.parseDouble(longitude);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    /**
     * Determines if a given Json input for creating a new destination already exists in the database.
     * @param json      the Json of the destination inputs.
     * @return          true if the destination does not exist in the database.
     */
    private boolean destinationDoesNotExist(JsonNode json) {
        String name = json.get(NAME).asText();
        String district = json.get(DISTRICT).asText();

        List<Destination> destinations = Destination.find.query().where()
                .ilike(NAME, name)
                .ilike(DISTRICT, district)
                .findList();
        return (destinations.isEmpty());
    }

    /**
     * Saves a new destination. Checks the destination to be saved doesn't already exist in the database.
     * @param request   Http request containing a Json body of the new destination details.
     * @return          Http response created() (Http 201) when the destination is saved. If a destination with
     *                  the same name and district already exists in the database, returns badRequest() (Http 400).
     */
    public Result save(Http.Request request) {
        JsonNode json = request.body().asJson();

        if (!validInput(json)) {
            return badRequest("Invalid input.");
        }
        if (destinationDoesNotExist(json)) {
            Destination destination = createNewDestination(json);
            destination.save();
            return created("Created");
        } else {
            return badRequest("A destination with the name [ " +json.get(NAME).asText() + " ] and district [ "
                    + json.get(DISTRICT).asText() + " ] already exists.");
        }
    }

    /**
     * Creates a new destination object given a Json object.
     * @param json  the Json of the destination object.
     * @return      the new destination object.
     */
    private Destination createNewDestination(JsonNode json) {
        Destination destination = new Destination();
        destination.setName(json.get(NAME).asText());
        destination.setCountry(json.get(COUNTRY).asText());
        destination.setDistrict(json.get(DISTRICT).asText());
        destination.setLatitude(json.get(LATITUDE).asDouble());
        destination.setLongitude(json.get(LONGITUDE).asDouble());

        DestinationType destType = DestinationType.find.byId(json.get(TYPE).asInt());

        destination.setType(destType);

        return destination;
    }

    /**
     * Deletes a destination from the database using the given destination id number.
     * @param id    the id of the destination.
     * @return      notFound() (Http 404) if destination could not found, ok() (Http 200) if successfully deleted.
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
     * Updates a destination based on input in the Http request body.
     * @param id        the id of the destination.
     * @param request   Http request containing a Json body of fields to update in the destination.
     * @return          notFound() (Http 404) if destination could not found, ok() (Http 200) if successfully updated.
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

        DestinationType destType = DestinationType.find.byId(json.get(TYPE).asInt());
        oldDestination.setType(destType);

        oldDestination.update();

        return ok("Updated");
    }
}
