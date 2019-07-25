package models.destinations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import models.BaseModel;
import models.Profile;
import models.photos.PersonalPhoto;
import models.trips.TripDestination;
import play.data.validation.Constraints;
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
    @Constraints.Required
    private String name;

    /**
     * The type of destination (monument, natural landmark, building, event, etc...).
     */
    @Constraints.Required
    @ManyToOne
    private DestinationType type;

    /**
     * The district(s) the destination belongs to.
     *
     */
    @Constraints.Required
    private String district;

    /**
     * The latitude of the destination.
     */
    @Constraints.Required
    private double latitude;

    /**
     * The longitude of the destination.
     */
    @Constraints.Required
    private double longitude;

    /**
     * The country the destination belongs to.
     */
    @Constraints.Required
    private String country;

    /**
     * The owner of the destination.
     */
    @Constraints.Required
    @ManyToOne
    private Profile owner;

    /**
     * The destinations photo gallery.
     */
    @ManyToMany
    private Set<PersonalPhoto> photoGallery = new TreeSet<>();

    /**
     * Stating the privacy of the destination if it is public or not.
     */
    @Constraints.Required
    private Boolean isPublic;

    /**
     * List of trip destinations that the destination is associated with.
     */
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "destination", orphanRemoval=true)
    private List<TripDestination> tripDestinations;


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public DestinationType getType() {
        return type;
    }


    public void setType(DestinationType type) {
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


    public static final Finder<Integer, Destination> find = new Finder<>(Destination.class);


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


    public String toString() {
        return "{ " +
                "name: " + this.name + ", " +
                "type: " + this.type + ", " +
                "district: " + this.district + ", " +
                "latitude: " + this.latitude + ", " +
                "longitude: " + this.longitude + ", " +
                "country: " + this.country + ", " +
                "owner: " + this.owner + ", " +
                "photoGallery: List of size " + this.photoGallery.size() + ", " +
                "isPublic: " + this.isPublic + ", " +
                "tripDestinations: List of size " + this.tripDestinations.size() + "}";
    }

}
