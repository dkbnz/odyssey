package repositories.treasureHunts;

import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.treasureHunts.TreasureHunt;

public class TreasureHuntRepository extends BeanRepository<Long, TreasureHunt> {

    public TreasureHuntRepository() {
        super(TreasureHunt.class, Ebean.getServer("default"));
    }
}
