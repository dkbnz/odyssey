package models.quests;

import models.BaseModel;
import models.Profile;
import models.treasureHunts.TreasureHunt;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Quest extends BaseModel {

    /**
     * List of treasure hunts to be solved in this quest.
     */
    @ManyToMany
    @OrderColumn
    private List<TreasureHunt> treasureHunts;

    /**
     * List of attempts that have been had on this quest.
     */
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<QuestAttempt> attempts;

    /**
     * Title of this quest.
     */
    private String title;

    /**
     * Date which this quest starts.
     */
    private LocalDate startDate;

    /**
     * Date which this quest ends.
     */
    private LocalDate endDate;

    /**
     * Owner of this quest.
     */
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
