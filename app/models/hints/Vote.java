package models.hints;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.profiles.Profile;
import models.util.BaseModel;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Vote extends BaseModel {

    /**
     * The user voting.
     */
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Profile voter;

    /**
     * The hint receiving the vote.
     */
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Hint targetHint;

    /**
     * Determines the type of vote.
     */
    private boolean isUpVote;

    public Vote(Profile voter, Hint targetHint) {
        this.voter = voter;
        this.targetHint = targetHint;
    }

    public boolean isUpVote() {
        return isUpVote;
    }

    public void setUpVote(boolean upVote) {
        isUpVote = upVote;
    }
}