package controllers.destinations;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import io.ebean.ExpressionList;
import models.Profile;
import models.destinations.Destination;
import models.destinations.DestinationType;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.DestinationRepository;
import repositories.ProfileRepository;
import util.AuthenticationUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static util.QueryUtil.queryComparator;

public class DestinationController extends Controller {

    public static final String NAME = "name";
    public static final String TYPE = "type_id";
    public static final String COUNTRY = "country";
    public static final String DISTRICT = "district";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String OWNER = "owner";
    public static final String IS_PUBLIC = "is_public";

    private static final Double LATITUDE_LIMIT = 90.0;
    private static final Double LONGITUDE_LIMIT = 180.0;

    private static final String AUTHORIZED = "authorized";
    private static final String NOT_SIGNED_IN = "You are not logged in.";
    private ProfileRepository profileRepo;
    private DestinationRepository destinationRepo;
    private Config config;

    @Inject
    public DestinationController(
            ProfileRepository profileRepo,
            DestinationRepository destinationRepo,
            Config config) {
        this.profileRepo = profileRepo;
        this.destinationRepo = destinationRepo;
        this.config = config;
    }


    /**
     * Return a Json object listing all destination types in the database.
     * @return ok() (Http 200) response containing all the different types of destinations.
     */
    public Result getTypes() {
        List<DestinationType> destinationTypes = DestinationType.find.all();
        return ok(Json.toJson(destinationTypes));
    }

    /**
     * Fetches all public destinations.
     * @return ok() (Http 200) response containing the destinations found in the response body.
     */
    private Result fetch() {
        List<Destination> destinations;
        ExpressionList<Destination> expressionList = Destination.find.query().where();
        expressionList.eq(IS_PUBLIC, true);

        destinations = expressionList.findList();
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

        String emptyString = "";

        ExpressionList<Destination> expressionList = Destination.find.query().where();
        String name =           queryString.get(NAME) == null         || queryString.get(NAME)[0] == null       ? emptyString : queryString.get(NAME)[0];
        String type =           queryString.get(TYPE) == null         || queryString.get(TYPE)[0] == null       ? emptyString : queryString.get(TYPE)[0];
        String latitude =       queryString.get(LATITUDE) == null     || queryString.get(LATITUDE)[0] == null   ? emptyString : queryString.get(LATITUDE)[0];
        String longitude =      queryString.get(LONGITUDE) == null    || queryString.get(LONGITUDE)[0] == null  ? emptyString : queryString.get(LONGITUDE)[0];
        String district =       queryString.get(DISTRICT) == null     || queryString.get(DISTRICT)[0] == null   ? emptyString : queryString.get(DISTRICT)[0];
        String country =        queryString.get(COUNTRY) == null      || queryString.get(COUNTRY)[0] == null    ? emptyString : queryString.get(COUNTRY)[0];

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
        expressionList.eq(IS_PUBLIC, true);


        destinations = expressionList.findList();

        return ok(Json.toJson(destinations));
    }

    /**
     * Fetches all destinations by user.
     * @return ok() (Http 200) response containing the destinations found in the response body.
     */
    public Result fetchByUser(Http.Request request, Long userId) {
        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }

        Profile loggedInUser = profileRepo.fetchSingleProfile(loggedInUserId);

        Profile profileToChange = profileRepo.fetchSingleProfile(userId.intValue());

        if (profileToChange == null) {
            return badRequest();
        }

        if(!AuthenticationUtil.validUser(loggedInUser, profileToChange)) {
            return forbidden();
        }

        List<Destination> destinations;
        ExpressionList<Destination> expressionList = Destination.find.query().where();
        expressionList.eq(OWNER,profileToChange);

