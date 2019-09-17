package controllers.profiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.profiles.Profile;
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
    private static final String REGULAR_USER_ID = "3";
    private static final String USERNAME = "username";

    private ProfileRepository mockProfileRepo;
    private NationalityRepository mockNationalityRepo;
    private PassportRepository mockPassportRepo;
    private TravellerTypeRepository mockTravellerTypeRepo;
    private AchievementTrackerRepository mockAchievementTrackerRepo;

    private Profile defaultAdminUser;
    private Profile adminUser;
    private Profile regularUser;
    private Profile fullUser;

    private ProfileController testProfileController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockProfileRepo = mock(ProfileRepository.class);
        mockNationalityRepo = mock(NationalityRepository.class);
        mockPassportRepo = mock(PassportRepository.class);
        mockTravellerTypeRepo = mock(TravellerTypeRepository.class);
        mockAchievementTrackerRepo = mock(AchievementTrackerRepository.class);

        testProfileController = new ProfileController(mockProfileRepo,
                mockNationalityRepo,
                mockPassportRepo,
                mockTravellerTypeRepo,
                mockAchievementTrackerRepo);

        defaultAdminUser = new Profile();
        defaultAdminUser.setId(1L);
        defaultAdminUser.setAdmin(false);

        adminUser = new Profile();
        adminUser.setId(2L);
        adminUser.setAdmin(true);

        regularUser = new Profile();
        regularUser.setId(3L);
        regularUser.setAdmin(false);

        fullUser = new Profile();
        fullUser.setId(4L);
        fullUser.setUsername("test1@email.com");
        fullUser.setPassword("guest123");
        fullUser.setFirstName("Jack");
        fullUser.setLastName("Taylor");
        fullUser.setGender("Male");
        fullUser.setDateOfBirth(LocalDate.now().minusYears(5));

        // Mock profile fetches
        when(mockProfileRepo.findById(1L)).thenReturn(defaultAdminUser);
        when(mockProfileRepo.findById(2L)).thenReturn(adminUser);
        when(mockProfileRepo.findById(3L)).thenReturn(regularUser);
        when(mockProfileRepo.findById(4L)).thenReturn(fullUser);
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
        Result result = testProfileController.delete(request, 1L);

        // Assert
        Assert.assertEquals(BAD_REQUEST, result.status());
    }


    @Test
    public void deleteProfileNotLoggedIn() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest();
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.delete(request, 3L);

        // Assert
        Assert.assertEquals(UNAUTHORIZED, result.status());
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
        Result result = testProfileController.makeAdmin(request, 3L);

        // Assert
        Assert.assertEquals(UNAUTHORIZED, result.status());
    }


    @Test
    public void makeAdminForbidden() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID);
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.makeAdmin(request, 3L);

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
        Result result = testProfileController.makeAdmin(request, 4L);

        // Assert
        Assert.assertEquals(OK, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(2)).findById(any(Long.class));
    }


    @Test
    public void removeAdminNotLoggedIn() {

        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest();
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.removeAdmin(request, 3L);

        // Assert
        Assert.assertEquals(UNAUTHORIZED, result.status());
    }


    @Test
    public void removeAdminForbidden() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID);
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.removeAdmin(request, 3L);

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
        Result result = testProfileController.removeAdmin(request, 1L);

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
        Result result = testProfileController.removeAdmin(request, 4L);

        // Assert
        Assert.assertEquals(OK, result.status());

        // Verify Mocks
        verify(mockProfileRepo, times(2)).findById(any(Long.class));
    }


    @Test
    public void checkUsernameNoUsername() throws Exception {
        ObjectNode jsonBody = objectMapper.createObjectNode();

        jsonBody.put(AUTHORIZED, "");

        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().bodyJson(jsonBody);
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testProfileController.checkUsername(request);

        // Assert
        Assert.assertEquals(BAD_REQUEST, result.status());
    }
}
