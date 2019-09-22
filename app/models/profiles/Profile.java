package models.profiles;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import models.hints.Hint;
import models.hints.Vote;
import models.points.AchievementTracker;
import models.util.BaseModel;
import models.destinations.Destination;
import models.photos.PersonalPhoto;
import models.quests.Quest;
import models.quests.QuestAttempt;
import models.objectives.Objective;
import models.trips.Trip;
import play.data.format.Formats;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;


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
    private boolean isAdmin;

    @Formats.DateTime(pattern = "yyyy-MM-dd")
    private Date lastSeenDate;

    @Formats.DateTime(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date dateOfCreation;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "profiles")
    private List<Nationality> nationalities;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "profiles")
    private List<TravellerType> travellerTypes;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "profiles")
    private List<Passport> passports;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profile")
    private List<Trip> trips;

    @OneToOne(cascade = CascadeType.REMOVE)
    private PersonalPhoto profilePicture;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profile")
    private List<PersonalPhoto> photoGallery;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<Destination> myDestinations;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<Objective> myObjectives;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attemptedBy")
    private List<QuestAttempt> questAttempts;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<Quest> myQuests;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "owner")
    private AchievementTracker achievementTracker;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private Set<Hint> hintsCreated;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "profilesSeen")
    private Set<Hint> hintsSeen;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Vote> votesCast;

    @Transient
    private int numberOfQuestsCreated;

    @Transient
    private int numberOfQuestsCompleted;

    public Date getLastSeenDate() {
        return lastSeenDate;
    }

    public void setLastSeenDate(Date lastSeenDate) {
        this.lastSeenDate = lastSeenDate;
    }


    public List<Objective> getMyObjectives() {
        return myObjectives;
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


    public int getNumberOfQuestsCreated() {
        if (myQuests == null) {
            return 0;
        }
        return myQuests.size();
    }


    /**
     * Calculates the number of quests the given profile has completed using the Java streams method.
     *
     * @return the total number of quests the profile has completed.
     */
    public long getNumberOfQuestsCompleted() {
        if (questAttempts == null) {
            return 0;
        }
        return questAttempts.stream().filter(QuestAttempt::isCompleted).count();
    }


    public boolean removeObjective(Objective objective) {
        return myObjectives.remove(objective);
    }


    public void addDestination(Destination newDestination) {
        this.myDestinations.add(newDestination);
    }


    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }


    public void setNationalities(List<Nationality> nationalities) {
        this.nationalities = nationalities;
    }


    public void setTravellerTypes(List<TravellerType> travellerTypes) {
        this.travellerTypes = travellerTypes;
    }


    public void setPassports(List<Passport> passports) {
        this.passports = passports;
    }


    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }


    public boolean isAdmin() {
        return isAdmin;
    }


    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }


    public List<Quest> getMyQuests() {
        return myQuests;
    }


    public AchievementTracker getAchievementTracker() {
        return achievementTracker;
    }


    public void setAchievementTracker(AchievementTracker achievementTracker) {
        this.achievementTracker = achievementTracker;
    }

    public Set<Hint> getHintsSeen() {
        return hintsSeen;
    }

    /**
     * Adds a new hint to the user's set of seen hints.
     *
     * @param seenHint      the new hint the user has seen.
     * @return              true if new hint was added, false if the hint is already in the set.
     */
    public boolean addSeenHint(Hint seenHint) {
        return hintsSeen.add(seenHint);
    }
}
