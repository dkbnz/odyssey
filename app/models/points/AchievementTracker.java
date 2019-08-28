package models.points;

import io.ebean.annotation.JsonIgnore;
import models.profiles.Profile;
import models.util.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;


/**
 * Class for tracking the achievements a user has made. This includes Points and Badges.
 */
@Entity
public class AchievementTracker extends BaseModel {

    /**
     * The points attained for the user.
     */

    private int points;

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
    
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Adds the provided number of points to the points value.
     * @param pointsToAdd the points to be added.
     */
    public void addPoints(int pointsToAdd) {
        this.points += pointsToAdd;
    }

    public Profile getOwner() {
        return owner;
    }

    public void setOwner(Profile owner) {
        this.owner = owner;
    }
}
