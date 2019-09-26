package models.objectives;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import models.hints.Hint;
import models.quests.Quest;
import models.util.ApiError;
import models.util.BaseModel;
import models.profiles.Profile;
import models.destinations.Destination;
import models.util.Errors;
import util.Views;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Quest questUsing;

    @JsonView(Views.Owner.class)
    @OneToMany(cascade = CascadeType.ALL)
    private List<Hint> hints;

    public boolean addHint(Hint hint) {
        return hints.add(hint);
    }

    /**
     * Backend validation for if an objective has any errors in the format of creating an objective.
     * @return      a list of ApiError that states which parts of the objective are incorrect.
     */
    @JsonIgnore
    public Collection<ApiError> getErrors() {
        List<ApiError> errors = new ArrayList<>();

        if (riddle == null || riddle.isEmpty()) {
            errors.add(new ApiError(Errors.NO_OBJECTIVE_RIDDLE.toString()));
        } else if (riddle.length() > MAX_RIDDLE_SIZE) {
            errors.add(new ApiError(Errors.MAX_RIDDLE_LENGTH.toString()));
        }

        if (owner == null) {
            errors.add(new ApiError(Errors.NO_OBJECTIVE_OWNER.toString()));
        }

        if (destination == null || destination.getId() == null) {
            errors.add(new ApiError(Errors.NO_OBJECTIVE_DESTINATION.toString()));
        }

        if (radius == null || radius <= MIN_RADIUS_VALUE) {
            errors.add(new ApiError(Errors.NO_OBJECTIVE_RADIUS.toString()));
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

    public Integer getNumberOfHints() {
        return this.hints.size();
    }

    /**
     * Returns an empty list if hints is null, otherwise returns the list of hints for the objective.
     *
     * @return      a list of hints.
     */
    public List<Hint> getHints() {
        return hints == null ? new ArrayList<>() : hints;
    }


    /**
     * Checks if an Object is equal to this instance of Objective.
     * A Objective is considered equal if:
     * riddle, radius, destination and owner are equal.
     *
     * @param obj   other object which this instance is being compared to.
     * @return      true if this object is equal to obj.
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == this) return true;
        if (!(obj instanceof Objective)) return false;

        Objective other = (Objective) obj;

        return other.getRiddle().equals(this.riddle) &&
                other.getRadius().equals(this.radius) &&
                other.getDestination().equals(this.destination) &&
                other.getOwner().equals(this.owner);
    }


    /**
     * Calculates the hashcode of this Objective using:
     * riddle, radius, destination and owner.
     *
     * @return  hashcode of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.riddle,
                this.radius,
                this.destination,
                this.owner);
    }
}

