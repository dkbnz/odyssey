package controllers.profiles;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.*;
import static play.test.Helpers.fakeRequest;

public class ProfileControllerTest {

    private static final String AUTHORIZED = "authorized";
    private static final String USER_ID = "1";

    private ProfileRepository mockProfileRepo;
    private NationalityRepository mockNationalityRepo;
    private PassportRepository mockPassportRepo;
    private TravellerTypeRepository mockTravellerTypeRepo;
    private AchievementTrackerRepository mockAchievementTrackerRepo;

    private Profile defaultAdminUser;
    private Profile adminUser;
    private Profile regularUser;

    private ProfileController testProfileController;

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


    @After
    public void tearDown() {
        verifyNoMoreInteractions(mockProfileRepo);
        verifyNoMoreInteractions(mockNationalityRepo);
        verifyNoMoreInteractions(mockPassportRepo);
        verifyNoMoreInteractions(mockTravellerTypeRepo);
        verifyNoMoreInteractions(mockAchievementTrackerRepo);
    }
}
