package controllers.destinations;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.points.AchievementTrackerController;
import models.profiles.Profile;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import repositories.destinations.DestinationRepository;
import repositories.destinations.DestinationTypeRepository;
import repositories.hints.HintRepository;
import repositories.objectives.ObjectiveRepository;
import repositories.points.BadgeRepository;
import repositories.points.PointRewardRepository;
import repositories.profiles.ProfileRepository;
import repositories.trips.TripDestinationRepository;
import repositories.trips.TripRepository;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.*;
import static play.test.Helpers.fakeRequest;

public class DestinationControllerTest {

    private static final String AUTHORIZED = "authorized";
    private static final String USER_ID = "2";
    private static final Long DEFAULT_ADMIN_ID = 1L;
    private static final Long REGULAR_USER_ID = 2L;
    private static final Long REQUESTED_USER_ID = 3L;
    private static final Long NON_EXISTENT_USER_ID = 500L;
    private DestinationController mockDestinationController;
    private ProfileRepository mockProfileRepository;

    @Before
    public void setUp() {
        DestinationRepository mockDestinationRepository;
        TripDestinationRepository mockTripDestinationRepository;
        TripRepository mockTripRepository;
        ObjectiveRepository mockObjectiveRepository;
        DestinationTypeRepository mockDestinationTypeRepository;
        BadgeRepository mockBadgeRepository;
        PointRewardRepository mockPointRewardRepository;;
        AchievementTrackerController mockAchievementTrackerController;
        HintRepository mockHintRepository;
        ObjectMapper objectMapper;

        mockDestinationRepository = mock(DestinationRepository.class);
        mockProfileRepository = mock(ProfileRepository.class);
        mockTripDestinationRepository = mock(TripDestinationRepository.class);
        mockTripRepository = mock(TripRepository.class);
        mockObjectiveRepository = mock(ObjectiveRepository.class);
        mockDestinationTypeRepository = mock(DestinationTypeRepository.class);
        mockBadgeRepository = mock(BadgeRepository.class);
        mockPointRewardRepository = mock(PointRewardRepository.class);
        mockHintRepository = mock(HintRepository.class);
        objectMapper = mock(ObjectMapper.class);

        mockAchievementTrackerController = new AchievementTrackerController(
                mockProfileRepository,
                mockPointRewardRepository,
                mockBadgeRepository,
                mockHintRepository,
                objectMapper);

        mockDestinationController = new DestinationController(
                mockProfileRepository,
                mockDestinationRepository,
                mockDestinationTypeRepository,
                mockTripDestinationRepository,
                mockTripRepository,
                mockObjectiveRepository,
                mockAchievementTrackerController);

        Profile defaultAdmin;
        Profile regularUser;
        Profile requestedUser;

        defaultAdmin = new Profile();
        defaultAdmin.setId(DEFAULT_ADMIN_ID);
        defaultAdmin.setAdmin(true);

        regularUser = new Profile();
        regularUser.setId(REGULAR_USER_ID);
        regularUser.setAdmin(false);


        requestedUser = new Profile();
        requestedUser.setId(REQUESTED_USER_ID);
        requestedUser.setAdmin(false);


        // Mock profile fetches
        when(mockProfileRepository.findById(DEFAULT_ADMIN_ID)).thenReturn(defaultAdmin);
        when(mockProfileRepository.findById(REGULAR_USER_ID)).thenReturn(regularUser);
        when(mockProfileRepository.findById(REQUESTED_USER_ID)).thenReturn(requestedUser);
    }

    @Test
    public void getTypes() {
        // Act
        Result result = mockDestinationController.getTypes();

        // Assert
        Assert.assertEquals(OK, result.status());
    }

    @Test
    public void fetchNotLoggedIn() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest();
        Http.Request request = requestBuilder.build();

        // Act
        Result result = mockDestinationController.fetch(request);

        // Assert
        Assert.assertEquals(UNAUTHORIZED, result.status());

        // Verify mocks
        verify(mockProfileRepository, times(0)).findById(any(Long.class));
    }


    @Test
    public void fetchByOwnerNotExist() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID).uri("?owner=500");
        Http.Request request = requestBuilder.build();
        // Act
        Result result = mockDestinationController.fetch(request);

        // Assert
        Assert.assertEquals(BAD_REQUEST, result.status());

        // Verify mocks
        verify(mockProfileRepository, times(2)).findById(any(Long.class));
    }


    @Test
    public void fetchByOwnerForbidden() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID).uri("?owner=3");
        Http.Request request = requestBuilder.build();

        // Act
        Result result = mockDestinationController.fetch(request);

        // Assert
        Assert.assertEquals(FORBIDDEN, result.status());

        // Verify mocks
        verify(mockProfileRepository, times(2)).findById(any(Long.class));
    }


    @Test
    public void fetchByUserNotLoggedIn() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest();
        Http.Request request = requestBuilder.build();

        // Act
        Result result = mockDestinationController.fetchByUser(request, REQUESTED_USER_ID);

        // Assert
        Assert.assertEquals(UNAUTHORIZED, result.status());

        // Verify mocks
        verify(mockProfileRepository, times(0)).findById(any(Long.class));
    }


    @Test
    public void fetchByUserProfileNotExist() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID);
        Http.Request request = requestBuilder.build();
        // Act
        Result result = mockDestinationController.fetchByUser(request, NON_EXISTENT_USER_ID);

        // Assert
        Assert.assertEquals(BAD_REQUEST, result.status());

        // Verify mocks
        verify(mockProfileRepository, times(2)).findById(any(Long.class));
    }


    @Test
    public void fetchByUserForbidden() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID);
        Http.Request request = requestBuilder.build();

        // Act
        Result result = mockDestinationController.fetchByUser(request, REQUESTED_USER_ID);

        // Assert
        Assert.assertEquals(FORBIDDEN, result.status());

        // Verify mocks
        verify(mockProfileRepository, times(2)).findById(any(Long.class));
    }


    @After
    public void tearDown() {
        verifyNoMoreInteractions(mockProfileRepository);
    }
}
