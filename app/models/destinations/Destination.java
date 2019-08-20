package models.destinations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.util.BaseModel;
import models.profiles.Profile;
import models.profiles.TravellerType;
import models.photos.PersonalPhoto;
import models.trips.TripDestination;
import javax.persistence.*;
import java.util.*;

/**
 * Class for destinations that users signify an interest in.
 */
@Entity
public class Destination extends BaseModel {

    /**
     * The name of the destination.
     */
    private String name;

    /**
     * The type of destination (monument, natural landmark, building, event, etc...).
     */
    @ManyToOne
    private Type type;

    /**
     * The district(s) the destination belongs to.
     *
     */
    private String district;

    /**
     * The latitude of the destination.
     */
    private double latitude;

    /**
     * The longitude of the destination.
     */
    private double longitude;

    /**
     * The country the destination belongs to.
     */
    private String country;

    /**
     * The owner of the destination.
     */
    @ManyToOne(cascade=CascadeType.PERSIST)
    private Profile owner;

    /**
     * The destinations photo gallery.
     */
    @ManyToMany
    private Set<PersonalPhoto> photoGallery;

    /**
     * Stating the privacy of the destination if it is public or not.
     */
    private Boolean isPublic;

    /**
     * List of trip destinations that the destination is associated with.
     */
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "destination")
    private List<TripDestination> tripDestinations;

    @ManyToMany
    @JoinTable(name = "destination_traveller_type")
    private Set<TravellerType> travellerTypes;

    @ManyToMany
    @JoinTable(name = "destination_proposed_traveller_type_add")
    private Set<TravellerType> proposedTravellerTypesAdd;

    @ManyToMany
    @JoinTable(name = "destination_proposed_traveller_type_remove")
    private Set<TravellerType> proposedTravellerTypesRemove;

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Type getType() {
        return type;
    }


    public void setType(Type type) {
        this.type = type;
    }


    public String getDistrict() {
        return district;
    }


    public void setDistrict(String district) {
        this.district = district;
    }


    public double getLatitude() {
        return latitude;
    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    public double getLongitude() {
        return longitude;
    }


    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public String getCountry() {
        return country;
    }


    public void setCountry(String country) { this.country = country; }


    public Set<PersonalPhoto> getPhotoGallery() { return photoGallery; }


    public boolean addPhotoToGallery(PersonalPhoto photoToAdd) {
        return photoGallery.add(photoToAdd);
    }


    public boolean removePhotoFromGallery(PersonalPhoto photoToRemove) {
        return photoGallery.remove(photoToRemove);
    }


    public void setPhotoGallery(Set<PersonalPhoto> photoGallery) {
        this.photoGallery = photoGallery;
    }


    public void clearPhotoGallery() {
        photoGallery.clear();
    }


    public List<TripDestination> getTripDestinations() {
        return tripDestinations;
    }

    /**
     * Adds the specified destination to the list of trip destinations.
     *
     * @param tripDestination the destination to be added to the list.
     * @return                the list of trip destinations.
     */
    public boolean addTripDestination(TripDestination tripDestination) {
        return tripDestinations.add(tripDestination);
    }


    public boolean removeTripDestination(TripDestination tripDestination) {
        return tripDestinations.remove(tripDestination);
    }

    public void addTripDestinations(List<TripDestination> tripDestinations){
        this.tripDestinations.addAll(tripDestinations);
    }

    public void clearTripDestinations() {
        tripDestinations.clear();
    }


    public Profile getOwner() {
        return this.owner;
    }


    public void changeOwner(Profile newOwner) {
        this.owner = newOwner;
    }


    public Boolean getPublic() {
        return isPublic;
    }


    public void setPublic(Boolean privacy) {
        isPublic = privacy;
    }

    public Set<TravellerType> getTravellerTypes() {
        return travellerTypes;
    }

    public void setTravellerTypes(Set<TravellerType> travellerTypes) {
        // TravellerTypes have been decided, clear propositions
        proposedTravellerTypesAdd.clear();
        proposedTravellerTypesRemove.clear();

        this.travellerTypes = travellerTypes;
    }

    public Set<TravellerType> getProposedTravellerTypesAdd() {
        return proposedTravellerTypesAdd;
    }

    public void setProposedTravellerTypesAdd(Set<TravellerType> proposedTravellerTypesAdd) {
        this.proposedTravellerTypesAdd = proposedTravellerTypesAdd;
    }

    public Set<TravellerType> getProposedTravellerTypesRemove() {
        return proposedTravellerTypesRemove;
    }

    public void setProposedTravellerTypesRemove(Set<TravellerType> proposedTravellerTypesRemove) {
        this.proposedTravellerTypesRemove = proposedTravellerTypesRemove;
    }

    public boolean addTravellerTypes(Collection<TravellerType> travellerTypesToAdd) {
        return this.travellerTypes.addAll(travellerTypesToAdd);
    }

    public boolean addProposeTravellerTypesAdd(Collection<TravellerType> travellerTypesToAdd) {
        return this.proposedTravellerTypesAdd.addAll(travellerTypesToAdd);
    }

    public boolean addProposeTravellerTypesRemove(Collection<TravellerType> travellerTypesToAdd) {
        return this.proposedTravellerTypesRemove.addAll(travellerTypesToAdd);
    }

    public void clearAllTravellerTypeSets() {
        this.proposedTravellerTypesAdd.clear();
        this.proposedTravellerTypesRemove.clear();
        this.travellerTypes.clear();
    }

    public void setOwner(Profile owner) {
        this.owner = owner;
    }

    public void setTripDestinations(List<TripDestination> tripDestinations) {
        this.tripDestinations = tripDestinations;
    }

    /**
     * Checks if an Object is equal to this instance of Destination.
     * A Destination is considered equal if:
     * name, type, district, country, latitude and longitude are equal.
     *
     * @param obj   other object which this instance is being compared to.
     * @return      true if this object is equal to obj.
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == this) return true;
        if (!(obj instanceof Destination)) return false;

        Destination other = (Destination) obj;

        return other.getName().equals(this.name) &&
                other.getType().equals(this.type) &&
                other.getDistrict().equals(this.district) &&
                other.getCountry().equals(this.country) &&
                other.getLatitude() == this.latitude &&
                other.getLongitude() == this.longitude;
    }


    /**
     * Calculates the hashcode of this Destination using:
     * name, type, district, country, latitude and longitude.
     *
     * @return  hashcode of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.name,
                this.type,
                this.district,
                this.country,
                this.latitude,
                this.longitude);
    }
}
