package models;

import io.ebean.Finder;
import play.data.format.Formats;
import play.data.validation.Constraints;

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


    /**
     * The first name of a user
     */
    public String fName;


    /**
     * The middle name of a user
     */
    public String mName;


    /**
     * The last name of a user
     */
    public String lName;


    /**
     * The gender of a user
     */
    public String gender;


    /**
     * The date of birth of a user
     */
    public LocalDate dateOfBirth;


    /**
     * The username of a user
     */
    public String username;


    /**
     * The password of a user
     */
    public String password;


    /**
     * The activation time of the user profile
     */
    @Formats.DateTime(pattern = "yyyy-MM-dd hh:mm:ss")
    public Date dateOfCreation;

    /**
     * The traveller types of a user
     */
    @ManyToMany
    public List<TravellerType> travellerTypes = new ArrayList<TravellerType>();


    /**
     * The nationalities of a user
     */
    @ManyToMany
    public List<Nationality> nationalities = new ArrayList<Nationality>();


    /**
     * The passports of a user
     */
    @OneToMany(mappedBy = "profile")
    public List<Passport> passports = new ArrayList<Passport>();


    public String getfName() {
        return fName;
    }


    public void setfName(String fName) {
        this.fName = fName;
    }


    public String getmName() {
        return mName;
    }


    public void setmName(String mName) {
        this.mName = mName;
    }


    public String getGender() {
        return gender;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getlName() {
        return lName;
    }


    public void setlName(String lName) {
        this.lName = lName;
    }


    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }


    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    public Date getDateOfCreation() {
        return dateOfCreation;
    }


    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }


    public List<TravellerType> getTravellerTypes() {
        return travellerTypes;
    }


    public void addTravellerType(TravellerType travellerType) {
        this.travellerTypes.add(travellerType);
    }


    public boolean removeTravellerType(TravellerType travellerType) {
        if (this.travellerTypes.size() >= 2) {
            return this.travellerTypes.remove(travellerType);
        } else {
            return false;
        }
    }


    public static Finder<Integer, Profile> find = new Finder<>(Profile.class);


    public List<Nationality> getNationalities() {
        return nationalities;
    }


    public List<Passport> getPassports() {
        return passports;
    }


    public void addNationality(Nationality nationality) {
        nationalities.add(nationality);
    }

    public void addPassport(Passport passport) {
        passports.add(passport);
    }

    public void destroyPassports() {
        for (Passport p : passports) {
            p.delete();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}