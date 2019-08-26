package models.points;

import io.ebean.annotation.JsonIgnore;
import models.profiles.Profile;
import models.quests.Quest;
import models.util.BaseModel;

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

    public int getPoints() {
        return points;
    }

    /**
     * TODO Matthew: Look at limiting how points are set, maybe even an 'add points' method.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    public Profile getOwner() {
        return owner;
    }

    public void setOwner(Profile owner) {
        this.owner = owner;
    }
}
