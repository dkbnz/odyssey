package repositories;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.Profile;
import models.photos.PersonalPhoto;
import org.springframework.context.annotation.Bean;

public class ProfileRepository extends BeanRepository<Long, Profile> {

    @Inject
    public ProfileRepository() {
        super(Profile.class, Ebean.getDefaultServer());
    }

    /**
     * Finds a single profile with the given id. Return null if no such profile exists.
     * @param userId        the id of the user to be retrieved.
     * @return              the profile object associated with the id. Returns null if no profile was found.
     */
    public Profile fetchSingleProfile(Integer userId) {
        return super.findById(userId.longValue());
    }


    /**
     * Updates a certain users profile photo and updates the database.
     * @param photo     the photo to set the profile photo to.
     * @param profile   the profile to change the photo for.
     */

    public void setProfilePhoto(PersonalPhoto photo, Profile profile) {
        profile.setProfilePicture(photo);
        super.update(profile);
    }


    /**
    * Saves the specified profile object.
    */
    public void save(Profile profile) {super.save(profile);}

    /**
     * Updates the specified profile object.
     */
    public void update(Profile profile) {profile.update();}


    /**
    * Deletes a profile photo from a specified user profile by setting the profile photo to null.
    */
    public void deleteProfilePhoto(Profile profile) {
        profile.setProfilePicture(null);
        profile.save();
    }
}