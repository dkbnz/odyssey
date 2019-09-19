package models.points;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.util.BaseModel;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class BadgeProgress extends BaseModel {


    /**
     * Badge to keep track of.
     */
    @ManyToOne
    private Badge badge;


    /**
     * Achievement Tracker belonging to the profile to track progress for.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnore
    private AchievementTracker achievementTracker;


    /**
     * Progress variable to store the progress of the tracked badge, this is directly associated with the breakpoints
     * given in the badge class.
     */
    private int progress;


    /**
     * BadgeProgress constructor.
     *
     * @param achievementTracker    the achievement tracker belonging to the profile that we want to track progress for.
     * @param badge                 the badge the profile has earned to track progress of.
     */
    public BadgeProgress(AchievementTracker achievementTracker, Badge badge) {
        this.badge = badge;
        this.achievementTracker = achievementTracker;
        this.progress = 0;
    }


    /**
     * Get the badge which we are tracking the progress of.
     *
     * @return  the badge belonging to the AchievementTracker.
     */
    public Badge getBadge() {
        // Set the progress of the badge before returning it.
        badge.setProgress(progress);
        return badge;
    }

    /**
     * Increment the progress variable by the specified amount.
     *
     * @param progress  integer denoting the amount of progress to increment by.
     */
    public void addProgress(int progress) {
        this.progress += progress;
    }
}