package controllers.destinations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.ExpressionList;
import models.photos.PersonalPhoto;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import javax.inject.Inject;
import java.util.*;

import models.Profile;
import models.destinations.Destination;
import models.destinations.DestinationType;
import models.trips.Trip;
import models.trips.TripDestination;
import repositories.TripRepository;
import repositories.destinations.DestinationRepository;
import repositories.ProfileRepository;
import repositories.destinations.DestinationTypeRepository;
import repositories.TripDestinationRepository;
import util.AuthenticationUtil;
import static util.QueryUtil.queryComparator;


public class DestinationController extends Controller {

    private static final String NAME = "name";
    private static final String TYPE = "type_id";
    private static final String COUNTRY = "country";
    private static final String DISTRICT = "district";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String OWNER = "owner";
    private static final String IS_PUBLIC = "is_public";
    private static final String PAGE = "page";
    private static final String AUTHORIZED = "authorized";
    private static final String NOT_SIGNED_IN = "You are not logged in.";
    private static final String TRIP_COUNT = "trip_count";
    private static final String PHOTO_COUNT = "photo_count";
    private static final String MATCHING_TRIPS = "matching_trips";
    private static final String MATCHING_DESTINATIONS = "matching_destinations";
    private static final String TRIP_ID = "trip_id";
    private static final String TRIP_NAME = "trip_name";
    private static final String USER_ID = "user_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final Double LATITUDE_LIMIT = 90.0;
    private static final Double LONGITUDE_LIMIT = 180.0;

    private ProfileRepository profileRepository;
    private DestinationRepository destinationRepository;
    private DestinationTypeRepository destinationTypeRepository;
    private TripDestinationRepository tripDestinationRepository;
    private TripRepository tripRepository;


    @Inject
    public DestinationController(
            ProfileRepository profileRepository,
            DestinationRepository destinationRepository,
            DestinationTypeRepository destinationTypeRepository,
            TripDestinationRepository tripDestinationRepository,
            TripRepository tripRepository) {
        this.profileRepository = profileRepository;
        this.destinationRepository = destinationRepository;
        this.destinationTypeRepository = destinationTypeRepository;
        this.tripDestinationRepository = tripDestinationRepository;
        this.tripRepository = tripRepository;
    }


    /**
     * Returns a Json object containing a count of trips that a specified destination is used in and how many photos
     * that destination contains. As well as a list of each trips name and owner.
     *
     * @param request           Http request from the client containing authentication details
     * @param destinationId     the id of the destination to find the number of dependent trips for and photos.
     * @return                  ok()    (Http 200) response containing the number of photos in a destination,
     *                          trips a destination is used in as well as the list of each trips name and its owner's
     *                          name. Otherwise, returns forbidden()
     *                          (Http 403) if the user is not allowed to access this number.
     */
    public Result getDestinationUsage(Http.Request request, Long destinationId) {
        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return unauthorized();
        }

        Destination destination = destinationRepository.fetch(destinationId);
        if (destination == null) {
            return notFound();
        }

        Profile loggedInUser = profileRepository.fetchSingleProfile(loggedInUserId);
        Integer destinationOwnerId = destination.getOwner().getId().intValue();
        Profile destinationOwner = profileRepository.fetchSingleProfile(destinationOwnerId);

        if (!AuthenticationUtil.validUser(loggedInUser, destinationOwner)) {
            return forbidden();
        }

        Set<Trip> matchingTrips = tripRepository.fetch(destination);

        int photoCount = destination.getPhotoGallery().size();
        int tripCount = matchingTrips.size();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode returnJson = mapper.createObjectNode();
        ArrayNode matchTrips = mapper.valueToTree(matchingTrips);
        ArrayNode matchDestinations = mapper.valueToTree(destinationRepository.findEqual(destination));

        returnJson.put(TRIP_COUNT, tripCount);
        returnJson.put(PHOTO_COUNT, photoCount);
        returnJson.putArray(MATCHING_TRIPS).addAll(matchTrips);
        returnJson.putArray(MATCHING_DESTINATIONS).addAll(matchDestinations);

