package models.points;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import models.util.BaseModel;

import javax.persistence.*;

@Entity
public class Badge extends BaseModel {

    private static final String MAX_LEVEL = "You have fully achieved this badge!";
    private static final ImmutableList<String> LEVELS = ImmutableList.of("bronze", "silver", "gold", "max");

    // Unique value declares that the name needs to be unique through the database.
    @Column(unique = true, nullable = false, name = "action_to_achieve")
    @Enumerated(EnumType.STRING)
    private Action actionToAchieve;

    private String name;

    /**
     * Required progress to achieve the bronze level for the badge.
     */
    private int bronzeBreakpoint;

    /**
     * Required progress to achieve the silver level for the badge.
     */
    private int silverBreakpoint;

    /**
     * Required progress to achieve the gold level for the badge.
     */
    private int goldBreakpoint;

    private String howToProgress;

    @Transient
    private Integer progress;


    /**
     * Get the action required to achieve this badge.
     * Essentially an identifier for the badge.
     *
     * @return an enum specifying the action required to add progress towards the badge
     */
    public Action getActionToAchieve() { return actionToAchieve; }


    /**
     * Set the progress of the badge, used by the badge progress class to set the progress before returning.
     *
     * @param progress the int to set the progress to.
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }


    /**
     * Return the amount of progress that has been achieved toward this badge.
     *
     * @return an integer specifying the amount of meters to achieve.
     */
    public Integer getProgress() {
        return progress;
    }

    public String getName() {
        return name;
    }


    /**
     * Get the current level that this badge is at.
     *
     * @return an integer denoting the level of the badge.
     */
    @JsonProperty("level")
    public Integer getLevel() {
        if (progress == null) {
            return null;
        }

        if (progress < bronzeBreakpoint) {
            return 0;
        } else if (progress < silverBreakpoint) {
            return 1;
        } else if (progress < goldBreakpoint) {
            return 2;
        }

        return 3;
    }


    /**
     * Calculates the amount of progress required to get to the next level.
     * If the user is at max level, return null.
     *
     * @return number to reach the next level or null if at max.
     */
    @JsonProperty("breakpoint")
    public Integer getCurrentBreakpoint() {
        if (progress == null) {
            return null;
        }

        switch (this.getLevel()) {
            case 0:
                return bronzeBreakpoint;
            case 1:
                return silverBreakpoint;
            case 2:
                return goldBreakpoint;
            default:
                return null;
        }
    }


    /**
     * Fetch a string specifying the action to take to achieve the next level.
     *
     * @return a string specifying what needs to be done to achieve the next level.
     */
    @JsonProperty("howToProgress")
    public String getHowToProgress() {

        // If progress is not set, no way to progress to the next level.
        if(progress == null) {
            return null;
        }

        Integer currentBreakpoint = getCurrentBreakpoint();

        // If there is no next breakpoint, return the max level string.
        if (currentBreakpoint == null) {
            return MAX_LEVEL;
        }

        String nextLevel = LEVELS.get(this.getLevel());

        /*
        Take the string stored in the database in the format of:
        "You need %s more %s to progress to %s"
        then insert the relevant information.
        String will be different depending on the badge.
         */
        return String.format(howToProgress, currentBreakpoint - progress, nextLevel);
    }


}
