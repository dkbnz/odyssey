package models;

import javax.persistence.Entity;


/**
 * Nationality entity managed by Ebean.
 */
@Entity
public class Nationality extends BaseModel {

    private String nationality;
    private String country;


    public String getNationality() {
        return nationality;
    }


    public String getCountry() {
        return country;
    }



}