        return ok(returnJson);
    }


    /**
     * Gets a list of all the trips that uses a particular destination.
     *
     * @param destination destination you want to search for.
     * @return a list of all the associated trips.
     */
    private List<Map> getTripsUsedByDestination(Destination destination) {
        List<TripDestination> tripDestinationList = tripDestinationRepository.fetchTripsContainingDestination(destination);

        List<Map> matchingTrips = new ArrayList<>();
        for (TripDestination tripDestination: tripDestinationList) {

            Trip temporaryTrip = tripDestination.getTrip();
            Map<Object, Object> tripDetails = new HashMap<>();
            tripDetails.put(TRIP_ID, temporaryTrip.getId());
            tripDetails.put(TRIP_NAME, temporaryTrip.getName());
            tripDetails.put(USER_ID, temporaryTrip.getProfile().getId());
            tripDetails.put(FIRST_NAME, temporaryTrip.getProfile().getFirstName());
            tripDetails.put(LAST_NAME, temporaryTrip.getProfile().getLastName());
            matchingTrips.add(tripDetails);
        }
        return matchingTrips;
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

        Profile loggedInUser = profileRepository.fetchSingleProfile(loggedInUserId);

        int pageNumber = 0;
        int pageSize = 50;
        List<Destination> destinations;

        ExpressionList<Destination> expressionList = Destination.find.query().where();

        // Checks if the owner is specified in the query string and user is valid.
        if (request.getQueryString(OWNER) != null && !request.getQueryString(OWNER).isEmpty()) {
            Profile destinationOwner = profileRepository.fetchSingleProfile(Integer.valueOf(request.getQueryString(OWNER)));

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
            expressionList.eq(TYPE, request.getQueryString(TYPE));
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

        Profile loggedInUser = profileRepository.fetchSingleProfile(loggedInUserId);

        Profile profileToChange = profileRepository.fetchSingleProfile(userId.intValue());

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
                    Profile loggedInUser = profileRepository.fetchSingleProfile(Integer.valueOf(loggedInUserId));

                    Profile profileToChange = profileRepository.fetchSingleProfile(userId.intValue());

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
                        profileRepository.save(profileToChange);

                        return created("Created");
                    } else {
                        return badRequest("A destination with these details already exists either in your " +
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
     * @return                  notFound() (Http 404) if destination could not found, ok() (Http 200) if
     *                          successfully deleted.
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

        Profile loggedInUser = profileRepository.fetchSingleProfile(loggedInUserId);
        if (!AuthenticationUtil.validUser(loggedInUser, destination.getOwner())) {
            return forbidden();
        }


        destinationRepository.delete(destination);
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

        Destination currentDestination = destinationRepository.fetch(id);

        if (currentDestination == null) {
            return notFound();
        }

        Profile loggedInUser = profileRepository.fetchSingleProfile(loggedInUserId);

        if (!AuthenticationUtil.validUser(loggedInUser, currentDestination.getOwner())) {
            return forbidden();
        }

        List<TripDestination> tripDestinationsList = currentDestination.getTripDestinations();

        JsonNode json = request.body().asJson();

        currentDestination = Json.fromJson(json, Destination.class);

        currentDestination.addTripDestinations(tripDestinationsList);

        if (currentDestination.getLongitude() > LONGITUDE_LIMIT || currentDestination.getLongitude() < -LONGITUDE_LIMIT) {
            return badRequest();
        }

        if (currentDestination.getLatitude() > LATITUDE_LIMIT || currentDestination.getLatitude() < -LATITUDE_LIMIT) {
            return badRequest();
        }

        mergeDestinations(currentDestination);
        destinationRepository.update(currentDestination);
        return ok("Destination updated");
    }


    /**
     * Merges a given destination with similar destinations if required.
     *
     * @param destinationToUpdate   the destination that needs to be merged.
     */
    private void mergeDestinations(Destination destinationToUpdate) {
        List<Destination> similarDestinations = destinationRepository.findEqual(destinationToUpdate);

        if (shouldMerge(destinationToUpdate, similarDestinations)) {
            if (!similarDestinations.isEmpty()) {
                for (Destination destinationToMerge: similarDestinations) {
                    consume(destinationToUpdate, destinationToMerge);
                }
                // Destination has been merged from other sources, change owner to admin.
                destinationRepository.transferDestinationOwnership(destinationToUpdate);
            }
        }
    }


    /**
     * Determines if the given destination and similar destinations should be merged into a single destination.
     *
     * @param destinationToUpdate   the destination that consumes similar destinations
     * @param similarDestinations   the list of similar destinations to destinationToUpdate
     * @return                      true if destinationToUpdate is public or any destinations in similarDestinations is
     *                              public. False otherwise.
     */
    private boolean shouldMerge(Destination destinationToUpdate, List<Destination> similarDestinations) {
        if (destinationToUpdate.getPublic()) {
            return true;
        }

        for (Destination destination: similarDestinations) {
            if (destination.getPublic()) {
                return true;
            }
        }
        return false;
    }


    /**
     * Used to merge destinations. Will extract desired attributes from a destinationToMerge and adds them to destinationToUpdate.
     * Then, updates each destination and deletes destinationToMerge.
     *
     * Will only consume if the given Destination is equal.
     *
     * @param destinationToUpdate   destination that gains the attributes of destinationToMerge.
     * @param destinationToMerge    destination that is being consumed by destinationToUpdate.
     */
    private void consume(Destination destinationToUpdate, Destination destinationToMerge) {
        if (!destinationToUpdate.equals(destinationToMerge)) return;

        mergeTripDestinations(destinationToUpdate, destinationToMerge);
        mergePersonalPhotos(destinationToUpdate, destinationToMerge);


        // Save destination that has had attributes taken to prevent deletion of attributes via cascading
        destinationRepository.update(destinationToUpdate);
        destinationRepository.update(destinationToMerge);

        destinationRepository.delete(destinationToMerge);
    }


    /**
     * Takes the trip destinations from the destinationToMerge and adds them to the destinationToUpdate.
     * Then removes these trip destinations from the destinationToMerge.
     *
     * @param destinationToUpdate   destination that gains the trip destinations of destinationToMerge.
     * @param destinationToMerge    destination that is being consumed by destinationToUpdate.
     */
    private void mergeTripDestinations(Destination destinationToUpdate, Destination destinationToMerge) {
        // Takes all trip destinations from other into this destination
        List<TripDestination> tripDestinationsToAdd = new ArrayList<>();
        List<TripDestination> tripDestinationsToDelete = new ArrayList<>();

        for (TripDestination tripDestination : destinationToMerge.getTripDestinations()) {

            // Create a new TripDestination as a copy of the TripDestination.
            TripDestination tripDestinationTemporary = new TripDestination();
            tripDestinationTemporary.setId(tripDestination.getId());
            tripDestinationTemporary.setDestination(destinationToUpdate);
            tripDestinationTemporary.setStartDate(tripDestination.getStartDate());
            tripDestinationTemporary.setEndDate(tripDestination.getEndDate());
            tripDestinationTemporary.setListOrder(tripDestination.getListOrder());
            tripDestinationTemporary.setTrip(tripDestination.getTrip());

            // Add the copied TripDestination to the list of TripDestination for the master destination in merge.
            destinationToUpdate.addTripDestination(tripDestinationTemporary);

            // Add both TripDestinations to the appropriate lists to be removed/deleted.
            tripDestinationsToAdd.add(tripDestinationTemporary);
            tripDestinationsToDelete.add(tripDestination);
        }

        for(TripDestination tripDestination: tripDestinationsToDelete) {
            // Set the Trip and Destination for the TripDestination to null, removing the foreign key links.
            tripDestination.setDestination(null);
            tripDestination.setTrip(null);
            tripDestinationRepository.update(tripDestination);
            tripDestinationRepository.delete(tripDestination);
        }

        for(TripDestination tripDestination: tripDestinationsToAdd) {
            // Save the Trip and Destination for the TripDestination, so can be added later.
            Trip trip = tripDestination.getTrip();
            Destination destination = tripDestination.getDestination();

            // Set the Trip and Destination for the TripDestination to null.
            tripDestination.setId(null);
            tripDestination.setTrip(null);
            tripDestination.setDestination(null);
            tripDestinationRepository.save(tripDestination);

            // Add the TripDestination to the Trip.
            trip.addDestinations(tripDestination);
            tripRepository.update(trip);

            // Set the Destination and Trip for the TripDestination.
            tripDestination.setDestination(destination);
            tripDestination.setTrip(trip);
            trip.addDestinations(tripDestination);

            destination.addTripDestination(tripDestination);

            // Save all changes to the database.
            destinationRepository.update(destination);
            tripDestinationRepository.update(tripDestination);
            tripRepository.update(trip);
        }
    }


    /**
     * Takes the personal photos from the destinationToMerge and adds them to the destinationToMerge.
     *
     * @param destinationToUpdate   destination that gains the personal photos of destinationToMerge.
     * @param destinationToMerge    destination that is being consumed by destinationToUpdate.
     */
    private void mergePersonalPhotos(Destination destinationToUpdate, Destination destinationToMerge) {
        // Take all PersonalPhotos
        for (PersonalPhoto photo : destinationToMerge.getPhotoGallery()) {
            // Remove photo from destination being merged
            destinationToMerge.removePhotoFromGallery(photo);
            // Save to destination to update
            destinationToUpdate.addPhotoToGallery(photo);
        }
    }
}
