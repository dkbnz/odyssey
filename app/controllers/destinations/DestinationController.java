package controllers.destinations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.ExpressionList;
import models.TravellerType;
import models.destinations.Type;
import models.photos.PersonalPhoto;
import models.treasureHunts.TreasureHunt;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import com.google.inject.Inject;
import java.util.*;

import models.Profile;
import models.destinations.Destination;
import models.trips.Trip;
import models.trips.TripDestination;
import repositories.TripRepository;
import repositories.destinations.DestinationRepository;
import repositories.ProfileRepository;
import repositories.TripDestinationRepository;
import repositories.destinations.DestinationTypeRepository;
import repositories.treasureHunts.TreasureHuntRepository;
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
    private static final String DESTINATION_COUNT = "destination_count";
    private static final String MATCHING_TRIPS = "matching_trips";
    private static final String MATCHING_DESTINATIONS = "matching_destinations";
    private static final Double LATITUDE_LIMIT = 90.0;
    private static final Double LONGITUDE_LIMIT = 180.0;

    private ProfileRepository profileRepository;
    private DestinationRepository destinationRepository;
    private TripDestinationRepository tripDestinationRepository;
    private TripRepository tripRepository;
    private TreasureHuntRepository treasureHuntRepository;
    private DestinationTypeRepository destinationTypeRepository;


    @Inject
    public DestinationController(
            ProfileRepository profileRepository,
            DestinationRepository destinationRepository,
            DestinationTypeRepository destinationTypeRepository,
            TripDestinationRepository tripDestinationRepository,
            TripRepository tripRepository,
            TreasureHuntRepository treasureHuntRepository) {
        this.profileRepository = profileRepository;
        this.destinationRepository = destinationRepository;
        this.tripDestinationRepository = tripDestinationRepository;
        this.tripRepository = tripRepository;
        this.treasureHuntRepository = treasureHuntRepository;
        this.destinationTypeRepository = destinationTypeRepository;
    }


    /**
     * Returns a Json object containing a count of trips that a specified destination is used in and how many photos
     * that destination contains. As well as a list of each trips name and owner.
     *
     * @param request           Http request from the client containing authentication details.
     * @param destinationId     the id of the destination to find the number of dependent trips for and photos.
     * @return                  ok()    (Http 200) response containing the number of photos in a destination,
     *                          trips a destination is used in as well as the list of each trips name and its owner's
     *                          name. Otherwise, returns forbidden()
     *                          (Http 403) if the user is not allowed to access this number.
     */
    public Result getDestinationUsage(Http.Request request, Long destinationId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized();
        }

        Destination destination = destinationRepository.findById(destinationId);
        if (destination == null) {
            return notFound();
        }

        Profile destinationOwner = destination.getOwner();

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
     * Uses a Json body containing a destination to check any matching destinations upon editing a destination.
     *
     * @param request   Http request from the client containing authentication details and the given destination.
     * @return          unauthorized() (Http 401) if the user is not logged in.
     *                  forbidden() (Http 403) if the logged in user is not the owner of the destination.
     *                  ok() (Http 200) containing a Json list of the matching destinations.
     */
    public Result getDestinationUsageEdited(Http.Request request) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized();
        }

        JsonNode destination = request.body().asJson();
        Destination foundDestination = Json.fromJson(destination, Destination.class);

        if (!AuthenticationUtil.validUser(loggedInUser, foundDestination.getOwner())) {
            return forbidden();
        }

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode matchDestinations = mapper.valueToTree(destinationRepository.findEqual(foundDestination));
        ObjectNode returnJson = mapper.createObjectNode();
        returnJson.putArray(MATCHING_DESTINATIONS).addAll(matchDestinations);
        returnJson.put(DESTINATION_COUNT, matchDestinations.size());

        return ok(returnJson);
    }


    /**
     * Return a Json object listing all destination types in the database.
     *
     * @return ok() (Http 200) response containing all the different types of destinations.
     */
    public Result getTypes() {
        List<Type> destinationTypes = destinationTypeRepository.findAll();
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
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized();
        }

        int pageNumber = 0;
        int pageSize = 50;
        List<Destination> destinations;

        ExpressionList<Destination> expressionList = destinationRepository.getExpressionList();

        // Checks if the owner is specified in the query string and user is valid.
        if (request.getQueryString(OWNER) != null && !request.getQueryString(OWNER).isEmpty()) {
            Profile destinationOwner = profileRepository.findById(Long.valueOf(request.getQueryString(OWNER)));

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

        updateExpressionList(expressionList, request);

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
     * Adds expressions to the expression list to search for destinations depending on values present in the query
     * string of the given request.
     *
     * @param expressionList    the expression list used to search for destinations.
     * @param request           the request containing the query string used to formulate the expression list.
     */
    private void updateExpressionList(ExpressionList<Destination> expressionList, Http.Request request) {
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
    }


    /**
     * Fetches all destinations by user.
     *
     * @return ok() (Http 200) response containing the destinations found in the response body.
     */
    public Result fetchByUser(Http.Request request, Long userId) {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized();
        }

        Profile profileToChange = profileRepository.findById(userId);

        if (profileToChange == null) {
            return badRequest();
        }

        if(!AuthenticationUtil.validUser(loggedInUser, profileToChange)) {
            return forbidden();
        }

        List<Destination> destinations;
        ExpressionList<Destination> expressionList = destinationRepository.getExpressionList();
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
        String country = json.get(COUNTRY).asText();
        Long latitude = json.get(LATITUDE).asLong();
        Long longitude = json.get(DISTRICT).asLong();

        List<Destination> destinations = destinationRepository.getExpressionList()
                .ilike(NAME, name)
                .ilike(DISTRICT, district)
                .ilike(COUNTRY, country)
                .eq(LATITUDE, latitude)
                .eq(LONGITUDE, longitude)
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
                    Profile loggedInUser = profileRepository.findById(Long.valueOf(loggedInUserId));
                    Profile profileToChange = profileRepository.findById(userId);

                    if (profileToChange == null) {
                        return badRequest("Profile not found");
                    }

                    if (loggedInUser == null) {
                        return unauthorized();
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
                        destinationRepository.save(destination);

                        profileToChange.addDestination(destination);
                        profileRepository.save(profileToChange);

                        return created(Json.toJson(destination.getId()));
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

        Type destType = destinationTypeRepository.findById(json.get(TYPE).asLong());

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
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized();
        }

        Destination destination = destinationRepository.findById(destinationId);

        if (destination == null) {
            return notFound();
        }

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
    public Result edit(Http.Request request, Long id) throws IllegalAccessException {
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized();
        }

        Destination currentDestination = destinationRepository.findById(id);

        if (currentDestination == null) {
            return notFound();
        }


        if (!AuthenticationUtil.validUser(loggedInUser, currentDestination.getOwner())) {
            return forbidden();
        }

        JsonNode json = request.body().asJson();

        currentDestination.updateFromObject(Json.fromJson(json, Destination.class));

        if (currentDestination.getLongitude() > LONGITUDE_LIMIT || currentDestination.getLongitude() < -LONGITUDE_LIMIT) {
            return badRequest();
        }

        if (currentDestination.getLatitude() > LATITUDE_LIMIT || currentDestination.getLatitude() < -LATITUDE_LIMIT) {
            return badRequest();
        }

        mergeDestinations(currentDestination);
        destinationRepository.update(currentDestination);

        return ok(Json.toJson(currentDestination));
    }


    /**
     * Merges a given destination with similar destinations if required.
     *
     * @param destinationToUpdate   the destination that needs to be merged.
     */
    private void mergeDestinations(Destination destinationToUpdate) {
        List<Destination> similarDestinations = destinationRepository.findEqual(destinationToUpdate);

        if (!similarDestinations.isEmpty() && shouldMerge(destinationToUpdate, similarDestinations)) {
                destinationRepository.transferToAdmin(destinationToUpdate);
                for (Destination destinationToMerge: similarDestinations) {
                    consume(destinationToUpdate, destinationToMerge);
                }
        }
    }


    /**
     * Determines if the given destination and similar destinations should be merged into a single destination.
     *
     * @param destinationToUpdate   the destination that consumes similar destinations.
     * @param similarDestinations   the list of similar destinations to destinationToUpdate.
     * @return                      true if destinationToUpdate is public or any destinations in similarDestinations is
     *                              public, and false otherwise.
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
        mergeTravellerTypes(destinationToUpdate, destinationToMerge);
        mergeTreasureHunts(destinationToUpdate, destinationToMerge);
        destinationToUpdate.setPublic(true);

        destinationRepository.save(destinationToUpdate);
        destinationRepository.save(destinationToMerge);
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

        //Takes all trip destinations from other into this destination
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
            // Save to destination to update
            destinationToUpdate.addPhotoToGallery(photo);
        }
        destinationToMerge.clearPhotoGallery();
    }


    /**
     * Merges all the traveller types for the destinations including proposed traveller types.
     *
     * @param destinationToUpdate   destination that gains all of the traveller types.
     * @param destinationToMerge    destination that is being consumed.
     */
    private void mergeTravellerTypes(Destination destinationToUpdate, Destination destinationToMerge) {
        // Gets the traveller types for both destinations
        Set<TravellerType> mergeTravelTypesProposeAdd = destinationToMerge.getProposedTravellerTypesAdd();
        Set<TravellerType> mergeTravelTypesProposeRemove = destinationToMerge.getProposedTravellerTypesRemove();

        // Adds the two sets together into a combined traveller type set
        destinationToUpdate.addTravellerTypes(destinationToMerge.getTravellerTypes());

        // Adds all the remove proposed traveller types to destination to merge
        destinationToUpdate.addProposeTravellerTypesRemove(mergeTravelTypesProposeRemove);

        // Adds all the proposed traveller types that are not already in the main set
        destinationToUpdate.addProposeTravellerTypesAdd(mergeTravelTypesProposeAdd);
        Set<TravellerType> combinedProposedAdd = destinationToUpdate.getProposedTravellerTypesAdd();

        for (TravellerType travelType : destinationToUpdate.getTravellerTypes()) {
            combinedProposedAdd.remove(travelType);
        }

        destinationToUpdate.setProposedTravellerTypesAdd(combinedProposedAdd);

        // Removes all traveller type references for the destination to merge
        destinationToMerge.clearAllTravellerTypeSets();
    }


    /**
     * Merges all the treasure hunts for the destinations.
     *
     * @param destinationToUpdate   the destination that gains all the treasure hunts.
     * @param destinationToMerge    the destination that is being consumed.
     */
    private void mergeTreasureHunts(Destination destinationToUpdate, Destination destinationToMerge) {
        List<TreasureHunt> mergeTreasureHuntsList = treasureHuntRepository
                .getTreasureHuntsWithDestination(destinationToMerge);

        for (TreasureHunt treasureHunt : mergeTreasureHuntsList) {
            treasureHunt.setDestination(destinationToUpdate);
            treasureHuntRepository.update(treasureHunt);
        }
    }
}
