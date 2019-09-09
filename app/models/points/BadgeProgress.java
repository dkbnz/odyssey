package models.points;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.destinations.Destination;
import models.util.BaseModel;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class BadgeProgress extends BaseModel {

    @ManyToOne
    private Badge badge;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnore
    private AchievementTracker achievementTracker;

    private int progress;

    public BadgeProgress(AchievementTracker achievementTracker, Badge badge) {
        this.badge = badge;
        this.achievementTracker = achievementTracker;
        this.progress = 0;
    }

    public Badge getBadge() {
        badge.setProgress(progress);
        return badge;
    }

    public void addProgress(int progress) {
        this.progress += progress;
    }
}
