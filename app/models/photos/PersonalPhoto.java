package models.photos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.util.BaseModel;
import models.profiles.Profile;
import models.destinations.Destination;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class PersonalPhoto extends BaseModel {

    /**
     * The instance of the actual photo.
     */
    @JsonIgnore
    @ManyToOne(cascade=CascadeType.ALL)
    private Photo photo;


    /**
     * The profile the the photo is for.
     */
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "profile_id", referencedColumnName="id")
    private Profile profile;


    /**
     * List of destinations that uses this photo
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "photoGallery")
    private List<Destination> destinations;


    /**
     * Boolean depending on if the profile photo is public or not.
     */
    private Boolean isPublic;


    public Photo getPhoto() {
        return photo;
    }


    public void setPhoto(Photo photo) {
        this.photo = photo;
    }


    public Profile getProfile() {
        return profile;
    }


    public void setProfile(Profile profile) {
        this.profile = profile;
    }


    public Boolean getPublic() {
        return isPublic;
    }


    public void setPublic(Boolean privacy) {
        isPublic = privacy;
    }


    /**
     * Checks if an Object is equal to this instance of PersonalPhoto.
     *
     * @param obj   other object which this instance is being compared to.
     * @return      true if this object is equal to obj.
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == this) return true;
        if (!(obj instanceof PersonalPhoto)) return false;

        PersonalPhoto other = (PersonalPhoto) obj;

        return other.getId().equals(this.getId());
    }


    /**
     * Calculates the hashcode of this PersonalPhoto.
     *
     * @return  hashcode of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public void clearDestinations() {
        this.destinations.clear();
    }
}
