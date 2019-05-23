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
import repositories.PhotoRepository;
import repositories.ProfileRepository;

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
    private PhotoRepository photoRepo;

    @Inject
    public PhotoController(
            ProfileRepository profileRepo,
            PhotoRepository photoRepo
                           ) {
        this.profileRepo = profileRepo;
        this.photoRepo = photoRepo;
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
     * Returns whether or not an uploaded image is valid.
     *
     * @param fileSize      file size in bytes of subject image.
     * @param contentType   content type of uploaded file.
     * @return              true if file is valid.
     */
    private boolean validImage(Long fileSize, String contentType) {
        ArrayList<String> validFileTypes = new ArrayList<>();
        validFileTypes.add("image/jpeg");
        validFileTypes.add("image/png");

        return (fileSize < MAX_IMG_SIZE) && (validFileTypes.contains(contentType));
    }

    /**
     * Takes a profile, filename of a previously saved image and boolean flag.
     * Creates photo object and personal photo object, saves them to profile.
     *
     * @param profileToAdd  profile to add the photo to.
     * @param filename      filename of saved photo.
     * @param isPublic      boolean flag of if photo is public.
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
     * Validates all given files in the form data.
     * Creates thumbnails for all files. Saves a full sized copy and a thumbnail of each photo.
     * Adds photos to the profile of the specified userId.
     *
     * TODO: reduce the cognitive complexity of this method.
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
                    Profile loggedInUser = Profile.find.byId(Integer.valueOf(loggedInUserId));
                    Profile profileToAdd;

                    // If user is admin, or if they are editing their own profile then allow them to edit.
                    if(loggedInUser.getIsAdmin() || userId.equals(Long.valueOf(loggedInUserId))) {
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
                    // Name field of the HTML data
                    List<Http.MultipartFormData.FilePart<TemporaryFile>> pictures = body.getFiles();

                    if (!pictures.isEmpty()) {
                        Collection<TemporaryFile> picturesToAdd = new ArrayList<>();

                        for (Http.MultipartFormData.FilePart<TemporaryFile> picture : pictures) {
                            String fileName = picture.getFilename();
                            Long fileSize = picture.getFileSize();
                            String contentType = picture.getContentType();

                            if (!validImage(fileSize, contentType))
                                return badRequest("Invalid Image: " + fileName);

                            picturesToAdd.add(picture.getRef());
                        }

                        for (TemporaryFile tempFile : picturesToAdd) {
                            String filename = generateFilename();

                            tempFile.copyTo(Paths.get(IMAGE_DIRECTORY, filename), true);

                            try {
                                BufferedImage img = ImageIO.read(new File(IMAGE_DIRECTORY + filename));
                                BufferedImage croppedImage = makeSquare(img);
                                BufferedImage thumbnail = scale(croppedImage);
                                ImageIO.write(thumbnail, "jpg", new File(THUMBNAIL_DIRECTORY
                                        + filename));
                            } catch (IOException e) {
                                log.error("Unable to convert image to thumbnail", e);
                                return internalServerError("Unable to convert image to thumbnail");
                            }

                            addImageToProfile(profileToAdd, filename, true);
                        }

                        return created("Files uploaded");
                    } else {
                        return badRequest();
                    }
                })
                .orElseGet(() -> unauthorized(NOT_SIGNED_IN)); // User is not logged in
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

}
