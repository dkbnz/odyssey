package repositories.quests;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.quests.QuestAttempt;


/**
 * Handles database interaction for quest attempts.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class QuestAttemptRepository extends BeanRepository<Long, QuestAttempt> {

    @Inject
    public QuestAttemptRepository() {
        super(QuestAttempt.class, Ebean.getDefaultServer());
    }

    /**
     * Return true if a quest attempt exists with identical profile and quest.
     *
     * @param questAttempt  the questAttempt to check existence of.
     * @return              true if exists.
     */
    public boolean exists(QuestAttempt questAttempt) {
        return query().where()
                .eq("attemptedBy", questAttempt.getAttemptedBy())
                .eq("questAttempted", questAttempt.getQuestAttempted())
                .exists();
    }
}
