package util;

import models.Profile;
import play.mvc.Http;
import repositories.ProfileRepository;

public final class AuthenticationUtil {

    private static final String AUTHORIZED = "authorized";

    /**
     * Private constructor for the class to prevent instantiation.
     */
    private AuthenticationUtil() {
        throw new IllegalStateException("Utility class");
    }


    /**
     * Returns true if the logged in user is either an admin, or is attempting to perform an action on their own
     * data.
     * @param loggedInUserId    The profile id of the currently logged in user.
     * @param ownerId           The profile id of the owner of the data that is being manipulated.
     * @return                  True if the logged in user is allowed to manipulate the owners data.
     */
    public static boolean validUser(Integer loggedInUserId, Integer ownerId) {
        return userIsAdmin(loggedInUserId) || ownerId.equals(loggedInUserId);
    }


    /**
     * Returns true if a user is an admin, false otherwise.
     * @param userId        The profile id of the user.
     * @return              True if the user is an admin.
     */
    private static boolean userIsAdmin(Integer userId) {
        Profile user = ProfileRepository.fetchSingleProfile(userId);
        return user.getIsAdmin();
    }


    /**
     * Gets the logged in user id from a given request.
     *
     * @param request       The Http request that was received.
     * @return              An integer value of the logged in user id, null if there is no logged in user.
     */
    public static Integer getLoggedInUserId(Http.Request request) {
        String userId = request.session().getOptional(AUTHORIZED).orElseGet(null);
        try {
            return Integer.valueOf(userId);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
