package repositories.treasureHunts;

import io.ebean.BeanRepository;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import models.treasureHunts.TreasureHunt;

import java.util.List;

public class TreasureHuntRepository extends BeanRepository<Long, TreasureHunt> {

    public TreasureHuntRepository() {
        super(TreasureHunt.class, Ebean.getServer("default"));
    }

    public List<TreasureHunt> findByOwner(Long ownerId) {
        List<TreasureHunt> treasureHunts;

        // Creates a list of trips from a query based on profile id
        ExpressionList<TreasureHunt> expressionList = TreasureHunt.getFind().query().where();
        expressionList.eq("owner_id", ownerId);
        treasureHunts = expressionList.findList();

        return treasureHunts;
    }

}
