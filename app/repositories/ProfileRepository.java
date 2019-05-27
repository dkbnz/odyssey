package repositories;


import models.Profile;
import models.photos.PersonalPhoto;

public class ProfileRepository {

    /**
     * Finds a single profile with the given id. Return null if no such profile exists.
     * @param userId        The id of the user to be retrieved.
     * @return              The profile object associated with the id. Returns null if no profile was found.
     */
    public Profile fetchSingleProfile(Integer userId) {
        return Profile.find.byId(userId);
    }

    /**
     * Updates a certain users profile photo and updates the database
     * @param photo the photo to set the profile photo to
     * @param profile the profile to change the photo for
     */

    public void setProfilePhoto(PersonalPhoto photo, Profile profile) { profile.setProfilePicture(photo); profile.update();}

    /**
    * Saves the specified profile object.
    */
    public void save(Profile profile) {profile.save();}

    /**
    * Deletes a profile photo from a specified user profile by setting the profile photo to null.
    */
    public void deleteProfilePhoto(Profile profile) {
        profile.setProfilePicture(null);
        profile.save();
    }
}
