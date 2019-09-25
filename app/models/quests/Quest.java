package models.quests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import models.util.ApiError;
import models.util.BaseModel;
import models.profiles.Profile;
import models.objectives.Objective;
import models.util.Errors;

import javax.persistence.*;
import java.util.*;


/**
 * Class for quest, is used to initialise a quest.
 */
@Entity
public class Quest extends BaseModel {

    private static final int MAX_TITLE_SIZE = 255;

    /**
     * List of objectives to be solved in this quest.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questUsing")
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
            errors.add(new ApiError(Errors.NO_QUEST_TITLE.toString()));
        } else if (title.length() > MAX_TITLE_SIZE) {
            errors.add(new ApiError(Errors.LONG_QUEST_TITLE.toString()));
        }

        if(startDate == null) {
            errors.add(new ApiError(Errors.NO_START_DATE.toString()));
        }

        if(endDate == null) {
            errors.add(new ApiError(Errors.NO_END_DATE.toString()));
        }

        if (startDate != null && endDate != null && endDate.before(startDate)) {
            errors.add(new ApiError(Errors.STAR_DATE_BEFORE_END_DATE.toString()));
        }

        if (owner == null) {
            errors.add(new ApiError(Errors.NO_QUEST_OWNER.toString()));
        }

        if (objectives.isEmpty()) {
            errors.add(new ApiError(Errors.QUEST_NEED_ONE_OBJECTIVE.toString()));
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
