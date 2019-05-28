package models.photos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import models.BaseModel;
import models.Profile;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

/**
 * Class for holding a photo
 */
@Entity
public class Photo extends BaseModel {

    /**
     * The randomly generated filename of the photo.
     */
    @JsonIgnore
    private String mainFilename;

    /**
     * The randomly generated filename of the photo's thumbnail.
     */
    @JsonIgnore
    private String thumbnailFilename;

    /**
     * Content type of the saved image
     */
    @JsonIgnore
    private String contentType;

    /**
     * The date the photo was uploaded.
     */
    @JsonIgnore
    private LocalDate uploadDate;

    /**
     * The profile that added the photo.
     */
    @JsonIgnore
    @ManyToOne
    private Profile uploadProfile;

    public String getMainFilename() {
        return mainFilename;
    }

    public void setMainFilename(String mainFilename) {
        this.mainFilename = mainFilename;
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

    public Profile getUploadProfile() {
        return uploadProfile;
    }

    public void setUploadProfile(Profile uploadProfile) {
        this.uploadProfile = uploadProfile;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public static final Finder<Integer, Photo> find = new Finder<>(Photo.class);

    public static Finder<Integer, Photo> getFind() {
        return find;
    }
}
