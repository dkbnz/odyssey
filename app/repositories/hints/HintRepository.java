package repositories.hints;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import io.ebean.Expr;
import models.hints.Hint;
import models.objectives.Objective;
import models.profiles.Profile;
import repositories.profiles.ProfileRepository;

import java.util.List;

/**
 * Handles database interaction for hints.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class HintRepository extends BeanRepository<Long, Hint> {

    private static final String OBJECTIVE = "objective";

    private static final String NET_VOTES = "upVotes - downVotes";

    private static final String PROFILES_SEEN = "profilesSeen";

    private static final int TOP_HINT = 0;

    private ProfileRepository profileRepository;

    @Inject
    public HintRepository(ProfileRepository profileRepository) {
        super(Hint.class, Ebean.getDefaultServer());
        this.profileRepository = profileRepository;
    }


    /**
     * Retrieves ids of all distinct hints that a profile has seen for a given objective.
     *
     * @param objective     the objective that the hint is for.
     * @param hintUser      the user that is requesting the hints.
     * @return              a list of hint ids that the user has seen.
     */
    private List<Long> findSeenHintIds(Objective objective, Profile hintUser) {
        return query()
                .where()
                .eq(PROFILES_SEEN, hintUser)
                .eq(OBJECTIVE, objective)
                .setDistinct(true)
                .findIds();
    }


    /**
     * Queries the database for all the hints the given user has not yet used, and returns
     * those hints as a list.
     *
     * @param objective         the objective that the hint is for.
     * @param hintUser          the user that is requesting the hints.
     * @return                  the list of hints the user has not yet seen.
     */
    private List<Hint> findOrderedUnseenHints(Objective objective, Profile hintUser) {
        List<Long> hintsSeenByProfile = findSeenHintIds(objective, hintUser);

        return query()
                .where()
                .eq(OBJECTIVE, objective)
                .not(Expr.in("id", hintsSeenByProfile))
                .orderBy()
                    .desc(NET_VOTES)
                .findList();
    }


    /**
     * Given an objective, retrieves the hint with the greatest net value of votes (up-votes - down-votes).
     * Returns null if no hint can be found for the given objective.
     *
     * @param objectiveUsed     the objective that the hint is for.
     * @param hintUser          the user that is requesting the hint.
     * @return                  the first hint with the greatest net up-votes.
     */
    public Hint findAHint(Objective objectiveUsed, Profile hintUser) {
        List<Hint> orderedHints = findOrderedUnseenHints(objectiveUsed, hintUser);
        if (orderedHints.isEmpty()) {
            return null;
        }
        Hint unseenHint = orderedHints.get(TOP_HINT);
        hintUser.addSeenHint(unseenHint);
        profileRepository.save(hintUser);
        return unseenHint;
    }
}
