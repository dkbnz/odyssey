package util;

import models.Profile;
import play.mvc.Http;

import java.util.Optional;

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
     * @param loggedInUser      The profile of the currently logged in user.
     * @param owner             The profile of the owner of the data that is being manipulated.
     * @return                  True if the logged in user is allowed to manipulate the owners data.
     */
    public static boolean validUser(Profile loggedInUser, Profile owner) {
        return loggedInUser.getIsAdmin()|| owner.getId().equals(loggedInUser.getId());
    }


    /**
     * Gets the logged in user id from a given request.
     *
     * @param request       The Http request that was received.
     * @return              An integer value of the logged in user id, null if there is no logged in user.
     */
    public static Integer getLoggedInUserId(Http.Request request) {
        Optional<String> optional = request.session().getOptional(AUTHORIZED);
        String userId = null;
        if (optional.isPresent()) {
            userId = optional.get();
        }
        try {
            return Integer.valueOf(userId);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}