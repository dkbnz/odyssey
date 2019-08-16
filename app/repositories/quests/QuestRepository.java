package repositories.quests;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.quests.Quest;

import java.util.List;


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
    public List<Quest> findAllUsing(String country) {
        return query().where().in("countryOccurrences.key", country).findList();
    }
}
