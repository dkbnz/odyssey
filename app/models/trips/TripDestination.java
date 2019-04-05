package models.trips;

import io.ebean.Finder;
import models.BaseModel;
import models.destinations.Destination;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Date;

/**
 * Class for holding a certain trips destinations, start and end dates
 */
@Entity
public class TripDestination extends BaseModel {

    /**
     * The starting date of the trip destination.
     */
    private LocalDate start_date;

    /**
     * The ending date of the trip destination.
     */
    private LocalDate end_date;

    /**
     * Position of the trip destination within a trip
     */
    private int list_order;

    /**
     * The trip ID that the trip destination belongs to
     */
    @ManyToOne
    private Trip trip;

    /**
     * The destination ID this trip destination has
     */
    @ManyToOne(cascade=CascadeType.PERSIST)
    private Destination destination;


    public LocalDate getStartDate() {
        return start_date;
    }

    public void setStartDate(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEndDate() {
        return end_date;
    }

    public void setEndDate(LocalDate end_date) {
        this.end_date = end_date;
    }

    public int getList_order() {
        return list_order;
    }

    public void setList_order(int list_order) {
        this.list_order = list_order;
    }

//    public int getTrip_id() {
//        return trip_id;
//    }
//
//    public void setTrip_id(int trip_id) {
//        this.trip_id = trip_id;
//    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    /**
     * A finder used to search for a trip destination
     */
    public static Finder<Integer, TripDestination> find = new Finder<>(TripDestination.class);
}
