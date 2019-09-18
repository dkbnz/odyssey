package repositories.hints;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.hints.Vote;

/**
 * Handles database interaction for votes.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class VoteRepository extends BeanRepository<Long, Vote> {

    @Inject
    public VoteRepository() {
        super(Vote.class, Ebean.getDefaultServer());
    }
}
