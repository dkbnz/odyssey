package repositories.hints;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.hints.Hint;
import models.objectives.Objective;
import models.profiles.Profile;

import java.util.List;

/**
 * Handles database interaction for hints.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class HintRepository extends BeanRepository<Long, Hint> {

    private static final String OBJECTIVE = "objective";

    private static final String NET_VOTES = "upVotes - downVotes";

    @Inject
    public HintRepository() {
        super(Hint.class, Ebean.getDefaultServer());
    }


    /**
     * Queries the database for all the hints the given user has not yet used, and returns
     * those hints as a list.
     *
     * @param objective         the objective for which the hints are retrieved.
     * @param hintUser          the user who is requesting the hint(s).
     * @return                  the list of hints the user has not yet seen.
     */
    private List<Hint> findOrderedUnseenHints(Objective objective, Profile hintUser) {
        // TODO Matthew || Matilda: filter out all hints the user has currently seen
        return query()
                .where()
                .eq(OBJECTIVE, objective)
                .orderBy()
                    .desc(NET_VOTES)
                .findList();
    }

    /**
     * Given an objective, retrieves the hint with the greatest net value of votes (up-votes - down-votes).
     *
     * @param objectiveUsed     the objective that the hint is wanted for.
     * @return                  the first hint with the greatest net up-votes.
     */
    public Hint findAHint(Objective objectiveUsed, Profile hintUser) {
        List<Hint> orderedHints = findOrderedUnseenHints(objectiveUsed, hintUser);
        for (Hint hint : orderedHints) {
            System.out.println(hint.getId());
        }
        // TODO Matthew || Matilda: Sort out bug with tests later :)
        Hint unseenHint = orderedHints.get(0);
        //hintUser.addSeenHint(unseenHint);
        return unseenHint;
    }
}
