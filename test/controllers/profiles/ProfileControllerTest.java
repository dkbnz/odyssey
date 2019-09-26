package controllers.profiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.profiles.Profile;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import repositories.destinations.TravellerTypeRepository;
import repositories.points.AchievementTrackerRepository;
import repositories.profiles.NationalityRepository;
import repositories.profiles.PassportRepository;
import repositories.profiles.ProfileRepository;

import java.time.LocalDate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.*;
import static play.test.Helpers.fakeRequest;

public class ProfileControllerTest {

    private static final String AUTHORIZED = "authorized";
    private static final String USER_ID = "1";
    private static final String ADMIN_USER = "2";

    private static final Long DEFAULT_ADMIN_ID = 1L;
    private static final Long ADMIN_USER_ID = 2L;
    private static final Long REGULAR_USER_ID = 3L;

    private static final Long FULL_USER_ID = 4L;
    private static final String FULL_USER_USERNAME = "test1@email.com";
    private static final String FULL_USER_AUTHENTICATION = "guest123";
    private static final String FULL_USER_FIRST_NAME = "Jack";
    private static final String FULL_USER_LAST_NAME = "Taylor";
    private static final String FULL_USER_GENDER = "Male";

    private ProfileRepository mockProfileRepo;
    private Profile regularUser;
    private ProfileController testProfileController;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockProfileRepo = mock(ProfileRepository.class);
        NationalityRepository mockNationalityRepo = mock(NationalityRepository.class);
        PassportRepository mockPassportRepo = mock(PassportRepository.class);
        TravellerTypeRepository mockTravellerTypeRepo = mock(TravellerTypeRepository.class);
        AchievementTrackerRepository mockAchievementTrackerRepo = mock(AchievementTrackerRepository.class);

        testProfileController = new ProfileController(mockProfileRepo,
                mockNationalityRepo,
                mockPassportRepo,
                mockTravellerTypeRepo,
                mockAchievementTrackerRepo);

        Profile defaultAdminUser = new Profile();
        defaultAdminUser.setId(DEFAULT_ADMIN_ID);
        defaultAdminUser.setAdmin(false);

        Profile adminUser = new Profile();
        adminUser.setId(ADMIN_USER_ID);
        adminUser.setAdmin(true);

        regularUser = new Profile();
        regularUser.setId(REGULAR_USER_ID);
        regularUser.setAdmin(false);

        Profile fullUser = new Profile();
        fullUser.setId(FULL_USER_ID);
        fullUser.setUsername(FULL_USER_USERNAME);
        fullUser.setPassword(FULL_USER_AUTHENTICATION);
        fullUser.setFirstName(FULL_USER_FIRST_NAME);
        fullUser.setLastName(FULL_USER_LAST_NAME);
        fullUser.setGender(FULL_USER_GENDER);
        fullUser.setDateOfBirth(LocalDate.now().minusYears(5));

