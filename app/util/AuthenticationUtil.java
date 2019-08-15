package util;

import models.Profile;
import play.mvc.Http;
import repositories.ProfileRepository;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
     * @param loggedInUser      the profile of the currently logged in user.
     * @param owner             the profile of the owner of the data that is being manipulated.
     * @return                  true if the logged in user is allowed to manipulate the owners data.
     */
    public static boolean validUser(Profile loggedInUser, Profile owner) {
        return loggedInUser.isAdmin()|| owner.getId().equals(loggedInUser.getId());
    }


    /**
     * Gets the logged in user id from a given request.
     *
     * @param request       the Http request that was received.
     * @return              a long value of the logged in user id, null if there is no logged in user.
     */
    public static Long getLoggedInUserId(Http.Request request) {
        Optional<String> optional = request.session().getOptional(AUTHORIZED);
        String userId = null;
        if (optional.isPresent()) {
            userId = optional.get();
        }
        if (userId == null) {
            return null;
        }
        return Long.valueOf(userId);

    }


    /**
     * Hashes a password string using the SHA 256 method from the MessageDigest library.
     *
     * @param password                  the string you want to hash.
     * @return                          a string of the hashed binary array as a hexadecimal string.
     * @throws NoSuchAlgorithmException if the algorithm specified does not exist for the MessageDigest library.
     */
    public static String hashProfilePassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return DatatypeConverter.printHexBinary(digest.digest(password.getBytes(StandardCharsets.UTF_8)));
    }


    /**
     * Validates the authentication of the request sent. Checks the logged in user id from the request is a valid user.
     *
     * @param profileRepository     the profile repository.
     * @param request               the request sent.
     * @return                      the profile of the logged in user, null if there is no user authenticated.
     */
    public static Profile validateAuthentication(ProfileRepository profileRepository, Http.Request request) {
        Long loggedInUserId = AuthenticationUtil.getLoggedInUserId(request);
        if (loggedInUserId == null) {
            return null;
        }
        return profileRepository.findById(loggedInUserId);
    }
}