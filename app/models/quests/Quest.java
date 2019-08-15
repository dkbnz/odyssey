package models.quests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.ApiError;
import models.BaseModel;
import models.Profile;
import models.treasureHunts.TreasureHunt;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
public class Quest extends BaseModel {

    /**
     * List of treasure hunts to be solved in this quest.
     */
    @ManyToMany
    @OrderColumn
    private List<TreasureHunt> treasureHunts;

    /**
     * List of attempts that have been had on this quest.
     */
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<QuestAttempt> attempts;

    /**
     * Title of this quest.
     */
    private String title;

    /**
     * Date which this quest starts.
     */
    private Date startDate;

    /**
     * Date which this quest ends.
     */
    private Date endDate;

    /**
     * Owner of this quest.
     */
    @ManyToOne
    private Profile owner;

    @JsonIgnore
    public Collection<ApiError> getErrors() {
        List<ApiError> errors = new ArrayList<>();

        if (title == null || title.isEmpty()) {
            errors.add(new ApiError("A quest title must be provided."));
        } else if (title.length() > 255) {
            errors.add(new ApiError("Quest titles must not exceed 255 characters in length."));
        }

        if(startDate == null) {
            errors.add(new ApiError("You must provide a start date."));
        }

        if(endDate == null) {
            errors.add(new ApiError("You must provide an end date."));
        }

        if (startDate != null && endDate != null && endDate.before(startDate)) {
            errors.add(new ApiError("Start date must be before end date."));
        }

        if (owner == null) {
            errors.add(new ApiError("This quest does not have an owner."));
        }

        if (treasureHunts.isEmpty()) {
            errors.add(new ApiError("You must provide at least one Treasure Hunt for a quest"));
        }

        return errors;
    }


    public List<TreasureHunt> getTreasureHunts() {
        return treasureHunts;
    }

    public void setTreasureHunts(List<TreasureHunt> treasureHunts) {
        this.treasureHunts = treasureHunts;
    }

    public boolean addTreasureHunt(TreasureHunt treasureHunt) {
        return treasureHunts.add(treasureHunt);
    }

    public boolean removeTreasureHunt(TreasureHunt treasureHunt) {
        return treasureHunts.remove(treasureHunt);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setOwner(Profile owner) {
        this.owner = owner;
    }



}
