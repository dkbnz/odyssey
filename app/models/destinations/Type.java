package models.destinations;

import models.BaseModel;
import play.data.validation.Constraints;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name="destination_type")
public class Type extends BaseModel {

    /**
     * The name of a destination type.
     */
    @Constraints.Required
    private String destinationType;

    public String getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }

    /**
     * Checks if an Object is equal to this instance of DestinationType.
     * A DestinationType is considered equal if destinationType are equal.
     *
     * @param obj   other object which this instance is being compared to.
     * @return      true if this object is equal to obj.
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == this) return true;
        if (!(obj instanceof Type)) return false;

        Type other = (Type) obj;

        return other.getDestinationType().equals(this.destinationType);
    }

    /**
     * Calculates the hashcode of this DestinationType using destinationType.
     *
     * @return  hashcode of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.destinationType);
    }
}
