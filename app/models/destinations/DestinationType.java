package models.destinations;

import io.ebean.Finder;
import models.BaseModel;
import play.data.validation.Constraints;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class DestinationType extends BaseModel {

    public DestinationType() {}

    public DestinationType(String destinationType) {
        this.destinationType = destinationType;
    }

    /**
     * The name of a destination type
     */
    @Constraints.Required
    private String destinationType;

    public String getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }

    public static final Finder<Integer, DestinationType> find = new Finder<>(DestinationType.class);


    /**
     * Checks if an Object is equal to this instance of DestinationType.
     * A DestinationType is considered equal if:
     * destinationType are equal.
     *
     * @param obj   other object which this instance is being compared to.
     * @return      true if this object is equal to obj.
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == this) return true;
        if (!(obj instanceof DestinationType)) return false;

        DestinationType other = (DestinationType) obj;

        return other.getDestinationType().equals(this.destinationType);
    }


    /**
     * Calculates the hashcode of this DestinationType using:
     * destinationType
     *
     * @return  hashcode of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.destinationType);
    }
}
