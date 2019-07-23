package models.photos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import models.BaseModel;
import models.Profile;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class PersonalPhoto extends BaseModel {

    /**
     * The instance of the actual photo.
     */
    @JsonIgnore
    @ManyToOne(cascade=CascadeType.PERSIST)
    private Photo photo;


    /**
     * The profile the the photo is for.
     */
    @JsonIgnore
    @ManyToOne
    private Profile profile;


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


    public static final Finder<Integer, PersonalPhoto> find = new Finder<>(PersonalPhoto.class);


    public static Finder<Integer, PersonalPhoto> getFind() {
        return find;
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

        return other.getId() == this.getId();
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
}
