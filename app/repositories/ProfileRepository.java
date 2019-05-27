package repositories;


import models.Profile;

public class ProfileRepository {

    /**
     * Finds a single profile with the given id. Return null if no such profile exists.
     * @param userId        The id of the user to be retrieved.
     * @return              The profile object associated with the id. Returns null if no profile was found.
     */
    public Profile fetchSingleProfile(Integer userId) {
        return Profile.find.byId(userId);
    }

    public void save(Profile profile) {profile.save();}
}
