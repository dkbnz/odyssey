package models.hints;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import models.objectives.Objective;
import models.util.BaseModel;
import util.Views;

import javax.persistence.CascadeType;
import javax.persistence.OneToOne;

/**
 * Class for hints that are used to help solve an objective.
 */
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
    @OneToOne(cascade = CascadeType.PERSIST)
    private Objective objective;

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

    public void upVote() {
        upVotes ++;
    }

    public void downVote() {
        downVotes ++;
    }
}
