package controllers.photos;

import com.typesafe.config.Config;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import repositories.ProfileRepository;
import repositories.photos.PersonalPhotoRepository;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.UNAUTHORIZED;
import static play.test.Helpers.*;

public class PhotoControllerTest {

    private static final String AUTHORIZED = "authorized";
    private static final String USER_ID = "1";

    private PhotoController testPhotoController;
    private ProfileRepository mockProfileRepo;
    private PersonalPhotoRepository mockPersonalPhotoRepo;
    private Config mockConfig;

    @Before
    public void setUp() {
        mockProfileRepo = mock(ProfileRepository.class);
        mockPersonalPhotoRepo = mock(PersonalPhotoRepository.class);
        mockConfig = mock(Config.class);
        testPhotoController = new PhotoController(mockProfileRepo, mockPersonalPhotoRepo, mockConfig);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(mockProfileRepo);
        verifyNoMoreInteractions(mockPersonalPhotoRepo);
    }

    @Test
    public void fetchNonExistentPhoto() {
        // Mock
        when(mockPersonalPhotoRepo.fetch(any(Long.class))).thenReturn(null);

        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID);
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testPhotoController.fetch(request, 12L, false);

        // Assert
        Assert.assertEquals(NOT_FOUND, result.status());

        // Verify Mocks
        verify(mockPersonalPhotoRepo, times(1)).fetch(any(Long.class));
    }

    @Test
    public void fetchNonExistentThumb() {
        // Mock
        when(mockPersonalPhotoRepo.fetch(any(Long.class))).thenReturn(null);

        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest().session(AUTHORIZED, USER_ID);
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testPhotoController.fetch(request, 12L, true);

        // Assert
        Assert.assertEquals(NOT_FOUND, result.status());

        // Verify Mocks
        verify(mockPersonalPhotoRepo, times(1)).fetch(any(Long.class));
    }

    @Test
    public void fetchPhotoNotLoggedIn() {
        // Arrange
        Http.RequestBuilder requestBuilder = fakeRequest(); // Create request without session (User not logged in)
        Http.Request request = requestBuilder.build();

        // Act
        Result result = testPhotoController.fetch(request, 12L, false);

        // Assert
        Assert.assertEquals(UNAUTHORIZED, result.status());
    }
}