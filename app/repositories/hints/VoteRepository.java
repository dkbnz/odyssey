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

    @Inject
    public VoteRepository() {
        super(Vote.class, Ebean.getDefaultServer());
    }

    public Vote findUsing(Profile voter, Hint targetHint) {
        return query().where().eq("voter", voter).eq("targetHint", targetHint).findOne();
    }
}
