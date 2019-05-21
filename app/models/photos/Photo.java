package models.photos;

import io.ebean.Finder;
import models.BaseModel;
import models.Profile;
import play.data.validation.Constraints;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

/**
 * Class for holding a photo
 */
@Entity
public class Photo extends BaseModel {

    /**
     * The randomly generated filename of the photo.
     */
    private String photoFilename;

    /**
     * The randomly generated filename of the photo's thumbnail.
     */
    private String thumbnailFilename;

    /**
     * The date the photo was uploaded.
     */
    private LocalDate uploadDate;

    /**
     * The profile ID that the photo belongs to.
     */
    @ManyToOne
    private Profile profile;


    public String getPhotoFilename() {
        return photoFilename;
    }

    public void setPhotoFilename(String photoFilename) {
        this.photoFilename = photoFilename;
    }

    public String getThumbnailFilename() {
        return thumbnailFilename;
    }

    public void setThumbnailFilename(String thumbnailFilename) {
        this.thumbnailFilename = thumbnailFilename;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }


}
