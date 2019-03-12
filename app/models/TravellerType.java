package models;

import io.ebean.Finder;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * TravellerType entity managed by Ebean
 */
@Entity
public class TravellerType extends BaseModel {


    /**
     * The name of a traveller type
     */
    @Constraints.Required
    public String travellerType;


    /**
     * The Profile entities mapped to a traveller type
     */
    @ManyToMany(mappedBy = "travellerTypes")
    public List<Profile> profiles = new ArrayList<Profile>();


    public String getTravellerType() {
        return travellerType;
    }


    public void setTravellerType(String travellerType) {
        this.travellerType = travellerType;
    }


    /**
     * A finder used to search for a traveller type
     */
    public static Finder<Integer, TravellerType> find = new Finder<>(TravellerType.class);
}
