package models.profiles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.util.BaseModel;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * TravellerType entity managed by Ebean.
 */
@Entity
public class TravellerType extends BaseModel {

    @JsonIgnore
    @ManyToMany
    private List<Profile> profiles;

    /**
     * The name of a traveller type.
     */
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
}
