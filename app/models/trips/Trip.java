package models.trips;

import io.ebean.annotation.JsonIgnore;
import models.util.BaseModel;
import models.profiles.Profile;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Class for holding trips for a user with all their trip destinations and trip name.
 */
@Entity
public class Trip extends BaseModel {

    /**
     * The name of the trip.
     */
    private String name;

    /**
     * The owner of the trip
     */
    @ManyToOne
    @JsonIgnore
    private Profile profile;


    public Profile getProfile() {
        return profile;
    }

    /**
     * The trips destinations for the trip.
     */
    @OneToMany(mappedBy="trip", cascade=CascadeType.ALL)
    private List<TripDestination> destinations;


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public List<TripDestination> getDestinations() {
        return destinations;
    }


    public void setDestinations(List<TripDestination> destinations) {
        this.destinations = destinations;
    }


    /**
     * Adds the given destination to the list of destinations.
     *
     * @param destination   destination to be added to list.
     * @return              whether the add process was successful.
     */
    public boolean addDestinations(TripDestination destination) {
        return destinations.add(destination);
    }


    /**
     * Removes the given destination from the list.
     *
     * @param destination   destination to be removed from the list.
     * @return              whether the removal process was successful.
     */
    public boolean removeDestinations(TripDestination destination) {
        return destinations.remove(destination);
    }

}
