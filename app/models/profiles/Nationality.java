package models.profiles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.util.BaseModel;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;


/**
 * Nationality entity managed by Ebean.
 */
@Entity
public class Nationality extends BaseModel {

    @JsonIgnore
    @ManyToMany
    private List<Profile> profiles;

    private String nationality;

    private String country;

    public String getNationality() {
        return nationality;
    }

    public String getCountry() {
        return country;
    }
}