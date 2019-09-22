package repositories.trips;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import models.profiles.Profile;
import models.destinations.Destination;
import models.trips.Trip;
import models.trips.TripDestination;
import repositories.profiles.ProfileRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Handles database interaction for trips.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class TripRepository extends BeanRepository<Long, Trip> {

    private static final String PROFILE_ID = "profile.id";
    private static final String TRIP_ID = "id";
    private static final String DESTINATIONS_START_DATE = "destinations.startDate";

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
     * Finds the total number of future trips the specified profile has.
     *
     * @param profileId     the profile id.
     * @return              integer value of the total number of future trips.
     */
    public int fetchAllFutureTripsCount(Long profileId) {
        LocalDate today = LocalDate.now();
        return query()
                .where()
                .eq(PROFILE_ID, profileId)
                .disjunction()
                    .ge(DESTINATIONS_START_DATE, today)
                    .isNull(DESTINATIONS_START_DATE)
                .endJunction()
                .findList()
                .size();
    }


    /**
     * Finds the total number of past trips the specified profile has.
     *
     * @param profileId     the profile id.
     * @return              integer value of the total number of past trips.
     */
    public int fetchAllPastTripsCount(Long profileId) {
        LocalDate today = LocalDate.now();
        return query()
                .where()
                .eq(PROFILE_ID, profileId)
                .lt(DESTINATIONS_START_DATE, today)
                .findList()
                .size();
    }


    /**
     * Finds the profile id of the trip's owner.
     *
     * @param tripId        the id of the trip.
     * @return              the profile id of the owner of the trip.
     */
    public Long fetchTripOwner(Long tripId) {
        return query().select(PROFILE_ID).where().eq(TRIP_ID, tripId).findSingleAttribute();
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
