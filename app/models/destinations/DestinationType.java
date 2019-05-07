package models.destinations;

import io.ebean.Finder;
import models.BaseModel;
import play.data.validation.Constraints;
import javax.persistence.Entity;

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

    /**
     * A finder used to search for a DestinationType
     */
    public static Finder<Integer, DestinationType> find = new Finder<>(DestinationType.class);
}
