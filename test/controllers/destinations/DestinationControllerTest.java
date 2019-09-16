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
import play.test.Helpers;
import repositories.destinations.DestinationRepository;
import repositories.destinations.DestinationTypeRepository;
import repositories.objectives.ObjectiveRepository;
import repositories.points.AchievementTrackerRepository;
import repositories.points.BadgeRepository;
import repositories.points.PointRewardRepository;
import repositories.profiles.ProfileRepository;
import repositories.trips.TripDestinationRepository;
import repositories.trips.TripRepository;

import java.awt.*;

import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.UNAUTHORIZED;
import static play.test.Helpers.fakeRequest;

public class DestinationControllerTest {

    private static final String AUTHORIZED = "authorized";
    private static final String ADMIN_ID = "1";
    private static final String USER_ID = "2";


    private DestinationRepository mockDestinationRepository;
    private ProfileRepository mockProfileRepository;
    private TripDestinationRepository mockTripDestinationRepository;
    private TripRepository mockTripRepository;
    private ObjectiveRepository mockObjectiveRepository;
    private DestinationTypeRepository mockDestinationTypeRepository;
    private BadgeRepository mockBadgeRepository;
    private PointRewardRepository mockPointRewardRepository;
    private AchievementTrackerRepository mockAchievementTrackerRepository;
    private DestinationController mockDestinationController;
    private AchievementTrackerController mockAchievementTrackerController;
    private ObjectMapper objectMapper;



    private Profile defaultAdmin;
    private Profile regularUser;

    @Before
    public void setUp() {
        mockDestinationRepository = mock(DestinationRepository.class);
        mockProfileRepository = mock(ProfileRepository.class);
        mockTripDestinationRepository = mock(TripDestinationRepository.class);
        mockTripRepository = mock(TripRepository.class);
        mockObjectiveRepository = mock(ObjectiveRepository.class);
        mockDestinationTypeRepository = mock(DestinationTypeRepository.class);
        mockBadgeRepository = mock(BadgeRepository.class);
        mockPointRewardRepository = mock(PointRewardRepository.class);
        mockAchievementTrackerRepository = mock(AchievementTrackerRepository.class);
        objectMapper = mock(ObjectMapper.class);
        mockAchievementTrackerController = new AchievementTrackerController(
                mockProfileRepository,
                mockPointRewardRepository,
                mockAchievementTrackerRepository,
                mockBadgeRepository,
                objectMapper);
        mockDestinationController = new DestinationController(
                mockProfileRepository,
                mockDestinationRepository,
                mockDestinationTypeRepository,
                mockTripDestinationRepository,
                mockTripRepository,
                mockObjectiveRepository,
                mockAchievementTrackerController);


        defaultAdmin = new Profile();
        defaultAdmin.setId(1L);
        defaultAdmin.setAdmin(true);

        regularUser = new Profile();
        regularUser.setId(2L);
        regularUser.setAdmin(false);

    }

    @After
    public void tearDown() {
//        verifyNoMoreInteractions(mockDestinationRepository);
//        verifyNoMoreInteractions(mockProfileRepository);
//        verifyNoMoreInteractions(mockTripDestinationRepository);
//        verifyNoMoreInteractions(mockTripRepository);
//        verifyNoMoreInteractions(mockObjectiveRepository);
//        verifyNoMoreInteractions(mockDestinationTypeRepository);
//        verifyNoMoreInteractions(mockBadgeRepository);
//        verifyNoMoreInteractions(mockPointRewardRepository);
//        verifyNoMoreInteractions(mockAchievementTrackerRepository);
//        verifyNoMoreInteractions(objectMapper);
    }


    @Test
    public void getTypes() {
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
    }

    @Test
    public void fetchByOwner() {
        // Mock
        when(mockProfileRepository.findById(any(Long.class))).thenReturn(regularUser);

        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID).uri("?owner=3");;
        Http.Request request = requestBuilder.build();

        // Act
        Result result = mockDestinationController.fetch(request);

        System.out.println(Helpers.contentAsString(result));

        // Assert
        Assert.assertEquals(OK, result.status());
    }

    @Test
    public void fetchByUser() {
    }

    @Test
    public void edit() {
    }
}
