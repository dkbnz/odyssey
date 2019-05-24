package steps;

import akka.http.impl.util.JavaMapping;
import akka.util.ByteString;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Source;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import models.Profile;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import play.Application;
import play.api.mvc.MultipartFormData;
import play.db.Database;
import play.db.evolutions.Evolutions;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;

import akka.stream.Materializer;

import static play.test.Helpers.*;


public class PhotoTestSteps {

    @Inject
    Application application;
    protected Database database;

    private static final String AUTHORIZED = "authorized";
    private int statusCode;
    private static final String UPLOAD_PHOTOS_URI = "/v1/photos/";
    private static final String SINGLE_PROFILE_URI = "/v1/profile";
    private static final String LOGIN_URI = "/v1/login";
    private static final String USERNAME = "username";
    private static final String PASS_FIELD = "password";

    /**
     * A valid username for login credentials for admin user.
     */
    private static final String VALID_USERNAME = "admin@travelea.com";


    /**
     * A valid password for login credentials for admin user.
     */
    private static final String VALID_AUTHPASS = "admin1";
    private static final String ADMIN_ID = "1";

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    /**
     * A valid username for login credentials for a regular user.
     */
    private static final String REG_USER = "guestUser@travelea.com";


    /**
     * A valid password for login credentials for a regular user.
     */
    private static final String REG_PASS = "guest123";
    private static final String REG_ID = "2";


    private String userId;

    @Before
    public void setUp() {
        Map<String, String> configuration = new HashMap<>();
        configuration.put("play.db.config", "db");
        configuration.put("play.db.default", "default");
        configuration.put("db.default.driver", "org.h2.Driver");
        configuration.put("db.default.url", "jdbc:h2:mem:testDBPhotos;MODE=MYSQL;");
        configuration.put("ebean.default", "models.*");
        configuration.put("play.evolutions.db.default.enabled", "true");
        configuration.put("play.evolutions.autoApply", "false");

        //Set up the fake application to use the in memory database config
        application = fakeApplication(configuration);

        database = application.injector().instanceOf(Database.class);
        applyEvolutions();

        Helpers.start(application);
    }

    /**
     * Applies down evolutions to the database from the test/evolutions/default directory.
     *
     * This drops tables and data from the database.
     */
    private void applyEvolutions() {
        Evolutions.applyEvolutions(
                database,
                Evolutions.fromClassLoader(
                        getClass().getClassLoader(),
                        "test/"
                )
        );
    }

    /**
     * Runs after each test scenario.
     * Cleans up the database by cleaning up evolutions and shutting it down.
     * Stops running the fake application.
     */
    @After
    public void tearDown() {
        cleanEvolutions();
        database.shutdown();
        Helpers.stop(application);
    }

    /**
     * Applies up evolutions to the database from the test/evolutions/default directory.
     *
     * This populates the database with necessary tables and values.
     */
    private void cleanEvolutions() {
        Evolutions.cleanupEvolutions(database);
    }


    /**
     * Sends a fake request to the application to login.
     * @param username      The string of the username to complete the login with.
     * @param password      The string of the password to complete the login with.
     */
    private void loginRequest(String username, String password) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.createObjectNode();

        ((ObjectNode) json).put("username", username);
        ((ObjectNode) json).put("password", password);

        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .bodyJson(json)
                .uri(LOGIN_URI);
        Result loginResult = route(application, request);

        statusCode = loginResult.status();
    }

    /**
     * Gets the response as an iterator array Node from any fake request so that you can iterate over the response data
     * @param content the string of the result using helper content as string
     * @return an Array node iterator
     */
    private Iterator<JsonNode> getTheResponseIterator(String content) {
        JsonNode arrNode = null;
        try {
            arrNode = new ObjectMapper().readTree(content);
        } catch (IOException e) {
            log.error("Unable to get response iterator for fake request.", e);
        }
        return arrNode.elements();
    }

    private JsonNode createJson() {
        // complex json
        ObjectMapper mapper = new ObjectMapper();

        //Add values to a JsonNode
        JsonNode json = mapper.createObjectNode();
        ObjectNode photoNode = mapper.createObjectNode();
        photoNode.put("id", 1);
        photoNode.put("mainFilename", "temp/1e699f1f-dc7f-448c-a673-dc49c30cee65");
        photoNode.put("thumbnailFilename", "temp/1e699f1f-dc7f-448c-a673-dc49c30cee65");
        photoNode.put("uploadDate", "2019-05-24");

        ((ObjectNode) json).put("id", 1);
        ((ObjectNode) json).putArray("photos").add(photoNode);
        ((ObjectNode) json).put("public", true);

        return json;
    }

    @Given("I am logged in as an admin with id {int}")
    public void anAdminIsLoggedIn(int loggedInId) {
        loginRequest(VALID_USERNAME, VALID_AUTHPASS);
        Assert.assertEquals(OK, statusCode);
    }

    @Given("I am logged in as a non-admin with id {int}")
    public void iAmLoggedInAsARegularUser(int loggedInId) {
        loginRequest(REG_USER, REG_PASS);
        Assert.assertEquals(OK, statusCode);
        userId = REG_ID;
    }

    @Given("I have a application running")
    public void theApplicationIsRunning() throws BeansException {
        Assert.assertTrue(application.isTest());
    }


    @Given("a user exists in the database with the username {string} and id {int}")
    public void aUserExistsInTheDatabaseWithTheUsernameAndId(String username, Integer id) {
        Profile profile = Profile.find.byId(id);
        Assert.assertNotNull(profile);
        Assert.assertEquals(profile.getUsername(), username);
    }


    @When("I upload a valid jpeg photo to a profile with id {int}")
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
                        .uri(UPLOAD_PHOTOS_URI + uploadUserId)
                        .method("POST")
                        .bodyRaw(
                                Collections.singletonList(part),
                                play.libs.Files.singletonTemporaryFileCreator(),
                                application.asScala().materializer())
                        .session(AUTHORIZED, REG_ID);

        Result createPhotoResult = route(application, request);
        statusCode = createPhotoResult.status();
    }


    @Then("the status code I get is Created")
    public void theStatusCodeIsCreated() {
        Assert.assertEquals(CREATED, statusCode);
    }

    @Then("the status code I get is Forbidden")
    public void theStatusCodeIGetIsForbidden() {
        Assert.assertEquals(FORBIDDEN, statusCode);
    }


}
