package repositories.quests;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.profiles.Profile;
import io.ebean.ExpressionList;
import models.quests.Quest;
import java.util.List;


/**
 * Handles database interaction for quests.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class QuestRepository extends BeanRepository<Long, Quest> {

    private static final String ATTEMPTED_BY = "attempts.attemptedBy";
    private static final String COMPLETED = "attempts.completed";

    @Inject
    public QuestRepository() {
        super(Quest.class, Ebean.getDefaultServer());
    }


    /**
     * Gets the expression list to build the search query for quests.
     * @return          an expression list with object type Quest.
     */
    public ExpressionList<Quest> getExpressionList() {
        return query().where();
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
                .eq(ATTEMPTED_BY, profile)
                .eq(COMPLETED, true)
                .findList();
    }
}
