package models.quests;

import models.Profile;
import models.treasureHunts.TreasureHunt;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Quest {

    /**
     * List of treasure hunts to be solved in this quest.
     */
    @ManyToMany
    @OrderColumn
    private List<TreasureHunt> treasureHunts;

    private String title;

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
        return treasureHunts.remove(treasureHunt);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
