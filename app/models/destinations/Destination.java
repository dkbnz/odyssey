package models.destinations;

import io.ebean.Finder;
import models.BaseModel;
import models.photos.PersonalPhoto;
import play.data.validation.Constraints;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
     *
     */
    @Constraints.Required
    private double latitude;

    /**
     * The longitude of the destination.
     *
     */
    @Constraints.Required
    private double longitude;

    /**
     * The country the destination belongs to.
     */
    @Constraints.Required
    private String country;

    /**
     * The destinations photo gallery
     */
//    @ManyToMany(cascade=CascadeType.ALL)
//    private List<PersonalPhoto> photoGallery = new ArrayList<>();

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

//    public List<PersonalPhoto> getPhotoGallery() { return photoGallery; }
//
//    public boolean addPhotoToGallery(PersonalPhoto photoToAdd) {
//        return photoGallery.add(photoToAdd);
//    }
//
//    public boolean removePhotoFromGallery(PersonalPhoto photoToRemove) {
//        return photoGallery.remove(photoToRemove);
//    }

    public static final Finder<Integer, Destination> find = new Finder<>(Destination.class);
}
