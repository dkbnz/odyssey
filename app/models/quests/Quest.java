package models.quests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import models.ApiError;
import models.BaseModel;
import models.Profile;
import models.objectives.Objective;

import javax.persistence.*;
import java.util.*;


/**
 * Class for quest, is used to initialise a quest.
 */
@Entity
public class Quest extends BaseModel {

    @ElementCollection
    private Map<String, Integer> countryOccurrences;

    private static final int MAX_TITLE_SIZE = 255;

    /**
     * List of objectives to be solved in this quest.
     */
    @ManyToMany(cascade = CascadeType.PERSIST)
    @OrderColumn
    private List<Objective> objectives;

    /**
     * List of attempts that have been had on this quest.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questAttempted")
    @JsonIgnore
    private List<QuestAttempt> attempts;

    /**
     * Title of this quest.
     */
    private String title;

    /**
     * Date which this quest starts.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssZ")
    private Date startDate;

    /**
     * Date which this quest ends.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssZ")
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
        } else if (title.length() > MAX_TITLE_SIZE) {
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

        if (objectives.isEmpty()) {
            errors.add(new ApiError("You must provide at least one Objective for a quest."));
        } else {
            for(Objective objective : objectives) {
                errors.addAll(objective.getErrors());
            }
        }

        return errors;
    }


    /**
     * Returns a set containing all countries within the quest objectives.
     *
     * @return      a set of strings of countries.
     */
    public Set<String> getObjectiveCountries() {
        Set<String> countries = new HashSet<>();
        for (Objective objective : objectives) {
            countries.add(objective.getDestination().getCountry());
        }
        return countries;
    }


    public List<Objective> getObjectives() {
        return objectives;
    }

    public void setObjectives(List<Objective> objectives) {
        this.objectives = objectives;
    }

    public boolean addObjective(Objective objective) {
        return objectives.add(objective);
    }

    public void clearObjectives() {
        objectives.clear();
    }

    public boolean removeObjective(Objective objective) {
        return objectives.remove(objective);
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

    public Profile getOwner() {
        return owner;
    }
}
