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

    public String nationality;
    public String country;

    public String getNationality() {
        return nationality;
    }

    public String getCountry() {
        return country;
    }

    public static Finder<Integer, Nationality> find = new Finder<>(Nationality.class);
}