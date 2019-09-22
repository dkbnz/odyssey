package models.hints;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import models.objectives.Objective;
import models.profiles.Profile;
import models.util.BaseModel;
import util.Views;

import javax.persistence.*;
import java.util.Set;

/**
 * Class for hints that are used to help solve an objective.
 */
@Entity
public class Hint extends BaseModel {

    /**
     * The hint message.
     */
    @JsonView(Views.Public.class)
    private String message;

    /**
     * The number of up votes a hint has received.
     */
    @JsonView(Views.Public.class)
    private int upVotes = 0;

    /**
     * The number of down votes a hint has received.
     */
    @JsonView(Views.Public.class)
    private int downVotes = 0;

    /**
     * The objective the hint relates to.
     */
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Objective objective;

    /**
     * The user that created the hint.
     */
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Profile creator;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Vote> votes;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Profile> profilesSeen;

    public String getMessage() {
        return message;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Objective getObjective() {
        return objective;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    public Profile getCreator() {
        return creator;
    }

    /**
     * Increment up votes.
     */
    public void upVote() {
        upVotes++;
    }


    /**
     * Increment down votes.
     */
    public void downVote() {
        downVotes++;
    }

    /**
     * Decrement up votes.
     */
    public void removeUpVote() {
        upVotes--;
    }

    /**
     * Decrement down votes.
     */
    public void removeDownVote() {
        downVotes--;
    }

    /**
     * Calculate the vote sum by subtracting the down votes from the up votes.
     *
     * @return      the vote sum, which is of primitive type int.
     */
    public int getVoteSum() {
        return upVotes - downVotes;
    }

}
