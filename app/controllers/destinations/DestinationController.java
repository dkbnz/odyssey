package controllers.destinations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.Config;

import io.ebean.ExpressionList;
import models.Profile;
import models.destinations.Destination;
import models.destinations.DestinationType;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.destinations.DestinationRepository;
import repositories.ProfileRepository;
import repositories.destinations.DestinationTypeRepository;
import repositories.TripDestinationRepository;
import util.AuthenticationUtil;

import javax.inject.Inject;
import java.util.*;

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
    private DestinationTypeRepository destinationTypeRepo;
    private TripDestinationRepository tripDestinationRepo;
    private Config config;

    @Inject
    public DestinationController(
            ProfileRepository profileRepo,
            DestinationRepository destinationRepo,
            DestinationTypeRepository destinationTypeRepo,
            TripDestinationRepository tripDestinationRepo,
            Config config) {
        this.profileRepo = profileRepo;
        this.destinationRepo = destinationRepo;
        this.destinationTypeRepo = destinationTypeRepo;
        this.tripDestinationRepo = tripDestinationRepo;
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

        int tripCount = tripDestinationRepo.fetchTripsContainingDestination(destination).size();

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
     * Fetches all destinations based on Http request query parameters. This also includes pagination, destination
     * ownership and the public or private query.
     *
     * @param request   Http request containing query parameters to filter results.
     * @return          ok() (Http 200) response containing the destinations found in the response body, forbidden()
     *                  (Http 403) if the user has tried to access destinations they are not authorised for.
     */
    public Result fetch(Http.Request request) {

        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }

        Profile loggedInUser = profileRepo.fetchSingleProfile(loggedInUserId);

        int pageNumber = 0;
        int pageSize = 50;
        List<Destination> destinations;

        ExpressionList<Destination> expressionList = Destination.find.query().where();

        // Checks if the owner is specified in the query string and user is valid.
        if (request.getQueryString(OWNER) != null && !request.getQueryString(OWNER).isEmpty()) {
            Profile destinationOwner = profileRepo.fetchSingleProfile(Integer.valueOf(request.getQueryString(OWNER)));

            if (AuthenticationUtil.validUser(loggedInUser, destinationOwner)) {
                expressionList.eq(OWNER, destinationOwner);
            } else {
                return forbidden();
            }
        } else if (!loggedInUser.getIsAdmin()) {
            expressionList
                    .disjunction()
                        .eq(IS_PUBLIC, true)
                        .conjunction()
                            .eq(IS_PUBLIC, false)
                            .eq(OWNER, loggedInUser)
                        .endJunction()
                    .endJunction();
        }

        if (request.getQueryString(NAME) != null && !request.getQueryString(NAME).isEmpty()) {
            expressionList.ilike(NAME, queryComparator(request.getQueryString(NAME)));
        }
        if (request.getQueryString(TYPE) != null && !request.getQueryString(TYPE).isEmpty()) {
            expressionList.ilike(TYPE, request.getQueryString(TYPE));
        }
        if (request.getQueryString(LATITUDE) != null && !request.getQueryString(LATITUDE).isEmpty()) {
            expressionList.eq(LATITUDE, Double.parseDouble(request.getQueryString(LATITUDE)));
        }
        if (request.getQueryString(LONGITUDE) != null && !request.getQueryString(LONGITUDE).isEmpty()) {
            expressionList.eq(LONGITUDE, Double.parseDouble(request.getQueryString(LONGITUDE)));
        }
        if (request.getQueryString(DISTRICT) != null && !request.getQueryString(DISTRICT).isEmpty()) {
            expressionList.ilike(DISTRICT, queryComparator(request.getQueryString(DISTRICT)));
        }
        if (request.getQueryString(COUNTRY) != null && !request.getQueryString(COUNTRY).isEmpty()) {
            expressionList.ilike(COUNTRY, queryComparator(request.getQueryString(COUNTRY)));
        }
        if (request.getQueryString(IS_PUBLIC) != null && !request.getQueryString(IS_PUBLIC).isEmpty()) {
            expressionList.eq(IS_PUBLIC, request.getQueryString(IS_PUBLIC));
        }

        // If page query is set, load said page. Otherwise, return the first page.
        if (request.getQueryString(PAGE) != null && !request.getQueryString(PAGE).isEmpty()) {
            pageNumber = Integer.parseInt(request.getQueryString(PAGE));
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
     * @param destinationId     the id of the destination.
     * @return                  notFound() (Http 404) if destination could not found, ok() (Http 200) if successfully deleted.
     */
    public Result destroy(Http.Request request, Long destinationId) {
        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }

        Destination destination = Destination.find.byId(destinationId.intValue());

        if (destination == null) {
            return notFound();
        }

        Profile loggedInUser = profileRepo.fetchSingleProfile(loggedInUserId);
        if (!AuthenticationUtil.validUser(loggedInUser, destination.getOwner())) {
            return forbidden();
        }

        destinationRepo.delete(destination);
        return ok("Deleted");
    }


    /**
     * Updates a destination based on input in the Http request body.
     *
     * @param id        the id of the destination.
     * @param request   Http request containing a Json body of fields to update in the destination.
     * @return          notFound() (Http 404) if destination could not found, ok() (Http 200) if successfully updated.
     */
    public Result edit(Http.Request request, Long id) {
        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }

        Destination currentDestination = destinationRepo.fetch(id);

        if (currentDestination == null) {
            return notFound();
        }

        Profile loggedInUser = profileRepo.fetchSingleProfile(loggedInUserId);

        if (!AuthenticationUtil.validUser(loggedInUser, currentDestination.getOwner())) {
            return forbidden();
        }

        JsonNode json = request.body().asJson();

        Iterator<String> fieldIterator = json.fieldNames();
        while (fieldIterator.hasNext()) {
            String fieldName = fieldIterator.next();
            switch (fieldName) {

                case NAME:
                    currentDestination.setName(json.get(NAME).asText());
                    break;

                case COUNTRY:
                    currentDestination.setCountry(json.get(COUNTRY).asText());
                    break;

                case DISTRICT:
                    currentDestination.setDistrict(json.get(DISTRICT).asText());
                    break;

                case TYPE:
                    DestinationType type = destinationTypeRepo.fetch(json.get(TYPE).asLong());
                    currentDestination.setType(type);
                    break;

                case LATITUDE:
                    double latitude = json.get(LATITUDE).asDouble();
                    if (latitude > LATITUDE_LIMIT || latitude < -LATITUDE_LIMIT) {
                        return badRequest();
                    } else {
                        currentDestination.setLatitude(latitude);
                    }
                    break;

                case LONGITUDE:
                    double longitude = json.get(LONGITUDE).asDouble();
                    if (longitude > LONGITUDE_LIMIT || longitude < -LONGITUDE_LIMIT) {
                        return badRequest();
                    } else {
                        currentDestination.setLatitude(longitude);
                    }
                    currentDestination.setLongitude(json.get(LONGITUDE).asDouble());
                    break;

                case IS_PUBLIC:
                    currentDestination.setPublic(json.get(IS_PUBLIC).asBoolean());
                    break;

                default:
                    return badRequest();
            }
        }
        currentDestination.update();
        return ok("Destination updated");
    }
}
