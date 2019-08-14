package models.quests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.BaseModel;
import models.Profile;
import models.destinations.Destination;
import models.treasureHunts.TreasureHunt;
import javax.persistence.ManyToOne;
import java.util.List;

public class QuestAttempt extends BaseModel {

    /**
     * Profile that has attempted the Quest.
     */
    @ManyToOne
    @JsonIgnore
    private Profile attemptedBy;

    /**
     * The Quest to be attempted.
     */
    @ManyToOne
    @JsonIgnore
    private Quest questAttempted;

    /**
     * Boolean to indicate if the current treasurehunt of interest has been solved.
     */
    private boolean solvedCurrent;

    /**
     * Index of the most recently checked in treasurehunt.
     */
    private int checkedInIndex;

    /**
     * Boolean to indicate if the quest attempt has been completed.
     */
    private boolean completed;

    /**
     * QuestAttempt constructor.
     *
     * @param attemptedBy       the profile that is attempting the quest.
     * @param questAttempted    the quest that the profile is attempting.
     */
    public QuestAttempt(Profile attemptedBy, Quest questAttempted) {
        this.questAttempted = questAttempted;
        this.attemptedBy = attemptedBy;
        this.solvedCurrent = false;
        this.checkedInIndex = 0;
        this.completed = false;
    }

    /**
     * Get a list of TreasureHunts that the user has correctly guessed the destination for.
     *
     * @return  list of treasure hunts the user has solved for this particular quest attempt.
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
     * This means that the previously solved treasure hunt has not been checked in to, or the quest is complete.
     * When serialized using Json.toJson destination will not show in the treasure hunt.
     *
     * @return  current TreasureHunt to solve.
     */
    @JsonProperty("current")
    @JsonIgnoreProperties({"destination", "radius"})
    public TreasureHunt getCurrent() {
        if (!solvedCurrent && !completed) {
            return questAttempted
                    .getTreasureHunts()
                    .get(checkedInIndex);
        } else {
            return null;
        }
    }

    /**
     * Returns a list of TreasureHunts that the user is yet to solve.
     * When serialized using Json.toJson destinations will not show in the list of treasure hunts.
     *
     * @return  a list of unsolved treasure hunts
     */
    @JsonProperty("unsolved")
    @JsonIgnoreProperties({"destination", "radius"})
    public List<TreasureHunt> getUnsolved() {
        return questAttempted
                .getTreasureHunts()
                .subList(checkedInIndex + (solvedCurrent ? 1 : 0),
                        questAttempted.getTreasureHunts().size()
                );
    }

    /**
     * Provide a destination to attempt to solve the current TreasureHunt.
     *
     * @param destination   the destination that the user has entered as a guess.
     * @return              true if the guess was correct.
     */
    public boolean solveCurrent(Destination destination) {
        if(!solvedCurrent && !completed) {
            solvedCurrent = questAttempted
                    .getTreasureHunts()
                    .get(checkedInIndex)
                    .getDestination()
                    .equals(destination);
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
        if(solvedCurrent && !completed && (checkedInIndex < questAttempted.getTreasureHunts().size())) {
            checkedInIndex += 1;
            solvedCurrent = false;
        }

        // If we have checked in to the last treasure hunt, quest is complete.
        completed = checkedInIndex == questAttempted.getTreasureHunts().size();
    }

    /**
     * Checks if the current quest attempt is completed.
     * If true, the user has solved and checked in to every TreasureHunt in the quest.
     *
     * @return true if the quest is completed.
     */
    public boolean isCompleted() {
        return completed;
    }
}
