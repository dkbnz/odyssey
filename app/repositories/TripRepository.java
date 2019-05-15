package repositories;

import io.ebean.ExpressionList;
import models.Profile;
import models.trips.Trip;
import models.trips.TripDestination;
import java.util.List;


public class TripRepository {


    private static final String PROFILE_ID = "profile_id";


    /**
     * Saves a new trip to a profile's list of trips, which is persisted to our database.
     * @param profile           The profile having a new trip created.
     * @param trip              The new trip being created for profile.
     */
    public void saveNewTrip(Profile profile, Trip trip) {

        // Add the new trip to the profile.
        profile.addTrip(trip);

        // Update the profile which cascades to save the trip and its contained destinations.
        profile.save();
    }



    /**
     * Removes the existing TripDestinations from a trip being edited.
     * Is used before parsing the request body in edit which will contain all the updated trips, "cleaning the slate".
     * @param trip the trip having its destinations deleted before editing
     */
    public void removeTripDestinations(Trip trip) {

        // Just get its list of destinations.
        List<TripDestination> destinations = trip.getDestinations();

        // Go through and delete each one from the database.
        for (TripDestination destination : destinations) {
            destination.delete();
        }
    }


    /**
     * Removes a single trip from a profile's list of trips, and deletes the trip from the profile before updating the profile.
     * This method is used in TripController inside the 'destroy' method.
     * @param profile The profile having its trip deleted.
     * @param trip The trip being deleted from a profile.
     */
    public void deleteTripFromProfile(Profile profile, Trip trip) {

        // Remove the trip from the profile's list of trips.
        profile.getTrips().remove(trip);

        // Delete the trip from the database.
        trip.delete();

        // Update the profile at a database level.
        profile.update();
    }


    /**
     * Finds all the trips with a specified user id
     * @param id the profile id
     * @return the list of trips
     */
    public List<Trip> fetchAllTrips(Long id) {

        List<Trip> trips;

        // Creates a list of trips from a query based on profile id
        ExpressionList<Trip> expressionList = Trip.find.query().where();
        expressionList.eq(PROFILE_ID, id);
        trips = expressionList.findList();

        return trips;
    }
}
