package models;

import io.ebean.Finder;
import javax.persistence.Entity;

@Entity
public class Passport extends BaseModel {

    public String country;

    public String getCountry() {
        return country;
    }

    public static Finder<Integer, Passport> find = new Finder<>(Passport.class);
}
