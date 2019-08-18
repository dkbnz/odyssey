package steps;

import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import models.Profile;
import models.destinations.Destination;
import models.photos.PersonalPhoto;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import repositories.ProfileRepository;
import repositories.destinations.DestinationRepository;
import repositories.photos.PersonalPhotoRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

import static play.mvc.Http.HttpVerbs.PATCH;
import static play.test.Helpers.*;


public class PhotoTestSteps {

    /**
     * Singleton class which stores generally used variables
     */
    private TestContext testContext = TestContext.getInstance();




    private static final String AUTHORIZED = "authorized";
    private static final String PHOTO_URI = "/v1/photos/";
    private static final String CHANGE_PHOTO_PRIVACY_URI = "/v1/photos";
    private static final String PROFILE_PHOTO_URI = "/v1/profilePhoto/";
    private static final String DESTINATION_PHOTO_URI = "/v1/destinationPhotos/";


    private final Logger log = LoggerFactory.getLogger(this.getClass());


    private PersonalPhotoRepository personalPhotoRepository =
            testContext.getApplication().injector().instanceOf(PersonalPhotoRepository.class);
    private DestinationRepository destinationRepository =
            testContext.getApplication().injector().instanceOf(DestinationRepository.class);
    private ProfileRepository profileRepository =
            testContext.getApplication().injector().instanceOf(ProfileRepository.class);


    /**
     * Creates a Json ObjectNode to be used for the photo.
     *
     * @param photoId  the photoId of the photo to be added to the Json node.
     * @param isPublic the boolean value for the photo privacy to be added to the Json node.
     * @return         the Json ObjectNode for the new photo.
     */
    private JsonNode createJson(int photoId, boolean isPublic) {
        // complex json
        ObjectMapper mapper = new ObjectMapper();

        //Add values to a JsonNode
        ObjectNode json = mapper.createObjectNode();

        json.put("id", photoId);
        json.put("public", isPublic);

        return json;
    }

    /**
     * Creates a Json ObjectNode to be used for the destination photo.
     *
     * @param photoId the photoId of the photo to be added to the Json node.
     * @return        the Json ObjectNode for the new destination photo.
     */
    private JsonNode createDestinationPhotoJson(int photoId) {
        // complex json
        ObjectMapper mapper = new ObjectMapper();

        //Add values to a JsonNode
        ObjectNode json = mapper.createObjectNode();

        json.put("id", photoId);

        return json;
    }


    @Given("^a user exists in the database with the username \"(.*)\" and id number (\\d+)$")
    public void aUserExistsInTheDatabaseWithTheUsernameAndId(String username, Integer id) {
        Profile profile = profileRepository.findById(id.longValue());
        Assert.assertNotNull(profile);
        Assert.assertEquals(profile.getUsername(), username);
    }

    @Given("^a photo exists with id (\\d+)$")
    public void photoExistsInDatabase(Integer id) {
        PersonalPhoto photo = personalPhotoRepository.findById(id.longValue());
        Assert.assertNotNull(photo);
    }


    @Given("^the destination with id (\\d+) exists$")
    public void theDestinationWithIdExists(Integer destinationId) {
        Destination destination = destinationRepository.findById(destinationId.longValue());
        Assert.assertNotNull(destination);
        Assert.assertEquals(destination.getId().toString(), destinationId.toString());
    }


    @Given("^the destination with id (\\d+) has a photo with id (\\d+)$")
    public void theDestinationWithIdHasAPhotoWithId(Integer destinationId, Integer photoId) {
        Destination destination = destinationRepository.findById(destinationId.longValue());
        PersonalPhoto photo = personalPhotoRepository.findById(photoId.longValue());
        Assert.assertNotNull(destination);
        Assert.assertNotNull(photo);
        Assert.assertTrue(destination.getPhotoGallery().contains(photo));
    }


    @When("^I upload a valid jpeg photo to a profile with id (\\d+)$")
    public void iUploadAValidJpegPhotoToMyOwnProfile(int uploadUserId) throws IOException {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        File file = new File("image.png");

        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            log.error("Unable to create test file", e);
        }

        Http.MultipartFormData.Part<Source<ByteString, ?>> part =
                new Http.MultipartFormData.FilePart<>(
                        "photo0",
                        "helloworld.jpg",
                        "image/jpeg",
                        FileIO.fromPath(file.toPath()),
                        Files.size(file.toPath()));

