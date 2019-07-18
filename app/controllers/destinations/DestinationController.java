package controllers.destinations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.Config;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import models.Profile;
import models.destinations.Destination;
import models.destinations.DestinationType;
import models.trips.TripDestination;
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
    private static final String OWNER = "owner";
    public static final String IS_PUBLIC = "is_public";
    private static final String PAGE = "page";


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
     * Returns a Json object containing a count of trips that a specified destination is used in.
     *
     * @param request           Http request from the client containing authentication details
     * @param destinationId     the id of the destination to find the number of dependent trips for.
     * @return  ok()    (Http 200) response containing the number of trips a destination is used in.
     */
    public Result getTripsByDestination(Http.Request request, Long destinationId) {
        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }

        Destination destination = destinationRepo.fetch(destinationId);
        if (destination == null) {
            return notFound();
        }

        Profile loggedInUser = profileRepo.fetchSingleProfile(loggedInUserId);
        Integer destinationOwnerId = destination.getOwner().getId().intValue();
        Profile destinationOwner = profileRepo.fetchSingleProfile(destinationOwnerId);

        if (!AuthenticationUtil.validUser(loggedInUser, destinationOwner)) {
            return unauthorized();
        }

        int tripCount = Ebean.find(TripDestination.class)
                .select("trip")
                .where()
                .eq("destination", destination)
                .setDistinct(true)
                .findSet()
                .size();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode returnJson = mapper.createObjectNode();

        returnJson.put("count", tripCount);

        return ok(returnJson);
    }


    /**
     * Return a Json object listing all destination types in the database.
     *
     * @return ok() (Http 200) response containing all the different types of destinations.
     */
    public Result getTypes() {
        List<DestinationType> destinationTypes = DestinationType.find.all();
        return ok(Json.toJson(destinationTypes));
    }

    /**
     * Fetches all destinations with no search query. This will include all public destinations and all private
     * destinations for the logged in user.
     *
     * @return ok() (Http 200) response containing the destinations found in the response body.
     */
    private Result fetchAll(Http.Request request) {
        List<Destination> destinations;
        ExpressionList<Destination> expressionList = Destination.find.query().where();
        Profile user = Profile.find.byId(AuthenticationUtil.getLoggedInUserId(request));
        expressionList.disjunction()
                .eq(IS_PUBLIC, true)
                .conjunction()
                    .eq(IS_PUBLIC, false)
                    .eq(OWNER, user)
                .endJunction()
            .endJunction();
        destinations = expressionList.findList();
        return ok(Json.toJson(destinations));
    }

    /**
     * Fetches all destinations based on Http request query parameters.
     *
     * @param request   Http request containing query parameters to filter results.
     * @return          ok() (Http 200) response containing the destinations found in the response body.
     */
    public Result fetch(Http.Request request) {

        Profile loggedInUser = profileRepo.fetchSingleProfile(AuthenticationUtil.getLoggedInUserId(request));

        int pageNumber = 0;
        int pageSize = 50;

        //If there are no query parameters, return all destinations.
        if (request.queryString().isEmpty()) {
            return fetchAll(request);
        }

        //Filter destinations based on query parameters.
        Map<String, String[]> queryString = request.queryString();
        List<Destination> destinations;

        ExpressionList<Destination> expressionList = Destination.find.query().where();

        if (queryString.get(OWNER) != null && queryString.get(OWNER)[0] != null) {
            Profile destinationOwner = profileRepo.fetchSingleProfile(Integer.valueOf(queryString.get(OWNER)[0]));

            if (AuthenticationUtil.validUser(loggedInUser, destinationOwner)) {
                expressionList.eq(OWNER, queryComparator(queryString.get(OWNER)[0]));
            } else {
                return forbidden();
            }

        } else {
            expressionList
            .disjunction()
                .eq(IS_PUBLIC, true)
                    .conjunction()
                    .eq(IS_PUBLIC, false)
                .eq(OWNER, loggedInUser)
                .endJunction()
            .endJunction();
        }

        if (queryString.get(NAME) != null && queryString.get(NAME)[0] != null) {
            expressionList.ilike(NAME, queryComparator(queryString.get(NAME)[0]));
        }
        if (queryString.get(TYPE) != null && queryString.get(TYPE)[0] != null) {
            expressionList.ilike(TYPE, queryString.get(TYPE)[0]);
        }
        if (queryString.get(LATITUDE) != null && queryString.get(LATITUDE)[0] != null) {
            expressionList.eq(LATITUDE, Double.parseDouble(queryString.get(LATITUDE)[0]));
        }
        if (queryString.get(LONGITUDE) != null && queryString.get(LONGITUDE)[0] != null) {
            expressionList.eq(LONGITUDE, Double.parseDouble(queryString.get(LONGITUDE)[0]));
        }
        if (queryString.get(DISTRICT) != null && queryString.get(DISTRICT)[0] != null) {
            expressionList.ilike(DISTRICT, queryComparator(queryString.get(DISTRICT)[0]));
        }
        if (queryString.get(COUNTRY) != null && queryString.get(COUNTRY)[0] != null) {
            expressionList.ilike(COUNTRY, queryComparator(queryString.get(COUNTRY)[0]));
        }
        if (queryString.get(COUNTRY) != null && queryString.get(COUNTRY)[0] != null) {
            expressionList.ilike(COUNTRY, queryComparator(queryString.get(COUNTRY)[0]));
        }

        if (queryString.get(IS_PUBLIC) != null && queryString.get(IS_PUBLIC)[0] != null) {
            expressionList.eq(IS_PUBLIC, queryString.get(IS_PUBLIC)[0]);
        }

        // If page query is set, load said page. Otherwise, return the first page.
        if (queryString.get(PAGE) != null && queryString.get(PAGE)[0] != null) {
            pageNumber = Integer.parseInt(queryString.get(PAGE)[0]);
        }

        destinations = expressionList
                .order(NAME)
                .setFirstRow(pageNumber*pageSize)
                .setMaxRows(pageSize)
                .findPagedList()
                .getList();

        return ok(Json.toJson(destinations));
    }

    /**
     * Fetches all destinations by user.
     *
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
     *
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
     * Determines if a given Json input for creating a new destination already exists in the database. This is validated
     * through the name and district of the new destination being the same as another destination that exists either in
     * the user's private destinations or the public destinations.
     *
     * @param json              the Json of the destination inputs.
     * @param profileToChange   the profile that the destination to be added is owned by.
     * @return                  true if the destination does not exist in the appropriate database tables.
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
     *
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
                        return badRequest("A destination with the name '" + json.get(NAME).asText() + "' and " +
                                "district '" + json.get(DISTRICT).asText() + "' already exists either in your " +
                                "destinations or public destinations lists.");
                    }
                })
                .orElseGet(() -> unauthorized(NOT_SIGNED_IN)); // User is not logged in
    }

    /**
     * Creates a new destination object given a Json object.
     *
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
     *
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
     *
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
