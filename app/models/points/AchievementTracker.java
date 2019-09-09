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

    @OneToMany(mappedBy = "achievementTracker", cascade = CascadeType.ALL)
    private Set<BadgeProgress> badgeProgressSet;


    /**
     * The user, who the has earned set of achievements listed.
     */
    @OneToOne
    @JsonIgnore
    private Profile owner;


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
     * @param pointsToAdd the points to be added to the tracker's total score.
     */
    public void addPoints(int pointsToAdd) {
        this.points += pointsToAdd;
    }

    public void addBadgeProgress(Badge badge, int progress) {
        Optional<BadgeProgress> badgeProgressOptional = badgeProgressSet.stream()
                .filter(badgeProgress -> badge
                        .getId()
                        .equals(badgeProgress
                                .getBadge()
                                .getId()
                        )
                ).findFirst();

        BadgeProgress badgeToProgress;

        if (badgeProgressOptional.isPresent()) {
            badgeToProgress = badgeProgressOptional.get();
        } else {
            badgeToProgress = new BadgeProgress(this, badge);
            badgeProgressSet.add(badgeToProgress);
        }

        badgeToProgress.addProgress(progress);
    }


    @JsonProperty("badges")
    public Set<Badge> getBadges() {
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
