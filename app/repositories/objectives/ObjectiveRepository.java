package repositories.objectives;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.destinations.Destination;
import models.objectives.Objective;
import models.profiles.Profile;

import java.util.List;


/**
 * Handles database interaction for objectives.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class ObjectiveRepository extends BeanRepository<Long, Objective> {

    private static final String DESTINATION = "destination";
    private static final String QUESTS_ATTEMPTED_BY_PROFILE = "questsUsing.attempts.attemptedBy";
    private static final String QUESTS_ATTEMPTED_COMPLETE = "questsUsing.attempts.completed";

    @Inject
    public ObjectiveRepository() {
        super(Objective.class, Ebean.getDefaultServer());
    }


    /**
     * Retrieve all objectives that use the destination.
     *
     * @param destination   the destination being checked for usage within objectives.
     * @return              a potentially empty list of objectives that contain contain the destination parameter.
     */
    public List<Objective> findAllUsing(Destination destination) {
        return query()
                .where()
                .eq(DESTINATION, destination)
                .findList();
    }


    /**
     * Retrieves a list of objectives completed by a user.
     *
     * @param profile   the user who is having their completed objectives retrieved.
     * @return          a list of completed objectives for the profile.
     */
    public List<Objective> findAllCompletedUsing(Profile profile) {
//        TODO: Matilda + Vinnie Include the return of objectives that are up to the checked in index.
        return  query()
                .where()
                .eq(QUESTS_ATTEMPTED_BY_PROFILE, profile)
                .eq(QUESTS_ATTEMPTED_COMPLETE, true)
                .findList();
    }
}
