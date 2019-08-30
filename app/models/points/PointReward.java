package models.points;


import models.util.BaseModel;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class PointReward extends BaseModel {

    /**
     * The name of the point reward.
     */
    @Column(unique = true, nullable = false, name = "name")    // Declares that the name needs to be unique through the database.
    @Enumerated(EnumType.STRING)
    private Action name;

    /**
     * The points value of the reward.
     */
    @Column(nullable = false)
    private int value;


    public int getValue() {
        return value;
    }

    public Action getName() {
        return name;
    }

    /**
     * Overridden equals method which performs a deep comparison between the given object and this object.
     * @param object the object to be compared with.
     * @return true if they are equal, false if they are not equal.
     */
    @Override
    public boolean equals(Object object) {
        // Check if object is itself.
        if (this == object) return true;

        // Check if object is null or of a different class
        if (object == null ||  object.getClass() != this.getClass()) return false;

        // Cast object to PointReward Class
        PointReward pointReward = (PointReward) object;

        // Check to see if values the same.
        return (pointReward.getName().equals(this.name) && pointReward.getValue() == this.value);
    }

    /**
     * Overridden hashCode method of the class. Has is based off the class attributes.
     * @return the hash code of the class instance based on the name and value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id + this.value);
    }
}
