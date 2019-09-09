package models.points;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import models.util.BaseModel;

import javax.persistence.*;

@Entity
public class Badge extends BaseModel {

    public static final ImmutableList<String> LEVELS = ImmutableList.of("bronze", "silver", "gold", "maxed");

    @Column(unique = true, nullable = false, name = "name")    // Declares that the name needs to be unique through the database.
    @Enumerated(EnumType.STRING)
    private Action name;

    private String badgeName;

    private int bronzeBreakpoint;

    private int silverBreakpoint;

    private int goldBreakpoint;

    private String howToProgress;

    @Transient
    private Integer progress;

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Integer getProgress() {
        return progress;
    }

    public String getBadgeName() {
        return badgeName;
    }

    @JsonProperty("level")
    public int getLevel() {
        if (progress < bronzeBreakpoint) {
            return 0;
        } else if (progress < silverBreakpoint) {
            return 1;
        } else if (progress < goldBreakpoint) {
            return 2;
        }

        return 3;
    }

    @JsonProperty("breakpoint")
    public Integer getCurrentBreakpoint() {
        switch (this.getLevel()) {
            case 0:
                return bronzeBreakpoint;
            case 1:
                return silverBreakpoint;
            case 2:
                return goldBreakpoint;
        }
        return null;
    }

    @JsonProperty("howToProgress")
    public String getHowToProgress() {

        String nextLevel = LEVELS.get(this.getLevel());
        Integer currentBreakpoint = getCurrentBreakpoint();

        if (currentBreakpoint == null) {
            return "You have maxed this badge!";
        }

        // "You need %s more %s to progress to %s";
        return String.format(howToProgress, currentBreakpoint - progress, nextLevel);
    }


}
