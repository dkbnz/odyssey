package models.treasureHunts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import models.BaseModel;
import models.Profile;
import models.destinations.Destination;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Class for treasure hunt, is used to initialise a treasure hunt.
 */
@Entity
public class TreasureHunt extends BaseModel {

    public static final Finder<Integer, TreasureHunt> find = new Finder<>(TreasureHunt.class);

    @JsonIgnore
    @ManyToOne
    private Destination destination;

    private String riddle;

    private Date startDate;

    private Date endDate;

    @JsonIgnore
    @ManyToOne
    private Profile owner;


    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public String getRiddle() {
        return riddle;
    }

    public void setRiddle(String riddle) {
        this.riddle = riddle;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Profile getOwner() {
        return owner;
    }

    public void setOwner(Profile owner) {
        this.owner = owner;
    }
}
