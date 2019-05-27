package controllers.photos;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import models.Profile;
import models.photos.PersonalPhoto;
import models.photos.Photo;
import play.libs.Files.TemporaryFile;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.photos.PersonalPhotoRepository;
import repositories.ProfileRepository;
import util.AuthenticationUtil;
import com.typesafe.config.Config;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class PhotoController extends Controller {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final Long MAX_IMG_SIZE = 5000000L;
    private static final String AUTHORIZED = "authorized";
    private static final String NOT_SIGNED_IN = "You are not logged in.";
    private static final String PHOTO_ID = "id";
    private static final String IS_PUBLIC = "public";
    private static final int IMAGE_DIMENSION = 200;

    private ProfileRepository profileRepo;
    private PersonalPhotoRepository personalPhotoRepo;
    private Config config;

    @Inject
    public PhotoController(
            ProfileRepository profileRepo,
            PersonalPhotoRepository personalPhotoRepo,
            Config config) {
        this.profileRepo = profileRepo;
        this.personalPhotoRepo = personalPhotoRepo;
        this.config = config;
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
     * Determines the filepath of where an image should be saved.
     * Checks for a system variable which is only set on the server.
     * If the system variable is set, then it will use that as the filepath.
     * Otherwise, use the specified temp file paths above.
     *
     * @param getThumbnail  whether thumbnail directory is being requested.
     * @return              filepath to save the photo to.
     */
    private String getPhotoFilePath(Boolean getThumbnail) throws IOException {

        String photoEnvironmentName = config.getString("travelea.photos.path.env");

        String mainPath = (photoEnvironmentName != null && System.getenv(photoEnvironmentName) != null
                ? System.getenv(photoEnvironmentName)
                : config.getString("travelea.photos.main"));

        String returnPath = mainPath + (getThumbnail
                ? config.getString("travelea.photos.thumbnail")
                : "");

        Path path = Paths.get(returnPath);

        if (!path.toFile().isDirectory()) {
            Files.createDirectory(path);
        }

        return returnPath;
    }

    /**
     * Returns whether or not a list of uploaded photos are valid.
     *
     * @param photos    list of photos to be validated
     * @return          true if all photo files are valid.
     */
    private boolean validatePhotos(List<Http.MultipartFormData.FilePart<TemporaryFile>> photos) {

        ArrayList<String> validFileTypes = new ArrayList<>();
        validFileTypes.add("image/jpeg");
        validFileTypes.add("image/png");

        for (Http.MultipartFormData.FilePart<TemporaryFile> photo : photos) {
            Long fileSize = photo.getFileSize();
            String contentType = photo.getContentType();

            if (!((fileSize < MAX_IMG_SIZE) && (validFileTypes.contains(contentType))))
                return false;
        }

        return true;
    }

    /**
     * Takes a profile, filename of a previously saved photo and boolean flag.
     * Creates photo object and personal photo object, saves them to profile.
     *
     * @param profileToAdd  profile to add the photo to.
     * @param filename      filename of saved photo.
     * @param isPublic      boolean flag of if photo is public.
     */
    private void addImageToProfile(Profile profileToAdd, String filename, String contentType, Boolean isPublic) throws IOException {
        Photo photoToAdd = new Photo();
        photoToAdd.setMainFilename(getPhotoFilePath(false) + filename);
        photoToAdd.setThumbnailFilename(getPhotoFilePath(true) + filename);
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
     * @return          notFound() (Http 404) if no image exists, forbidden() (Http 403) if the user is not allowed to
     *                  delete the photo, unauthorized() (Http 401) if the user is not logged in, otherwise returns
     *                  badRequest (Http 400).
     */
    public Result destroy(Http.Request request, Long photoId) {

        Integer loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);

        if (loggedInUserId == null) {
            return unauthorized();
        }

        PersonalPhoto photo = personalPhotoRepo.fetch(photoId);

        if (photo == null) {
            return notFound();
        }

        Profile photoOwner = photo.getProfile();
        Profile loggedInUser = profileRepo.fetchSingleProfile(loggedInUserId);

        if (!AuthenticationUtil.validUser(loggedInUser, photoOwner)) {
            return forbidden();
        }
        if (photoOwner != null) {
            personalPhotoRepo.delete(photoOwner, photo);
            return ok();
        }
        return badRequest();
    }

    /**
     * Change the privacy of the selected photo from private to public, or public to private. Public means all users can
     * see the photo, private means only the logged in user or an admin can see the photo.
     *
     * @param request   the Http request body containing the image to change from public to private.
     * @return          ok() (Http 200) if the photo is successfully changed. notFound() (Http 404) if the specified
     *                  photo cannot be found. forbidden() (Http 403) if the person trying to change the privacy of the
     *                  photo is not the owner of the image or an admin. unauthorized() (Http 401) if the user is not
     *                  logged in. internalServerError() (Http 500) if for some reason the photo couldn't be changed.
     */
    public Result changePrivacy(Http.Request request) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(loggedInUserId -> {
                    JsonNode json = request.body().asJson();

                    if (!(json.has(PHOTO_ID) && json.has(IS_PUBLIC))) {
                        return badRequest();
                    }

                    Long personalPhotoId = json.get(PHOTO_ID).asLong();
                    String isPublic = json.get(IS_PUBLIC).asText();

                    Profile loggedInUser = profileRepo.fetchSingleProfile(Integer.valueOf(loggedInUserId));
                    Profile profileToChange;

                    PersonalPhoto personalPhoto = personalPhotoRepo.fetch(personalPhotoId);

                    if (personalPhoto == null) {
                        return notFound();
                    }

                    Profile owner = personalPhoto.getProfile();

                    if (owner == null) {
                        return notFound();
                    }

                    if(AuthenticationUtil.validUser(loggedInUser, owner)) {
                        profileToChange = profileRepo.fetchSingleProfile(owner.getId().intValue());
                    } else {
                        return forbidden();
                    }

                    if (profileToChange != null) {
                        personalPhotoRepo.updatePrivacy(profileToChange, personalPhoto, isPublic);
                        return ok();
                    }
                    return internalServerError("Can't change privacy of photo");
                })
                .orElseGet(() -> unauthorized(NOT_SIGNED_IN)); // User is not logged in
    }

    /**
     * Saves a list of images given in multipart form data in the application.
     * Creates thumbnails for all files. Saves a full sized copy and a thumbnail of each photo.
     *
     * @param profileToAdd  profile to add the photos to
     * @param photos        list of images to add the the profile
     * @return              created() (Http 201) if upload was successful and the Json form of the new profile Photo gallery
     *                      internalServerError() (Http 500) if there was an error with thumbnail creation
     */
    private Result savePhotos(Profile profileToAdd, Collection<Http.MultipartFormData.FilePart<TemporaryFile>> photos) {
        for (Http.MultipartFormData.FilePart<TemporaryFile> photo : photos) {
            TemporaryFile tempFile = photo.getRef();
            String filename = generateFilename();
            try {
                tempFile.copyTo(Paths.get(getPhotoFilePath(false), filename),true);
                saveThumbnail(filename);
                addImageToProfile(profileToAdd, filename, photo.getContentType(), false);
            } catch (IOException e) {
                log.error("Unable to convert image to thumbnail", e);
                return internalServerError("Unable to save image");
            }
        }
        return created(Json.toJson(profileToAdd.getPhotoGallery()));
    }

    /**
     * Takes a multipart form data request to upload an image.
     * Validates all given files in the form data.
     * Adds photos to the profile of the specified userId.
     *
     * @param request   http request containing multipart form data.
     * @param userId    id of the user to add the photos to.
     * @return          created() (Http 201) if successful. badRequest() (Http 400) if photo is invalid, or no profile
     *                  found. forbidden() (Http 403) if the logged in user isn't admin or adding photo for themselves.
     *                  internalServerError() (Http 500) if photo cannot be converted to a thumbnail.
     */
    public Result upload(Http.Request request, Long userId) {
        return request.session()
                .getOptional(AUTHORIZED)
                .map(loggedInUserId -> {
                    Profile loggedInUser = profileRepo.fetchSingleProfile(Integer.valueOf(loggedInUserId));
                    Profile profileToAdd = profileRepo.fetchSingleProfile(userId.intValue());

                    if (profileToAdd == null) {
                        return badRequest(); // User does not exist in the system.
                    }

                    // If user is admin, or if they are editing their own profile then allow them to edit.
                    if (!AuthenticationUtil.validUser(loggedInUser, profileToAdd)) {
                        return forbidden();
                    }

                    Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
                    List<Http.MultipartFormData.FilePart<TemporaryFile>> photos = body.getFiles();

                    // Validate images
                    if (!validatePhotos(photos)) {
                        return badRequest("Invalid image size/type.");
                    }

                    // Images are valid, if we have images, then add them to profile
                    if (!photos.isEmpty()) {
                        return savePhotos(profileToAdd, photos);
                    }

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
        BufferedImage photo = ImageIO.read(new File(getPhotoFilePath(false) + filename));
        BufferedImage croppedImage = makeSquare(photo);
        BufferedImage thumbnail = scale(croppedImage);
        ImageIO.write(thumbnail, "jpg", new File(getPhotoFilePath(true)
                + filename));
    }

    /**
     * Gets a middle section of the image and makes it into a square.
     *
     * @param photo the BufferedImage object of the uploaded image
     * @return      a new BufferedImage subImage object of the square section of the image.
     */
    private BufferedImage makeSquare(BufferedImage photo) {
        int width = photo.getWidth();
        int height = photo.getHeight();
        int size = Math.min(width, height);

        return photo.getSubimage((width/2) - (size/2), (height/2) - (size/2), size, size);
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
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
        BufferedImage scaledImage = graphicsConfiguration.createCompatibleImage(IMAGE_DIMENSION, IMAGE_DIMENSION);

        Graphics2D newGraphicsImage = scaledImage.createGraphics();
        newGraphicsImage.setColor(Color.white);
        newGraphicsImage.fillRect(0, 0, IMAGE_DIMENSION, IMAGE_DIMENSION);

        double xScale = (double) IMAGE_DIMENSION / sourceImage.getWidth();
        double yScale = (double) IMAGE_DIMENSION / sourceImage.getHeight();
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
                    Profile owner = personalPhoto.getProfile();

                    if(AuthenticationUtil.validUser(loggedInUser, owner))
                        return getImageResult(personalPhoto.getPhoto(), getThumbnail);

                    return forbidden();
                }).orElseGet(() -> unauthorized());
    }

}
