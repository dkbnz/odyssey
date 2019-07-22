package models;

import io.ebean.Finder;
import play.data.validation.Constraints;
import javax.persistence.Entity;

/**
 * TravellerType entity managed by Ebean.
 */
@Entity
public class TravellerType extends BaseModel {

    /**
     * The name of a traveller type.
     */
    @Constraints.Required
    private String travellerType;

    private String description;

    private String imgUrl;

    public String getTravellerType() {
        return travellerType;
    }

    public String getDescription() {
        return description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public static final Finder<Integer, TravellerType> find = new Finder<>(TravellerType.class);
}
