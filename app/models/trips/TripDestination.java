package models.trips;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import models.BaseModel;
import models.destinations.Destination;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import java.time.LocalDate;

/**
 * Class for holding a certain trips destinations, start and end dates.
 */
@Entity
public class TripDestination extends BaseModel {
    /**
     * A finder used to search for a TripDestination.
     */
    public static final Finder<Integer, TripDestination> find = new Finder<>(TripDestination.class);

    /**
     * The starting date of the trip destination.
     */
    private LocalDate startDate;

    /**
     * The ending date of the trip destination.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /**
     * Position of the trip destination within a trip.
     */
    private int listOrder;

    /**
     * The trip that the trip destination is part of.
     */
    @JsonIgnore
    @ManyToOne(cascade=CascadeType.ALL)
    private Trip trip;

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    /**
     * The destination this trip destination is associated with.
     */
    @ManyToOne(cascade=CascadeType.PERSIST)
    private Destination destination;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getStartDate() {
        return startDate;
    }


    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getEndDate() {
        return endDate;
    }


    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    public int getListOrder() {
        return listOrder;
    }


    public void setListOrder(int listOrder) {
        this.listOrder = listOrder;
    }


    public Destination getDestination() {
        return destination;
    }


    public void setDestination(Destination destination) {
        this.destination = destination;
    }


    public Trip getTrip() {
        return trip;
    }

    /**
     * Clears the trip for the TripDestination
     */
    public void clearTrip() {
        trip = null;
    }


    public String toString() {
        return "{ " +
                "startDate: " + this.startDate + ", " +
                "endDate: " + this.endDate + ", " +
                "listOrder: " + this.listOrder + ", " +
                "trip: " + this.trip + ", " +
                "destination: " + this.destination + "}";
    }
}
