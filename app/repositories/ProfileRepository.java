package repositories;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import models.Profile;
import models.photos.PersonalPhoto;
import org.springframework.context.annotation.Bean;

public class ProfileRepository extends BeanRepository<Long, Profile> {

    @Inject
    public ProfileRepository() {
        super(Profile.class, Ebean.getDefaultServer());
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
    public void update(Profile profile) {super.update(profile);}


    /**
    * Deletes a profile photo from a specified user profile by setting the profile photo to null.
    */
    public void deleteProfilePhoto(Profile profile) {
        profile.setProfilePicture(null);
        super.save(profile);
    }

    public ExpressionList<Profile> getExpressionList() {
        return query().where();
    }
}