package repositories;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import models.Profile;
import models.destinations.Destination;
import models.trips.Trip;
import models.trips.TripDestination;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TripRepository extends BeanRepository<Long, Trip> {

    private static final String PROFILE_ID = "profile_id";
    private static final String TRIP_ID = "id";

    private ProfileRepository profileRepository;
    private TripDestinationRepository tripDestinationRepository;

    @Inject
    public TripRepository(ProfileRepository profileRepository,
                          TripDestinationRepository tripDestinationRepository) {
        super(Trip.class, Ebean.getDefaultServer());
        this.profileRepository = profileRepository;
        this.tripDestinationRepository = tripDestinationRepository;
    }

    /**
     * Saves a new trip to a profile's list of trips, which is persisted to our database.
     *
     * @param profile       the profile having a new trip created.
     * @param trip          the new trip being created for profile.
     */
    public void saveNewTrip(Profile profile, Trip trip) {

        // Add the new trip to the profile.
        profile.addTrip(trip);

        // Update the profile which cascades to save the trip and its contained destinations.
        profileRepository.save(profile);
    }


    /**
     * Updates a trip with new attributes and destinations. Also updates the profile.
     *
     * @param profile           the profile which is having a trip be updated.
     * @param trip              the updated trip.
     * @param destinationList   list of destinations to be added to the trip.
     */
    public void updateTrip(Profile profile, Trip trip, List<TripDestination> destinationList) {
        trip.setDestinations(destinationList);
        super.update(trip);
        profileRepository.update(profile);
    }


    /**
     * Updates a trip.
     * @param trip      The trip to update.
     */
    public void update(Trip trip) {
        super.update(trip);
    }


    /**
     * Removes the existing TripDestinations from a trip being edited.
     * Is used before parsing the request body in edit which will contain all the updated trips, "cleaning the slate".
     *
     * @param trip          the trip having its destinations deleted before editing.
     */
    public void removeTripDestinations(Trip trip) {

        // Just get its list of destinations.
        List<TripDestination> destinations = trip.getDestinations();

        // Go through and delete each one from the database.
        for (TripDestination tripDestination : destinations) {
            tripDestination.clearTrip();
            tripDestinationRepository.delete(tripDestination);
        }
    }


    /**
     * Removes a single trip from a profile's list of trips, and deletes the trip from the profile before updating
     * the profile.
     * This method is used in TripController inside the 'destroy' method.
     *
     * @param profile       the profile having its trip deleted.
     * @param trip          the trip being deleted from a profile.
     */
    public void deleteTripFromProfile(Profile profile, Trip trip) {

        // Remove the trip from the profile's list of trips.
        profile.getTrips().remove(trip);

        // Delete the trip from the database.
        super.delete(trip);

        // Update the profile at a database level.
        profileRepository.update(profile);
    }


    /**
     * Finds all the trips with a specified user id.
     *
     * @param profileId            the profile id.
     * @return              the list of trips.
     */
    public List<Trip> fetchAllTrips(Long profileId) {

        List<Trip> trips;

        // Creates a list of trips from a query based on profile id
        ExpressionList<Trip> expressionList = query().where();
        expressionList.eq(PROFILE_ID, profileId);
        trips = expressionList.findList();

        return trips;
    }


    /**
     * Finds a single trip with a given id. Returns null if no such trip was found.
     *
     * @param tripId        the id of the trip.
     * @return              the Trip object associated with the id. Null if no trip was found.
     */
    public Trip fetchSingleTrip(Long tripId) {
        return super.findById(tripId);
    }


    /**
     * Finds the profile id of the trip's owner.
     *
     * @param tripId        the id of the trip.
     * @return              the profile id of the owner of the trip.
     */
    public Long fetchTripOwner(Long tripId) {
        return query().select("profile.id").where().eq(TRIP_ID, tripId).findSingleAttribute();
    }


    /**
     * Finds the set of Trips a destination is used in.
     *
     * @param usedDestination   the destination to be checked if is used in any trips.
     * @return                  the set of Trips a destination is used in.
     */
    public Set<Trip> fetch(Destination usedDestination) {

        List<TripDestination> tripDestinations = tripDestinationRepository.findAllUsing(usedDestination);
        Set<Trip> trips = new HashSet<>();

        for (TripDestination tripDestination : tripDestinations) {
            trips.add(tripDestination.getTrip());
        }

        return trips;
    }

    public ExpressionList<Trip> getExpressionList() {
        return query().where();
    }
}
