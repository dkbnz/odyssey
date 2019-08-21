package repositories.quests;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.profiles.Profile;
import models.quests.QuestAttempt;

import java.util.Date;
import java.util.List;


/**
 * Handles database interaction for quest attempts.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class QuestAttemptRepository extends BeanRepository<Long, QuestAttempt> {

    private static final String ATTEMPTED_PROFILE = "attemptedBy";
    private static final String START_DATE = "questAttempted.startDate";
    private static final String END_DATE = "questAttempted.endDate";
    private static final String QUEST_ATTEMPTED = "questAttempted";
    private static final String COMPLETED = "completed";
    private static final String QUEST_ATTEMPTED_ID = "questAttempted.id";

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
                .lt(START_DATE, new Date())
                .gt(END_DATE, new Date())
                .eq(ATTEMPTED_PROFILE, questAttempt.getAttemptedBy())
                .eq(QUEST_ATTEMPTED, questAttempt.getQuestAttempted())
                .exists();
    }


    /**
     * Finds all quest attempts using the profile.
     * @param requestedProfile      the profile to get quest attempts for.
     * @return                      a list of quest attempts for the given profile.
     */
    public List<QuestAttempt> findAllUsing(Profile requestedProfile) {
        return query().where()
                .eq(ATTEMPTED_PROFILE, requestedProfile)
                .lt(START_DATE, new Date())
                .gt(END_DATE, new Date())
                .findList();
    }


    /**
     * Finds all quest attempts using the profile.
     * @param requestedProfile      the profile to get quest attempts for.
     * @return                      a list of quest attempts for the given profile.
     */
    public List<QuestAttempt> findAllUsing(Profile requestedProfile, boolean allowCompleted) {
        return query().where()
                .eq(ATTEMPTED_PROFILE, requestedProfile)
                .lt(START_DATE, new Date())
                .gt(END_DATE, new Date())
                .eq(COMPLETED, allowCompleted)
                .findList();
    }


    /**
     * Finds all quest attempts using the profile and quest id.
     * @param requestedProfile      the profile to get quest attempts for.
     * @param questId               the quest id to get quest attempts for.
     * @return                      a list of quest attempts for the given profile relating to the given quest id.
     */
    public List<QuestAttempt> findAllUsing(Profile requestedProfile, Long questId) {
        return query().where()
                .eq(ATTEMPTED_PROFILE, requestedProfile)
                .eq(QUEST_ATTEMPTED_ID, questId)
                .lt(START_DATE, new Date())
                .gt(END_DATE, new Date())
                .findList();
    }
}
