package repositories.hints;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.hints.Hint;
import models.hints.Vote;
import models.profiles.Profile;

/**
 * Handles database interaction for votes.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class VoteRepository extends BeanRepository<Long, Vote> {

    private static final String VOTER = "voter";
    private static final String TARGET_HINT = "targetHint";

    @Inject
    public VoteRepository() {
        super(Vote.class, Ebean.getDefaultServer());
    }


    /**
     * Retrieves a vote that contains the given user and hint.
     * Used to check if a user has already voted on a hint or to change a users vote.
     *
     * @param voter         the profile to find.
     * @param targetHint    the hint to find.
     * @return              the corresponding vote.
     *                      null if vote not found.
     */
    public Vote findUsing(Profile voter, Hint targetHint) {
        return query()
                .where()
                .eq(VOTER, voter)
                .eq(TARGET_HINT, targetHint)
                .findOne();
    }
}
