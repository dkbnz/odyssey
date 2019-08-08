package repositories.treasureHunts;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.treasureHunts.TreasureHunt;


public class TreasureHuntRepository extends BeanRepository<Long, TreasureHunt> {

    @Inject
    public TreasureHuntRepository() {
        super(TreasureHunt.class, Ebean.getDefaultServer());
    }
}
