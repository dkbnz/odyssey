package repositories.quests;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.quests.Quest;


public class QuestRepository extends BeanRepository<Long, Quest> {

    @Inject
    public QuestRepository() {
        super(Quest.class, Ebean.getDefaultServer());
    }

}