        Http.RequestBuilder request =
                Helpers.fakeRequest()
                        .uri(PHOTO_URI + uploadUserId)
                        .method(POST)
                        .bodyRaw(
                                Collections.singletonList(part),
                                play.libs.Files.singletonTemporaryFileCreator(),
                                testContext.getApplication().asScala().materializer())
                        .session(AUTHORIZED, testContext.getLoggedInId());

        Result createPhotoResult = route(testContext.getApplication(), request);
        testContext.setStatusCode(createPhotoResult.status());

        if (!file.delete())
            log.error("Unable to delete test file");

    }


    @When("^I change the privacy of the photo with id (\\d+) to public (.*)$")
    public void iChangeThePrivacyOfThePhoto(int photoId, String isPublic) {
        JsonNode json = createJson(photoId, Boolean.parseBoolean(isPublic));
        Http.RequestBuilder request =
                Helpers.fakeRequest()
                        .uri(CHANGE_PHOTO_PRIVACY_URI)
                        .method(PATCH)
                        .bodyJson(json)
                        .session(AUTHORIZED, testContext.getLoggedInId());
        Result changePhotoPrivacyResult = route(testContext.getApplication(), request);

        testContext.setStatusCode(changePhotoPrivacyResult.status());
    }


    @When("^I delete the photo with id (\\d+)$")
    public void iDeleteThePhotoWithId(int photoId) {
        Http.RequestBuilder request =
                Helpers.fakeRequest()
                        .uri(PHOTO_URI + photoId)
                        .method(DELETE)
                        .session(AUTHORIZED, testContext.getLoggedInId());
        Result changePhotoPrivacyResult = route(testContext.getApplication(), request);

        testContext.setStatusCode(changePhotoPrivacyResult.status());
    }


    @When("^I delete a profile picture of profile (\\d+)$")
    public void iDeleteAProfilePicture(int userId) {
        Http.RequestBuilder request =
                Helpers.fakeRequest()
                        .uri(PROFILE_PHOTO_URI + userId)
                        .method(DELETE)
                        .session(AUTHORIZED, testContext.getLoggedInId());
        Result changePhotoPrivacyResult = route(testContext.getApplication(), request);

        testContext.setStatusCode(changePhotoPrivacyResult.status());
    }


    @When("^I set a profile photo from their photo Gallery with id (\\d+)$")
    public void iSetAProfilePhotoFromTheirPhotoGalleryWithId(Integer photoId) {
        Http.RequestBuilder request =
                Helpers.fakeRequest()
                        .uri(PROFILE_PHOTO_URI + photoId)
                        .method(PUT)
                        .session(AUTHORIZED, testContext.getLoggedInId());

        Result changeProfilePhotoResult = route(testContext.getApplication(), request);
        testContext.setStatusCode(changeProfilePhotoResult.status());
    }


    @When("^I add a photo with id (\\d+) to a destination with id (\\d+)$")
    public void iAddAPhotoWithIdToADestinationWithId(Integer photoId, Integer destinationId) {
        JsonNode json = createDestinationPhotoJson(photoId);
        Http.RequestBuilder request =
                Helpers.fakeRequest()
                        .uri(DESTINATION_PHOTO_URI + destinationId)
                        .method(POST)
                        .bodyJson(json)
                        .session(AUTHORIZED, testContext.getLoggedInId());

        Result addDestinationPhotoResult = route(testContext.getApplication(), request);
        testContext.setStatusCode(addDestinationPhotoResult.status());
    }


    @When("^I remove a photo with id (\\d+) from a destination with id (\\d+)$")
    public void iRemoveAPhotoWithIdFromADestinationWithId(Integer photoId, Integer destinationId) {
        JsonNode json = createDestinationPhotoJson(photoId);
        Http.RequestBuilder request =
                Helpers.fakeRequest()
                        .uri(DESTINATION_PHOTO_URI + destinationId)
                        .method(DELETE)
                        .bodyJson(json)
                        .session(AUTHORIZED, testContext.getLoggedInId());

        Result addDestinationPhotoResult = route(testContext.getApplication(), request);
        testContext.setStatusCode(addDestinationPhotoResult.status());
    }
}
