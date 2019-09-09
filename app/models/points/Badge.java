package models.points;

import com.fasterxml.jackson.annotation.JsonProperty;
import models.util.BaseModel;

import javax.persistence.*;

@Entity
public class Badge extends BaseModel {

    @Column(unique = true, nullable = false, name = "name")    // Declares that the name needs to be unique through the database.
    @Enumerated(EnumType.STRING)
    private Action name;

    private int silverBreakpoint;

    private int goldBreakpoint;

    private String howToProgress;

    @Transient
    private Integer progress;

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @JsonProperty("level")
    public int getLevel() {
        if (progress < 1) {
            return 0;
        } else if (progress < silverBreakpoint) {
            return 1;
        } else if (progress < goldBreakpoint) {
            return 2;
        }

        return 3;
    }

    @JsonProperty("howToProgress")
    public String getHowToProgress() {
        return "You need %s more %s to progress to %s";
    }
}
