package repositories.treasureHunt;

import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.treasureHunt.TreasureHunt;

public class TreasureHuntRepository extends BeanRepository<Long, TreasureHunt> {

    public TreasureHuntRepository() {
        super(TreasureHunt.class, Ebean.getServer("default"));
    }
}
