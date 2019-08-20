package models.objectives;

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
import java.util.List;


/**
 * Class for objective, is used to initialise a objective.
 */
@Entity
public class Objective extends BaseModel {

    private static final int MAX_RIDDLE_SIZE = 255;
    private static final int MIN_RADIUS_VALUE = 0;


    @JsonView(Views.Owner.class)
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Destination destination;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Profile owner;

    @JsonView(Views.Public.class)
    private String riddle;

    @JsonView(Views.Owner.class)
    private Double radius;

    @JsonIgnore
    public Collection<ApiError> getErrors() {
        List<ApiError> errors = new ArrayList<>();

        if (riddle == null || riddle.isEmpty()) {
            errors.add(new ApiError("A riddle must be provided."));
        } else if (riddle.length() > MAX_RIDDLE_SIZE) {
            errors.add(new ApiError("Objective riddles must not exceed 255 characters in length."));
        }

        if (owner == null) {
            errors.add(new ApiError("This objective does not have an owner."));
        }

        if (destination == null || destination.getId() == null) {
            errors.add(new ApiError("Objectives must have a destination."));
        }

        if (radius == null || radius <= MIN_RADIUS_VALUE) {
            errors.add(new ApiError("You must select a range for an objective destination's check in"));
        }

        return errors;
    }

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

    public Profile getOwner() {
        return owner;
    }

    public void setOwner(Profile owner) {
        this.owner = owner;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }
}

