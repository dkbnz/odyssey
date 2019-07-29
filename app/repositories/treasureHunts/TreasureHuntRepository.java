package repositories.treasureHunts;

import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.treasureHunts.TreasureHunt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TreasureHuntRepository extends BeanRepository<Long, TreasureHunt> {

    public TreasureHuntRepository() {
        super(TreasureHunt.class, Ebean.getServer("default"));
    }


    /**
     * Uses a conditional query to only get the treasure hunts that started before today and end after today.
     *
     * @return treasureHunts    the list of treasure hunts that meet the conditions.
     */
    public List<TreasureHunt> findAllTreasureHunts() {
        Calendar todaysDate = Calendar.getInstance();
        List<TreasureHunt> treasureHunts = new ArrayList<>();

        List<TreasureHunt> treasureHuntsQuery = Ebean.find(TreasureHunt.class)
                .findList();


        for (TreasureHunt treasureHunt: treasureHuntsQuery) {
            if (treasureHunt.getStartDate().after(todaysDate.getTime())
                    && treasureHunt.getEndDate().before(todaysDate.getTime())) {
                treasureHunts.add(treasureHunt);
            }
        }
        return treasureHunts;
    }
}
