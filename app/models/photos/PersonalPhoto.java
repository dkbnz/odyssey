package models.photos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import models.BaseModel;
import models.Profile;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class PersonalPhoto extends BaseModel {

    /**
     * The instance of the actual photo.
     */
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

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public static final Finder<Integer, PersonalPhoto> find = new Finder<>(PersonalPhoto.class);

    public static Finder<Integer, PersonalPhoto> getFind() {
        return find;
    }
}
