package controllers;

import models.Profile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import repositories.ProfileRepository;
import util.AuthenticationUtil;

import play.mvc.Http;

import static org.mockito.Mockito.mock;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.GET;


public class AuthenticationUtilTest {

    private static final long ADMIN_ID = 12L;
    private static final long OWNER_ID = 17L;
    private static final String LOGGED_IN_ID = "1";
    private static final String AUTHORIZED = "authorized";
    private static final String PROFILES_URI = "/v1/profiles";


    private ProfileRepository mockProfileRepository;

    @Before
    public void setUp() {
        mockProfileRepository = mock(ProfileRepository.class);
    }


    @Test
    public void validUserTestIsAdmin() {
        //Arrange
        Profile admin = new Profile();
        admin.setId(ADMIN_ID);
        admin.setIsAdmin(true);

        Profile owner = new Profile();
        owner.setId(OWNER_ID);

        //Act
        boolean result = AuthenticationUtil.validUser(admin, owner);

        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void validUserTestIsNotAdmin() {
        //Arrange
        Profile notAdmin = new Profile();
        notAdmin.setId(ADMIN_ID);
        notAdmin.setIsAdmin(false);

        Profile owner = new Profile();
        owner.setId(OWNER_ID);

        //Act
        boolean result = AuthenticationUtil.validUser(notAdmin, owner);

        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void validUserTestIsOwner() {
        //Arrange
        Profile user = new Profile();
        user.setId(ADMIN_ID);
        user.setIsAdmin(false);

        Profile owner = new Profile();
        owner.setId(ADMIN_ID);

        //Act
        boolean result = AuthenticationUtil.validUser(user, owner);

        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void validUserTestIsNotOwner() {
        //Arrange
        Profile notOwner = new Profile();

        Profile owner = new Profile();
        owner.setId(ADMIN_ID);

        notOwner.setId(OWNER_ID);
        notOwner.setIsAdmin(false);

        //Act
        boolean result = AuthenticationUtil.validUser(notOwner, owner);

        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void getLoggedInUserTest() {
        //Arrange
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .session(AUTHORIZED, LOGGED_IN_ID)
                .uri(PROFILES_URI);

        //Act
        Long userId = AuthenticationUtil.getLoggedInUserId(request.build());
        Long parsedId = Long.parseLong(LOGGED_IN_ID);

        //Assert
        Assert.assertNotNull(userId);
        Assert.assertEquals(userId, parsedId);
    }


    @Test
    public void noLoggedInUserTest() {
        //Arrange
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(PROFILES_URI);

        //Act
        Long userId = AuthenticationUtil.getLoggedInUserId(request.build());

        //Assert
        Assert.assertNull(userId);
    }


    @Test
    public void noAuthenticationTest() {
        //Arrange
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri(PROFILES_URI);

        //Act
        Profile loggedInUser = AuthenticationUtil.validateAuthentication(mockProfileRepository, request.build());

        //Assert
        Assert.assertNull(loggedInUser);
    }
}