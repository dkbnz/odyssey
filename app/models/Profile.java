package models;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Finder;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.libs.Json;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Profile entity managed by Ebean
 */
@Entity
@Table(name = "profile")
public class Profile extends BaseModel {

    public String username;
    public String password;
    public String firstName;
    public String middleName;
    public String lastName;
    public String gender;
    public LocalDate dateOfBirth;

    @Formats.DateTime(pattern = "yyyy-MM-dd hh:mm:ss")
    public Date dateOfCreation;

    /**
     * Converts a Profile object to a JSON readable format
     * @return JsonNode object of profile
     */
    public JsonNode toJson(){
        JsonNode profile = Json.toJson(this);
        profile.
        return
    }

//    @ManyToMany
//    public List<TravellerType> travellerTypes = new ArrayList<TravellerType>();

    @ManyToMany
    public List<Nationality> nationalities = new ArrayList<Nationality>();

    @OneToMany(mappedBy = "profile")
    public List<Passport> passports = new ArrayList<Passport>();

//    public void addTravellerType(TravellerType travellerType) {
//        this.travellerTypes.add(travellerType);
//    }
//
//
//    public boolean removeTravellerType(TravellerType travellerType) {
//        if (this.travellerTypes.size() >= 2) {
//            return this.travellerTypes.remove(travellerType);
//        } else {
//            return false;
//        }
//    }

    public static Finder<Integer, Profile> find = new Finder<>(Profile.class);
}