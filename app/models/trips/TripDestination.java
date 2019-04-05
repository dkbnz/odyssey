package models.trips;

import io.ebean.Finder;
import models.BaseModel;
import models.destinations.Destination;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;

/**
 * Class for holding a certain trips destinations, start and end dates
 */
@Entity
public class TripDestination extends BaseModel {

    /**
     * The starting date of the trip destination.
     */
    private Date start_date;

    /**
     * The ending date of the trip destination.
     */
    private Date end_date;

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
    @ManyToOne
    private Destination destination;


    public Date getStartDate() {
        return start_date;
    }

    public void setStartDate(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEndDate() {
        return end_date;
    }

    public void setEndDate(Date end_date) {
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

//    public int getDestination_id() {
//        return destination_id;
//    }
//
//    public void setDestination_id(int destination_id) {
//        this.destination_id = destination_id;
//    }

    /**
     * A finder used to search for a trip destination
     */
    public static Finder<Integer, TripDestination> find = new Finder<>(TripDestination.class);
}
