package models;

import io.ebean.Finder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Passport extends BaseModel {

    public String country;

    public static Finder<Integer, Passport> find = new Finder<>(Passport.class);
}
