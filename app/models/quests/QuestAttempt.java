package models.quests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.BaseModel;
import models.Profile;
import models.destinations.Destination;
import models.treasureHunts.TreasureHunt;

import java.util.List;

public class QuestAttempt extends BaseModel {

    private Profile attemptedBy;

    @JsonIgnoreProperties({"treasureHunts"})
    private Quest questAttempted;

    private boolean solvedCurrent;
    private int checkedInIndex;

    private boolean completed;

    public QuestAttempt() {
        // Empty constructor needed by Ebeans
    }

    public QuestAttempt(Profile attemptedBy, Quest questAttempted) {
        this.questAttempted = questAttempted;
        this.attemptedBy = attemptedBy;
        this.solvedCurrent = false;
        this.checkedInIndex = 0;
        this.completed = false;
    }

    /**
     * Returns a list of TreasureHunts that the user has correctly guessed the destination for.
     * @return
     */
    @JsonProperty("solved")
    public List<TreasureHunt> getSolved() {
        return questAttempted
                .getTreasureHunts()
                .subList(0, checkedInIndex + (solvedCurrent ? 1 : 0));
    }

    /**
     * Returns the current Treasure Hunt the user needs to solve.
     * Will return null if there is no current treasure hunt to solve.
     * This means that the previously solved treasure hunt needs to be checked in, or the quest is complete.
     * @return
     */
    @JsonProperty("current")
    @JsonIgnoreProperties({"destination"})
    public TreasureHunt getCurrent() {
        if (!solvedCurrent && !completed)
            return questAttempted
                    .getTreasureHunts()
                    .get(checkedInIndex);
        else
            return null;
    }

    /**
     * Returns a list of TreasureHunts that the user is yet to solve.
     * @return
     */
    @JsonProperty("unsolved")
    @JsonIgnoreProperties({"destination"})
    public List<TreasureHunt> getUnsolved() {
        return questAttempted
                .getTreasureHunts()
                .subList(checkedInIndex + (solvedCurrent ? 1 : 0),
                        questAttempted.getTreasureHunts().size()
                );
    }

    /**
     * Provide a destination to attempt to solve the current TreasureHunt.
     * @param destination
     * @return
     */
    public boolean solveCurrent(Destination destination) {
        if(!solvedCurrent && !completed) {
            solvedCurrent = questAttempted
                    .getTreasureHunts()
                    .get(checkedInIndex)
                    .getDestination()
                    .equals(destination);
            // TODO: Doug, do we want to compare just ID? This is technically comparing similar destinations
            return solvedCurrent;
        } else {
            return false;
        }
    }

    /**
     * Check in to the most recently solved TreasureHunt.
     * Will make the next TreasureHunt available to solve.
     * If the user checks in to the last TreasureHunt, Quest will be complete.
     */
    public void checkIn() {
        if(solvedCurrent && !completed && (questAttempted.getTreasureHunts().size() > checkedInIndex)) {
            checkedInIndex += 1;
            solvedCurrent = false;
        }

        // If we have checked in to the last treasure hunt, quest is complete.
        completed = checkedInIndex == questAttempted.getTreasureHunts().size();
    }

    public Profile getAttemptedBy() {
        return attemptedBy;
    }

    public void setAttemptedBy(Profile attemptedBy) {
        this.attemptedBy = attemptedBy;
    }

    public Quest getQuestAttempted() {
        return questAttempted;
    }

    public void setQuestAttempted(Quest questAttempted) {
        this.questAttempted = questAttempted;
    }

    public boolean isCompleted() {
        return completed;
    }
}
