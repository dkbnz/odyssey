package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import models.destinations.Destination;
import models.photos.PersonalPhoto;
import models.treasureHunts.TreasureHunt;
import models.trips.Trip;
import play.data.format.Formats;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Profile entity managed by Ebean.
 */
@Entity
@Table(name = "profile")
public class Profile extends BaseModel {

    private String username;

    @JsonIgnore
    private String password;

    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private LocalDate dateOfBirth;
    private Boolean isAdmin;

    @Formats.DateTime(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date dateOfCreation;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Nationality> nationalities = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    private List<TravellerType> travellerTypes = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Passport> passports = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "profile")
    private List<Trip> trips = new ArrayList<>();

    @OneToMany(cascade=CascadeType.ALL)
    private List<PersonalPhoto> photoGallery = new ArrayList<>();

    @OneToOne(cascade = CascadeType.REMOVE)
    private PersonalPhoto profilePicture;

    @JsonIgnore
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "owner")
    private List<Destination> myDestinations = new ArrayList<>();

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "owner")
    private List<TreasureHunt> myTreasureHunts = new ArrayList<>();

    public List<TreasureHunt> getMyTreasureHunts() {
        return myTreasureHunts;
    }

    public void setMyTreasureHunts(List<TreasureHunt> myTreasureHunts) {
        this.myTreasureHunts = myTreasureHunts;
    }

    public PersonalPhoto getProfilePicture() {
        return profilePicture;
    }


    public void setProfilePicture(PersonalPhoto profilePicture) {
        this.profilePicture = profilePicture;
    }


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


    public Boolean getIsAdmin() {
        return isAdmin;
    }


    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }


    public List<PersonalPhoto> getPhotoGallery() {
        return photoGallery;
    }


    public void setPhotoGallery(List<PersonalPhoto> photoGallery) {
        this.photoGallery = photoGallery;
    }


    public boolean addPhotoToGallery(PersonalPhoto photoToAdd) {
        return photoGallery.add(photoToAdd);
    }


    public boolean removePhotoFromGallery(PersonalPhoto photoToRemove) {
        return photoGallery.remove(photoToRemove);
    }


    public void addDestination(Destination newDestination) {
        this.myDestinations.add(newDestination);
    }


    public void removeDestination(Destination toRemove) {
        this.myDestinations.remove(toRemove);
    }


    public List<Destination> getMyDestinations() {
        return this.myDestinations;
    }


    public static final Finder<Integer, Profile> find = new Finder<>(Profile.class);
}
