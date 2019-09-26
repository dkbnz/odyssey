package repositories.destinations;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.destinations.Type;

import javax.annotation.Nonnull;
import java.util.List;


/**
 * Handles database interaction for destination types.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class DestinationTypeRepository extends BeanRepository<Long, Type> {

    private static final String DEST_TYPE_FIELD = "destinationType";

    @Inject
    public DestinationTypeRepository() {
        super(Type.class, Ebean.getDefaultServer());
    }

    @Nonnull
    @Override
    public List<Type> findAll() {
        return query()
                .orderBy()
                    .asc(DEST_TYPE_FIELD)
                .findList();
    }
}
