package util;

import models.Profile;

public final class AuthenticationUtil {

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
     * @param ownerId           The id of the
     * @return
     */
    public static boolean validUser(Profile loggedInUser, Long ownerId) {
        return loggedInUser.getIsAdmin() || ownerId.equals(loggedInUser.getId());
    }
}
