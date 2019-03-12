package models;

import io.ebean.Finder;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;


/**
 * Nationality entity managed by Ebean
 */
@Entity
public class Nationality extends BaseModel {


    /**
     * The demonym of the nation
     */
    public String nationality;


    /**
     * The name of the nation
     */
    public String country;


    /**
     * The user profiles associated with the nationalities
     */
    @ManyToMany(mappedBy = "nationalities")
    public List<Profile> profiles = new ArrayList<Profile>();


    /**
     * The passports associated with a nationality
     */
    @OneToMany(mappedBy = "nationality")
    public List<Passport> passports = new ArrayList<Passport>();


    public String getCountry() {
        return country;
    }


    public String getNationality() {
        return nationality;
    }


    /**
     * A finder used to search for nationalities
     */
    public static Finder<Integer, Nationality> find = new Finder<>(Nationality.class);
}