package repositories.quests;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.Profile;
import io.ebean.ExpressionList;
import models.quests.Quest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
     * Retrieve a List of Quests that contain an Objective with the given country string as the Destination.
     *
     * @param country   the Country to find.
     * @return          a List of Quests that have the given country as an occurrence.
     */
    public HashSet<Quest> findAllUsing(String country) {
        return (HashSet<Quest>) query().where().in("countryOccurrences.key", country).findSet();
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


    /**
     * Gets the expression list to build the search query for quests.
     * @return          an expression list with object type Quest.
     */
    public ExpressionList<Quest> getExpressionList() {
        return query().where();
    }
}
