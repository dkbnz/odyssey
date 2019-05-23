package controllers.photos;

import models.Profile;
import models.photos.PersonalPhoto;
import models.photos.Photo;
import play.libs.Files.TemporaryFile;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.PhotoRepository;
import repositories.ProfileRepository;

import javax.inject.Inject;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PhotoController extends Controller {
    private static final String IMAGE_DIRECTORY = "temp/";
    private static final String THUMBNAIL_DIRECTORY = "temp/thumbnail/";

    ProfileRepository profileRepo;
    PhotoRepository photoRepo;

    @Inject
    public PhotoController(
            ProfileRepository profileRepo,
            PhotoRepository photoRepo
                           ) {
        this.profileRepo = profileRepo;
        this.photoRepo = photoRepo;
    }

    /**
     *
     * @return
     */
    private String generateFilename() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * Returns whether or not an uploaded image is valid.
     * @param fileSize
     * @param contentType
     * @return
     */
    private boolean validImage(Long fileSize, String contentType) {
        return (fileSize < 1000L) && (contentType.equals("image/jpeg"));
    }

    /**
     *
     * @param profileToAdd
     * @param filename
     * @param isPublic
     */
    private void addImageToProfile(Profile profileToAdd, String filename, Boolean isPublic) {
        Photo photoToAdd = new Photo();
        photoToAdd.setMainFilename(IMAGE_DIRECTORY + filename);
        photoToAdd.setThumbnailFilename(THUMBNAIL_DIRECTORY + filename);
        photoToAdd.setUploadDate(LocalDate.now());
        photoToAdd.setUploadProfile(profileToAdd);

        PersonalPhoto personalPhoto = new PersonalPhoto();
        personalPhoto.setPhoto(photoToAdd);
        personalPhoto.setPublic(isPublic);
        personalPhoto.setProfile(profileToAdd);

        profileToAdd.addPhotoToGallery(personalPhoto);
        profileRepo.save(profileToAdd);
    }

    /**
     * Takes a multipart form data request to upload an image.
     *
     * @param request
     * @param userId
     * @return
     */
    public Result upload(Http.Request request, Long userId) {

        Profile profileToAdd = profileRepo.fetchSingleProfile(userId.intValue());

        System.out.println(profileToAdd.getFirstName());

        Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
        List<Http.MultipartFormData.FilePart<TemporaryFile>> pictures = body.getFiles(); // Name field of the HTML data

        if (!pictures.isEmpty()) {
            Collection<TemporaryFile> picturesToAdd = new ArrayList<TemporaryFile>();

            // Validate images
            for (Http.MultipartFormData.FilePart<TemporaryFile> picture : pictures) {
                String fileName = picture.getFilename();
                Long fileSize = picture.getFileSize();
                String contentType = picture.getContentType();

                if (!validImage(fileSize, contentType))
                    return badRequest("Invalid Image: " + fileName);

                picturesToAdd.add(picture.getRef());
            }

            // Crop Images

            // Save images
            for (TemporaryFile tempFile : picturesToAdd) {

                String filename = generateFilename();
                tempFile.copyTo(Paths.get("temp/", filename), true);
                addImageToProfile(filename, );
            }
            TemporaryFile file = picture.getRef();
            return ok("Files uploaded");
        } else {
            return badRequest();
        }
    }

}
