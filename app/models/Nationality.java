package models;

import io.ebean.Finder;

import javax.persistence.Entity;


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