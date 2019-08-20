package repositories.quests;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.Profile;
import models.quests.Quest;

import java.util.List;


/**
 * Handles database interaction for quests.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class QuestRepository extends BeanRepository<Long, Quest> {

    @Inject
    public QuestRepository() {
        super(Quest.class, Ebean.getDefaultServer());
    }

    /**
     * Retrieve a List of Quests that a given user has completed.
     *
     * @param profile     the profiles to find the quests.
     * @return              a List of Quests that have been completed by the given user.
     */
    public List<Quest> findAllCompleted(Profile profile) {
        return  query()
                .where()
                .eq("attempts.attemptedBy", profile)
                .eq("attempts.completed", true)
                .findList();
    }
}