        // Mock profile fetches
        when(mockProfileRepo.findById(DEFAULT_ADMIN_ID)).thenReturn(defaultAdminUser);
        when(mockProfileRepo.findById(ADMIN_USER_ID)).thenReturn(adminUser);
        when(mockProfileRepo.findById(REGULAR_USER_ID)).thenReturn(regularUser);
        when(mockProfileRepo.findById(FULL_USER_ID)).thenReturn(fullUser);
    }

    @Test
    public void forbiddenCreateWhenLoggedIn() {
        // Mock
        when(mockProfileRepo.findById(any(Long.class))).thenReturn(regularUser);

        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID);
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.create(request);

        // Assert
        Assert.assertEquals(FORBIDDEN, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(1)).findById(any(Long.class));
    }


    @Test
    public void incorrectJsonCreate() {
        // Mock
        when(mockProfileRepo.findById(any(Long.class))).thenReturn(null);

        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID);
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.create(request);

        // Assert
        Assert.assertEquals(BAD_REQUEST, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(1)).findById(any(Long.class));
    }


    @Test
    public void deleteDefaultAdmin() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID);
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.delete(request, DEFAULT_ADMIN_ID);

        // Assert
        Assert.assertEquals(BAD_REQUEST, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(0)).findById(any(Long.class));
    }


    @Test
    public void deleteProfileNotLoggedIn() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest();
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.delete(request, REGULAR_USER_ID);

        // Assert
        Assert.assertEquals(UNAUTHORIZED, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(0)).findById(any(Long.class));
    }


    @Test
    public void totalNumberOfProfilesNotLoggedIn() {
        // Mock
        when(mockProfileRepo.findById(any(Long.class))).thenReturn(null);

        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID);
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.getTotalNumberOfProfiles(request);

        // Assert
        Assert.assertEquals(UNAUTHORIZED, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(1)).findById(any(Long.class));
    }


    @Test
    public void fetchProfilesNotLoggedIn() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest();
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.list(request);

        // Assert
        Assert.assertEquals(UNAUTHORIZED, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(0)).findById(any(Long.class));
    }


    @Test
    public void totalNumberOfProfilesInvalidQuery() {
        // Mock
        when(mockProfileRepo.findById(any(Long.class))).thenReturn(regularUser);

        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID).uri("?min_age=10&max_age=200");
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.getTotalNumberOfProfiles(request);

        // Assert
        Assert.assertEquals(BAD_REQUEST, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(1)).findById(any(Long.class));
        verify(mockProfileRepo, times(1)).getExpressionList();
    }


    @Test
    public void validateQueryStringAgeException() {
        // Mock
        when(mockProfileRepo.findById(any(Long.class))).thenReturn(regularUser);

        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID).uri("?min_age=s&max_age=s");
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.list(request);

        // Assert
        Assert.assertEquals(BAD_REQUEST, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(1)).findById(any(Long.class));
        verify(mockProfileRepo, times(1)).getExpressionList();
    }


    @Test
    public void validateQueryStringMinAgeSmaller() {
        // Mock
        when(mockProfileRepo.findById(any(Long.class))).thenReturn(regularUser);

        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID).uri("?min_age=-1&max_age=5");
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.list(request);

        // Assert
        Assert.assertEquals(BAD_REQUEST, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(1)).findById(any(Long.class));
        verify(mockProfileRepo, times(1)).getExpressionList();
    }


    @Test
    public void validateQueryStringMinAgeGreater() {
        // Mock
        when(mockProfileRepo.findById(any(Long.class))).thenReturn(regularUser);

        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID).uri("?min_age=125&max_age=5");
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.list(request);

        // Assert
        Assert.assertEquals(BAD_REQUEST, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(1)).findById(any(Long.class));
        verify(mockProfileRepo, times(1)).getExpressionList();
    }


    @Test
    public void validateQueryStringMaxAgeSmaller() {
        // Mock
        when(mockProfileRepo.findById(any(Long.class))).thenReturn(regularUser);

        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID).uri("?min_age=1&max_age=-1");
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.list(request);

        // Assert
        Assert.assertEquals(BAD_REQUEST, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(1)).findById(any(Long.class));
        verify(mockProfileRepo, times(1)).getExpressionList();
    }


    @Test
    public void validateQueryStringMaxAgeGreater() {
        // Mock
        when(mockProfileRepo.findById(any(Long.class))).thenReturn(regularUser);

        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID).uri("?min_age=1&max_age=200");
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.list(request);

        // Assert
        Assert.assertEquals(BAD_REQUEST, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(1)).findById(any(Long.class));
        verify(mockProfileRepo, times(1)).getExpressionList();
    }


    @Test
    public void validateQueryStringMinGreaterThanMax() {
        // Mock
        when(mockProfileRepo.findById(any(Long.class))).thenReturn(regularUser);

        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID).uri("?min_age=50&max_age=10");
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.list(request);

        // Assert
        Assert.assertEquals(BAD_REQUEST, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(1)).findById(any(Long.class));
        verify(mockProfileRepo, times(1)).getExpressionList();
    }


    @Test
    public void makeAdminNotLoggedIn() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest();
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.makeAdmin(request, REGULAR_USER_ID);

        // Assert
        Assert.assertEquals(UNAUTHORIZED, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(0)).findById(any(Long.class));
    }


    @Test
    public void makeAdminForbidden() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID);
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.makeAdmin(request, REGULAR_USER_ID);

        // Assert
        Assert.assertEquals(FORBIDDEN, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(1)).findById(any(Long.class));
    }


    @Test
    public void makeAdminNotFound() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, ADMIN_USER);
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.makeAdmin(request, 500L);

        // Assert
        Assert.assertEquals(NOT_FOUND, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(2)).findById(any(Long.class));
    }


    @Test
    public void makeAdminSuccess() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, ADMIN_USER);
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.makeAdmin(request, FULL_USER_ID);

        // Assert
        Assert.assertEquals(OK, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(2)).findById(any(Long.class));
        verify(mockProfileRepo, times(1)).update(any(Profile.class));
    }


    @Test
    public void removeAdminNotLoggedIn() {

        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest();
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.removeAdmin(request, REGULAR_USER_ID);

        // Assert
        Assert.assertEquals(UNAUTHORIZED, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(0)).findById(any(Long.class));
    }


    @Test
    public void removeAdminForbidden() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID);
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.removeAdmin(request, REGULAR_USER_ID);

        // Assert
        Assert.assertEquals(FORBIDDEN, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(1)).findById(any(Long.class));
    }


    @Test
    public void removeDefaultAdminForbidden() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, ADMIN_USER);
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.removeAdmin(request, DEFAULT_ADMIN_ID);

        // Assert
        Assert.assertEquals(FORBIDDEN, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(1)).findById(any(Long.class));
    }


    @Test
    public void removeAdminNotFound() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, ADMIN_USER);
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.removeAdmin(request, 500L);

        // Assert
        Assert.assertEquals(NOT_FOUND, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(2)).findById(any(Long.class));
    }


    @Test
    public void removeAdminSuccess() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, ADMIN_USER);
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.removeAdmin(request, FULL_USER_ID);

        // Assert
        Assert.assertEquals(OK, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(2)).findById(any(Long.class));
        verify(mockProfileRepo, times(1)).update(any(Profile.class));
    }


    @Test
    public void checkUsernameNoUsername() {
        ObjectNode jsonBody = objectMapper.createObjectNode();

        jsonBody.put(AUTHORIZED, "");

        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().bodyJson(jsonBody);
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.checkUsername(request);

        // Assert
        Assert.assertEquals(BAD_REQUEST, result.status());
        verify(mockProfileRepo, times(0)).findById(any(Long.class));
    }


    @After
    public void tearDown() {
        verifyNoMoreInteractions(mockProfileRepo);
    }
}
