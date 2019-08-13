package models.treasureHunts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import models.ApiError;
import models.BaseModel;
import models.Profile;
import models.destinations.Destination;
import util.Views;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Class for treasure hunt, is used to initialise a treasure hunt.
 */
@Entity
public class TreasureHunt extends BaseModel {



    @JsonView(Views.Owner.class)
    @ManyToOne
    private Destination destination;

    @JsonView(Views.Public.class)
    private String riddle;

    @JsonView(Views.Public.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssZ")
    private Date startDate;

    @JsonView(Views.Public.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssZ")
    private Date endDate;

    @JsonIgnore
    @ManyToOne(cascade=CascadeType.PERSIST)
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

    @JsonIgnore
    public Collection<ApiError> getErrors() {
        List<ApiError> errors = new ArrayList<>();

        if (riddle == null || riddle.isEmpty()) {
            errors.add(new ApiError("A riddle must be provided."));
        } else if (riddle.length() > 255) {
            errors.add(new ApiError("Riddles must not exceed 255 characters in length."));
        }

        if(startDate == null) {
            errors.add(new ApiError("You must provide a start date."));
        }

        if(endDate == null) {
            errors.add(new ApiError("You must provide an end date."));
        }

        if (startDate != null && endDate != null && endDate.before(startDate)) {
            errors.add(new ApiError("Start date must be before end date."));
        }

        if (owner == null) {
            errors.add(new ApiError("This treasure hunt does not have an owner."));
        }

        if (destination == null || destination.getId() == null) {
            errors.add(new ApiError("Treasure hunts must have a destination."));
        }

        return errors;
    }
}
