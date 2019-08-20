package models.quests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.BaseModel;
import models.Profile;
import models.destinations.Destination;
import models.objectives.Objective;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.List;


/**
 * Class for quest attempt, is used to initialise a quest attempt.
 * This is a user's instance of a currently active quest.
 */
@Entity
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
     * Boolean to indicate if the current objective of interest has been solved.
     */
    private boolean solvedCurrent;


    /**
     * Index of the most recently checked in objective.
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
     * Get a list of Objectives that the user has correctly guessed the destination for.
     *
     * @return  list of objectives the user has solved for this particular quest attempt.
     */
    @JsonProperty("solved")
    public List<Objective> getSolved() {
        return questAttempted
                .getObjectives()
                .subList(0, checkedInIndex + (solvedCurrent ? 1 : 0));
    }


    /**
     * Returns the current Objective the user needs to solve.
     * Will return null if there is no current objective to solve.
     * This means that the previously solved objective has not been checked in to, or the quest is complete.
     * When serialized using Json.toJson destination will not show in the objective.
     *
     * @return  current Objective to solve.
     */
    @JsonProperty("current")
    @JsonIgnoreProperties({"destination", "radius"})
    public Objective getCurrent() {
        if (!solvedCurrent && !completed) {
            return questAttempted
                    .getObjectives()
                    .get(checkedInIndex);
        } else {
            return null;
        }
    }


    /**
     * Returns a list of Objectives that the user is yet to solve.
     * When serialized using Json.toJson destinations will not show in the list of objectives.
     *
     * @return  a list of unsolved objectives
     */
    @JsonProperty("unsolved")
    @JsonIgnoreProperties({"destination", "radius"})
    public List<Objective> getUnsolved() {
        return questAttempted
                .getObjectives()
                .subList(checkedInIndex + (solvedCurrent ? 1 : 0),
                        questAttempted.getObjectives().size()
                );
    }


    /**
     * Provide a destination to attempt to solve the current Objective.
     *
     * @param destination   the destination that the user has entered as a guess.
     * @return              true if the guess was correct.
     */
    public boolean solveCurrent(Destination destination) {
        if(!solvedCurrent && !completed) {
            solvedCurrent = questAttempted
                    .getObjectives()
                    .get(checkedInIndex)
                    .getDestination()
                    .equals(destination);
            return solvedCurrent;
        } else {
            return false;
        }
    }


    /**
     * Check in to the most recently solved Objective.
     * Will make the next Objective available to solve.
     * If the user checks in to the last Objective, Quest will be complete.
     */
    public void checkIn() {
        if(solvedCurrent && !completed && (checkedInIndex < questAttempted.getObjectives().size())) {
            checkedInIndex += 1;
            solvedCurrent = false;
        }

        // If we have checked in to the last objective, quest is complete.
        completed = checkedInIndex == questAttempted.getObjectives().size();
    }


    /**
     * Checks if the current quest attempt is completed.
     * If true, the user has solved and checked in to every Objective in the quest.
     *
     * @return true if the quest is completed.
     */
    public boolean isCompleted() {
        return completed;
    }
}