        destinations = expressionList.findList();
        return ok(Json.toJson(destinations));
    }


    /**
     * Looks at all the input fields for creating a destination and determines if the input is valid or not.
     * @param json      the Json of the destination inputs.
     * @return          a boolean true if the input is valid.
     */
    private boolean validInput(JsonNode json) {
        String name =       json.get(NAME).asText();
        String country =    json.get(COUNTRY).asText();
        String district =   json.get(DISTRICT).asText();
        String latitude =   json.get(LATITUDE).asText();
        String longitude =  json.get(LONGITUDE).asText();

        // Checks all fields contain data
        if (name.length() == 0 || country.length() == 0 || district.length() == 0 ||
                latitude.length() == 0  || longitude.length() == 0) {
            return false;
        }

        Double latitudeValue;
        Double longitudeValue;

        // Ensure latitude and longitude can be converted to doubles.
        try {
            latitudeValue = Double.parseDouble(latitude);
            longitudeValue = Double.parseDouble(longitude);
        } catch (NumberFormatException e) {
            return false;
        }

        // Check range for latitude and longitude""
        if (latitudeValue > LATITUDE_LIMIT || latitudeValue < -LATITUDE_LIMIT) {
            return false;
        }
        if (longitudeValue > LONGITUDE_LIMIT || longitudeValue < -LONGITUDE_LIMIT) {
            return false;
        }

        // Check string inputs against regex
        String nameRegEx = "^(?=.{1,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$";

        return country.matches(nameRegEx);
    }

    /**
     * Determines if a given Json input for creating a new destination already exists in the database.
     * @param json      the Json of the destination inputs.
     * @return          true if the destination does not exist in the database.
     */
    private boolean destinationDoesNotExist(JsonNode json, Profile profileToChange) {
        String name = json.get(NAME).asText();
        String district = json.get(DISTRICT).asText();

        List<Destination> destinations = Destination.find.query().where()
                .ilike(NAME, name)
                .ilike(DISTRICT, district)
                .disjunction()
                .eq(IS_PUBLIC, true)
                .conjunction()
                .eq(IS_PUBLIC, false)
                .eq(OWNER, profileToChange)
                .endJunction()
                .endJunction()
                .findList();
        return (destinations.isEmpty());
    }

    /**
     * Saves a new destination. Checks the destination to be saved doesn't already exist in the database.
     * @param request   Http request containing a Json body of the new destination details.
     * @return          Http response created() (Http 201) when the destination is saved. If a destination with
     *                  the same name and district already exists in the database, returns badRequest() (Http 400).
     */
    public Result save(Http.Request request, Long userId) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(loggedInUserId -> {
                    Profile loggedInUser = profileRepo.fetchSingleProfile(Integer.valueOf(loggedInUserId));

                    Profile profileToChange = profileRepo.fetchSingleProfile(userId.intValue());

                    if (profileToChange == null) {
                        return badRequest();
                    }

                    if(!AuthenticationUtil.validUser(loggedInUser, profileToChange)) {
                        return forbidden();
                    }


                    JsonNode json = request.body().asJson();

                    if (!validInput(json)) {
                        return badRequest("Invalid input.");
                    }
                    if (destinationDoesNotExist(json, profileToChange)) {
                        Destination destination = createNewDestination(json, profileToChange);
                        destination.save();

                        profileToChange.addDestination(destination);
                        profileRepo.save(profileToChange);

                        return created("Created");
                    } else {
                        return badRequest("A destination with the name '" + json.get(NAME).asText() + "' and district '"
                                + json.get(DISTRICT).asText() + "' already exists either in your destinations or public destinations.");
                    }
                })
                .orElseGet(() -> unauthorized(NOT_SIGNED_IN)); // User is not logged in
    }

    /**
     * Creates a new destination object given a Json object.
     * @param json  the Json of the destination object.
     * @return      the new destination object.
     */
    private Destination createNewDestination(JsonNode json, Profile owner) {
        Destination destination = new Destination();
        destination.setName(json.get(NAME).asText());
        destination.setCountry(json.get(COUNTRY).asText());
        destination.setDistrict(json.get(DISTRICT).asText());
        destination.setLatitude(json.get(LATITUDE).asDouble());
        destination.setLongitude(json.get(LONGITUDE).asDouble());
        destination.setPublic(json.has(IS_PUBLIC) && json.get(IS_PUBLIC).asBoolean());
        destination.changeOwner(owner);

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
