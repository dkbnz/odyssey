package models.points;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.profiles.Profile;
import models.util.BaseModel;
import repositories.points.AchievementTrackerRepository;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Class for tracking the achievements a user has made. This includes Points and Badges.
 */
@Entity
public class AchievementTracker extends BaseModel {

    /**
     * The points attained for the user.
     */
    private int points;

    @JsonIgnore
    private int currentStreak;

    @OneToMany(mappedBy = "achievementTracker", cascade = CascadeType.ALL)
    private Set<BadgeProgress> badgeProgressSet;


    @JsonIgnore
    @Transient
    private Badge recentlyAchieved;


    /**
     * The user, who the has earned set of achievements listed.
     */
    @OneToOne
    @JsonIgnore
    private Profile owner;

    /**
     * gets the current streak for the user
     * @return the integer of the users current streak
     */
    public int getCurrentStreak() {
        return currentStreak;
    }

    /**
     * sets the current streak for the user
     */
    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    /**
     * Default constructor to set the points to a not-null default value.
     */
    public AchievementTracker() {
        this.points = 0;
    }

    public int getPoints() {
        return points;
    }


    /**
     * Adds the provided number of points to the points value.
     *
     * @param pointsToAdd the points to be added to the tracker's total score.
     */
    public int addPoints(int pointsToAdd) {
        this.points += pointsToAdd;
        return pointsToAdd;
    }


    /**
     * Add progress to the specified badge. If the profile has not achieved the badge, create it for them.
     *
     * @param badge     the badge to increment the progress of.
     * @param progress  the progress to add to the badge.
     */
    public void addBadgeProgress(Badge badge, int progress) {

        // Retrieve an optional for the badge progress.
        // Checks if there is already a badge progress class tracking the specified badge.
        Optional<BadgeProgress> badgeProgressOptional = badgeProgressSet.stream()
                .filter(badgeProgress -> badge
                        .getId()
                        .equals(badgeProgress
                                .getBadge()
                                .getId()
                        )
                ).findFirst();

        // Badge progress we are going to mutate.
        BadgeProgress badgeToProgress;

        if (badgeProgressOptional.isPresent()) {
            // If there is a badge progress class tracking the current badge, then retrieve it.
            badgeToProgress = badgeProgressOptional.get();
        } else {
            // Otherwise, create a new one and add it to the set of badges we are tracking progress of.
            badgeToProgress = new BadgeProgress(this, badge);
            badgeProgressSet.add(badgeToProgress);
        }

        // Check level
        int currentLevel = badgeToProgress.getBadge().getLevel();

        // Increment the progress by the specified amount.
        badgeToProgress.addProgress(progress);

        // If level increment, add to recently achieved
        if (badgeToProgress.getBadge().getLevel() != currentLevel) {
            recentlyAchieved = badgeToProgress.getBadge();
        }

    }

    public Badge getRecentlyAchieved() {
        return recentlyAchieved;
    }

    /**
     * Get all of the badges held by the set of badgeprogress bridging model.
     *
     * @return a set of badges that the user has achieved with their corresponding progress.
     */
    @JsonProperty("badges")
    public Set<Badge> getBadges() {

        // Extract all of the badges out of the badge progress set and return them as a set.
        return badgeProgressSet
                .stream()
                .map(BadgeProgress::getBadge)
                .collect(Collectors.toSet());
    }


    /**
     * Returns the rank of the profile associated with this achievement tracker.
     * Will be evaluated when serialized with toJson or any object mappers.
     *
     * @return an int defining the rank of the profile associated to this achievement tracker.
     */
    @JsonProperty("rank")
    public int getRank() {
        return AchievementTrackerRepository.getRank(this.owner);
    }
}
