package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.Finder;
import models.trips.Trip;
import play.data.format.Formats;
import play.libs.Json;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
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

    @ManyToMany
    public List<Nationality> nationalities = new ArrayList<Nationality>();

    @ManyToMany
    public List<TravellerType> travellerTypes = new ArrayList<TravellerType>();

    @ManyToMany
    public List<Passport> passports = new ArrayList<Passport>();

    @OneToMany(cascade=CascadeType.ALL)
    public List<Trip> trips = new ArrayList<Trip>();

    public void addTravType(TravellerType travellerType) {
        this.travellerTypes.add(travellerType);
    }

    public void addNationality(Nationality nationality) {
        this.nationalities.add(nationality);
    }

    public void addPassport(Passport passport) {
        this.passports.add(passport);
    }

    public void addTrip(Trip trip) {this.trips.add(trip);}

    /**
     * Converts a Profile object to a JSON readable format
     *
     * @return JsonNode object of profile
     */
    public JsonNode toJson() {
        ObjectNode profile = (ObjectNode) Json.toJson(this);
        profile.remove("password");
        return profile;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMiddleName() {
        return middleName;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public List<Passport> getPassports() {
        return passports;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public List<Nationality> getNationalities() {
        return nationalities;
    }

    public List<TravellerType> getTravellerTypes() {
        return travellerTypes;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public int getAge() {
        Period age = Period.between(dateOfBirth, LocalDate.now());
        return age.getYears();
    }

    public void clearTravellerTypes() {
        this.travellerTypes.clear();
    }

    public void clearNationalities() {
        this.nationalities.clear();
    }

    public void clearPassports() {
        this.passports.clear();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public static Finder<Integer, Profile> find = new Finder<>(Profile.class);
}