package models.photos;

import models.BaseModel;
import models.Profile;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class ProfilePhoto extends BaseModel {

    /**
     * The instance of the specific photo.
     */
    private Photo photo;

    /**
     * Boolean depending on if the profile photo is public or not.
     */
    private Boolean isPublic;

}
