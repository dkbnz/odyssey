package repositories.objectives;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.destinations.Destination;
import models.objectives.Objective;
import models.profiles.Profile;
import models.quests.QuestAttempt;

import java.util.List;


/**
 * Handles database interaction for objectives.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class ObjectiveRepository extends BeanRepository<Long, Objective> {

    private static final String DESTINATION = "destination";

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


//    TODO: Vinnie + Matilda

    /**
     * Retrieves a list of objectives completed by a user.
     *
     * @param profile   the user who is having their completed objectives retrieved.
     * @return          a list of completed objectives for the profile.
     */
    public List<Objective> findAllCompletedUsing(Profile profile) {
        return  query()
                .where()
                .eq("questsUsing.attempts.attemptedBy", profile)
                .eq("questsUsing.attempts.completed", true)
                .findList();
    }
}
