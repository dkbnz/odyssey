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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PhotoController extends Controller {

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
                long fileSize = picture.getFileSize();
                String contentType = picture.getContentType();

                if (!validImage(fileSize, contentType))
                    return badRequest("Invalid Image: " + fileName);

                picturesToAdd.add(picture.getRef());
            }

            // Crop Images

            // Save images
            for (TemporaryFile tempFile : picturesToAdd) {

                addImageToProfile();

                Photo photoToAdd = new Photo();
                photoRepo.save(photoToAdd);
                Long photoId = photoToAdd.getId();

                PersonalPhoto personalPhoto = new PersonalPhoto();
                personalPhoto.setPhoto(photoToAdd);

                tempFile.
            }





            TemporaryFile file = picture.getRef();
            file.copyTo(Paths.get("temp/"), true);
            return ok("Files uploaded");
        } else {
            return badRequest();
        }
    }

}
