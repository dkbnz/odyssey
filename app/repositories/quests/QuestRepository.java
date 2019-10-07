package repositories.quests;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.objectives.Objective;
import models.profiles.Profile;
import io.ebean.ExpressionList;
import models.quests.Quest;
import models.quests.QuestAttempt;
import java.util.Date;
import java.util.List;


/**
 * Handles database interaction for quests.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class QuestRepository extends BeanRepository<Long, Quest> {

    private static final String ATTEMPTED_BY = "attempts.attemptedBy";
    private static final String COMPLETED = "attempts.completed";
    private static final String OBJECTIVES = "objectives";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String OWNER = "owner";

    private QuestAttemptRepository questAttemptRepository = new QuestAttemptRepository();

    @Inject
    public QuestRepository() {
        super(Quest.class, Ebean.getDefaultServer());
    }


    /**
     * Gets the expression list to build the search query for quests.
     *
     * @return          an expression list with object type Quest.
     */
    public ExpressionList<Quest> getExpressionList() {
        return query().where();
    }


    /**
     * Retrieve a list of quests that a given user has completed.
     *
     * @param profile       the profiles to find the quests.
     * @return              a List of Quests that have been completed by the given user.
     */
    public List<Quest> findAllCompleted(Profile profile) {
        return  query()
                .where()
                .eq(ATTEMPTED_BY, profile)
                .eq(COMPLETED, true)
                .findList();
    }


    /**
     * Retrieve a list of quests that contain the given objective.
     *
     * @param objective     the objective to find the quests.
     * @return              a list of quests that use the given objective.
     */
    public List<Quest> findAllUsing(Objective objective) {
        return  query()
                .where()
                .eq(OBJECTIVES, objective)
                .findList();
    }


    /**
     * Find the total number of quests that are available to the user. These are quests they don't own or haven't
     * started.
     *
     * @param profile       the profiles to find the quests.
     * @return              the count of all available quests
     */
    public Integer findCountAvailable(Profile profile) {
        List<Quest> quests = query()
                .where()
                .notIn(OWNER, profile)
                .lt(START_DATE, new Date())
                .gt(END_DATE, new Date())
                .findList();

        List<QuestAttempt> questAttempts = questAttemptRepository.findAllUsing(profile);

        for (QuestAttempt questAttempt : questAttempts) {
            quests.remove(questAttempt.getQuestAttempted());
        }

        return quests.size();
    }
}
