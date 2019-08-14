package models.quests;

import models.Profile;
import models.treasureHunts.TreasureHunt;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Quest {

    /**
     * Map holding a list of treasure hunts mapped to their order.
     */
//    @ManyToMany
//    private Map<Integer, TreasureHunt> treasureHunts;

    private  List<TreasureHunt> treasureHunts = new ArrayList<>();
    private LocalDate startDate;

    private LocalDate endDate;

    private List<QuestAttempt> attempts;

    @ManyToOne
    private Profile owner;


    public List<TreasureHunt> getTreasureHunts() {
        return treasureHunts;
    }

    public void setTreasureHunts(List<TreasureHunt> treasureHunts) {
        this.treasureHunts = treasureHunts;
    }

    public boolean addTreasureHunt(TreasureHunt treasureHunt) {
        return treasureHunts.add(treasureHunt);
    }

    public boolean removeTreasureHunt(TreasureHunt treasureHunt) {
//        List<TreasureHunt> treasureHunts = getTreasureHunts();
//        boolean successful = treasureHunts.remove(treasureHunt);
//        this.setTreasureHunts(treasureHunts);
//        return successful;
        return treasureHunts.remove(treasureHunt);
    }
}
