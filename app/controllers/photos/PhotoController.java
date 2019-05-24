package controllers.photos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import models.Profile;
import models.photos.PersonalPhoto;
import models.photos.Photo;
import play.libs.Files.TemporaryFile;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.photos.PersonalPhotoRepository;
import repositories.ProfileRepository;
import util.AuthenticationUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.inject.Inject;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class PhotoController extends Controller {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String IMAGE_DIRECTORY = "temp/";
    private static final String THUMBNAIL_DIRECTORY = "temp/thumbnail/";
    private static final Long MAX_IMG_SIZE = 5000000L;
    private static final String AUTHORIZED = "authorized";
    private static final String NOT_SIGNED_IN = "You are not logged in.";

    private ProfileRepository profileRepo;
    private PersonalPhotoRepository personalPhotoRepo;

    @Inject
    public PhotoController(
            ProfileRepository profileRepo,
            PersonalPhotoRepository personalPhotoRepo
                           ) {
        this.profileRepo = profileRepo;
        this.personalPhotoRepo = personalPhotoRepo;
    }

    /**
     * Generates a UUID for a filename.
     *
     * @return universally unique identifier for saving a file.
     */
    private String generateFilename() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * Returns whether or not a list of uploaded images are valid.
     *
     * @param pictures  list of pictures to be validated
     * @return          true if all image files are valid.
     */
    private boolean validateImages(List<Http.MultipartFormData.FilePart<TemporaryFile>> pictures) {

        ArrayList<String> validFileTypes = new ArrayList<>();
        validFileTypes.add("image/jpeg");
        validFileTypes.add("image/png");

        for (Http.MultipartFormData.FilePart<TemporaryFile> picture : pictures) {
            Long fileSize = picture.getFileSize();
            String contentType = picture.getContentType();

            if (!((fileSize < MAX_IMG_SIZE) && (validFileTypes.contains(contentType))))
                return false;
        }

        return true;
    }

    /**
     * Takes a profile, filename of a previously saved image and boolean flag.
     * Creates photo object and personal photo object, saves them to profile.
     *
     * @param profileToAdd  profile to add the photo to.
     * @param filename      filename of saved photo.
     * @param isPublic      boolean flag of if photo is public.
     */
    private void addImageToProfile(Profile profileToAdd, String filename, String contentType, Boolean isPublic) {
        Photo photoToAdd = new Photo();
        photoToAdd.setMainFilename(IMAGE_DIRECTORY + filename);
        photoToAdd.setThumbnailFilename(THUMBNAIL_DIRECTORY + filename);
        photoToAdd.setContentType(contentType);
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
     * Deletes a photo from a specified user, based on the id number of the photo.
     *
     * @param request   the Http request body.
     * @param photoId   the ID number of the photo to be deleted.
     * @return          notFound() (Http 404) if no image exists, badRequest() (Http 400) if the id number of the photo
     *                  owner is not th
     */
//    public Result destroy(Http.Request request, Long photoId) {
//
//        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
//
//        if (loggedInUserId == null) {
//            return unauthorized();
//        }
//
//        PersonalPhoto photo = personalPhotoRepo.fetch(photoId);
//
//        if (photo == null) {
//            return notFound();
//        }
//
//        Profile photoOwner = photo.getProfile();
//        Profile loggedInUser = ProfileRepository.fetchSingleProfile(loggedInUserId);
//
//        if (!AuthenticationUtil.validUser(loggedInUser, photoOwner)) {
//            return forbidden();
//        }
//
//        personalPhotoRepo.delete(photoOwner, photo);
//
//        return ok();
//    }


    /**
     * Saves a list of images given in multipart form data in the application.
     * Creates thumbnails for all files. Saves a full sized copy and a thumbnail of each photo.
     *
     * @param profileToAdd  profile to add the photos to
     * @param pictures      list of pictures to add the the profile
     * @return              created() (Http 201) if upload was successful.
     *                      internalServerError() (Http 500) if there was an error with thumbnail creation
     */
    private Result saveImages(Profile profileToAdd, Collection<Http.MultipartFormData.FilePart<TemporaryFile>> pictures) {
        for (Http.MultipartFormData.FilePart<TemporaryFile> picture : pictures) {
            TemporaryFile tempFile = picture.getRef();
            String filename = generateFilename();
            tempFile.copyTo(Paths.get(IMAGE_DIRECTORY, filename), true);
            try {
                saveThumbnail(filename);
            } catch (IOException e) {
                log.error("Unable to convert image to thumbnail", e);
                return internalServerError("Unable to convert image to thumbnail");
            }
            addImageToProfile(profileToAdd, filename, picture.getContentType(), true);
        }
        return created("Files uploaded");
    }

    /**
     * Takes a multipart form data request to upload an image.
     * Validates all given files in the form data.
     * Adds photos to the profile of the specified userId.
     *
     * @param request   http request containing multipart form data.
     * @param userId    id of the user to add the photos to.
     * @return          created() (Http 201) if successful. badRequest() (Http 400) if image is invalid, or no profile
     *                  found. forbidden() (Http 403) if the logged in user isn't admin or adding photo for themselves.
     *                  internalServerError() (Http 500) if image cannot be converted to a thumbnail.
     */
    public Result upload(Http.Request request, Long userId) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(loggedInUserId -> {
                    Profile loggedInUser = profileRepo.fetchSingleProfile(Integer.valueOf(loggedInUserId));
                    Profile profileToAdd;

                    // If user is admin, or if they are editing their own profile then allow them to edit.
                    if(AuthenticationUtil.validUser(loggedInUser, userId)) {
                        profileToAdd = profileRepo.fetchSingleProfile(userId.intValue());
                    } else if (!userId.equals(Long.valueOf(loggedInUserId))) {
                        return forbidden(); // User has specified an id which is not their own, but is not admin
                    } else {
                        return badRequest();
                    }

                    if (profileToAdd == null) {
                        return badRequest(); // User does not exist in the system.
                    }

                    Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
                    List<Http.MultipartFormData.FilePart<TemporaryFile>> pictures = body.getFiles();

                    // Validate images
                    if (!validateImages(pictures))
                        return badRequest("Invalid image size/type.");

                    // Images are valid, if we have images, then add them to profile
                    if (!pictures.isEmpty())
                        return saveImages(profileToAdd, pictures);

                    // Images are empty
                    return badRequest("No images to upload.");
                })
                .orElseGet(() -> unauthorized(NOT_SIGNED_IN)); // User is not logged in
    }

    /**
     * Takes a filename of a previously saved image and creates a thumbnail from it.
     * After creation, it will save into the specified thumbnail directory.
     *
     * @param filename      filename of the fullsized image to create a thumbnail from.
     * @throws IOException  if there is an error with saving the thumbnail.
     */
    private void saveThumbnail(String filename) throws IOException {
        BufferedImage img = ImageIO.read(new File(IMAGE_DIRECTORY + filename));
        BufferedImage croppedImage = makeSquare(img);
        BufferedImage thumbnail = scale(croppedImage);
        ImageIO.write(thumbnail, "jpg", new File(THUMBNAIL_DIRECTORY
                + filename));
    }

    /**
     * Gets a middle section of the image and makes it into a square.
     *
     * @param img   the BufferedImage object of the uploaded image
     * @return      a new BufferedImage subImage object of the square section of the image.
     */
    private BufferedImage makeSquare(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int size = Math.min(width, height);

        return img.getSubimage((width/2) - (size/2), (height/2) - (size/2), size, size);
    }

    /**
     * Scales a BufferedImage object to a 200x200 pixels image, with lower quality to be stored as a thumbnail. Uses
     * the Graphics2D class to do this. A new image is created using the GraphicsEnvironment, GraphicsDevice and
     * GraphicsConfiguration. A new Graphics2D object is then created, and filled with a white background in case of
     * transparent images. The image is then scaled and transformed using the AffineTransformation class.
     *
     * @param sourceImage the BufferedImage to be scaled down.
     * @return            a new BufferedImage scaled to the appropriate size.
     */
    private BufferedImage scale(BufferedImage sourceImage) {
        int width = 200;
        int height = 200;

        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
        BufferedImage scaledImage = graphicsConfiguration.createCompatibleImage(width, height);

        Graphics2D newGraphicsImage = scaledImage.createGraphics();
        newGraphicsImage.setColor(Color.white);
        newGraphicsImage.fillRect(0, 0, width, height);

        double xScale = (double) width / sourceImage.getWidth();
        double yScale = (double) height / sourceImage.getHeight();
        AffineTransform affineTransform = AffineTransform.getScaleInstance(xScale,yScale);
        newGraphicsImage.drawRenderedImage(sourceImage, affineTransform);
        newGraphicsImage.dispose();

        return scaledImage;
    }

    /**
     * Retrieves an image file from a path specified in the given photo object.
     * If getThumbnail is true, it will return the thumbnail version from the given photo object.
     *
     * @param photoToRetrieve   photo object containing the filepath to get the image from.
     * @param getThumbnail      boolean to specify if a thumbnail version is required.
     * @return                  result containing an image file.
     */
    private Result getImageResult(Photo photoToRetrieve, Boolean getThumbnail) {

        String contentType = photoToRetrieve.getContentType();
        // If get thumbnail is true, set filename to thumbnail filename, otherwise set it to main filename
        String filename = getThumbnail
                ? photoToRetrieve.getThumbnailFilename()
                : photoToRetrieve.getMainFilename();

        return ok(new File(filename)).as(contentType);
    }

    /**
     * Fetches a personal photo from the application based on the specified Id.
     *
     * @param request           http request from the client.
     * @param personalPhotoId   id of the personal photo to be returned.
     * @param getThumbnail      boolean to dictate if a thumbnail is to be returned.
     * @return                  unauthorized() (Http 401) if a user is not logged in.
     *                          forbidden() (Http 403) if a user is requesting a resource they do not have access to.
     *                          ok() (Http 200) containing the image if user is authorized to receive it.
     */
    public Result fetch(Http.Request request, Long personalPhotoId, Boolean getThumbnail) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(userId -> {

                    PersonalPhoto personalPhoto = personalPhotoRepo.fetch(personalPhotoId);

                    if (personalPhoto == null)
                        return notFound("Image could not be found.");

                    if (personalPhoto.getPublic())
                        return getImageResult(personalPhoto.getPhoto(), getThumbnail);

                    Profile loggedInUser = profileRepo.fetchSingleProfile(Integer.valueOf(userId));
                    Long ownerId = personalPhoto.getProfile().getId();

                    if(AuthenticationUtil.validUser(loggedInUser, ownerId))
                        return getImageResult(personalPhoto.getPhoto(), getThumbnail);

                    return forbidden();
                }).orElseGet(() -> unauthorized());
    }

}
