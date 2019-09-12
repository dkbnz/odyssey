package repositories.objectives;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.destinations.Destination;
import models.objectives.Objective;
import play.libs.Json;

import java.util.List;


/**
 * Handles database interaction for objectives.
 * Extends the BeanRepository containing all CRUD methods.
 */
public class ObjectiveRepository extends BeanRepository<Long, Objective> {

    private static final String DESTINATION = "destination";
    private static final String OWNER = "owner";
    private static final String RIDDLE = "riddle";
    private static final String RADIUS = "radius";
    private static final String ID = "id";

    @Inject
    public ObjectiveRepository() {
        super(Objective.class, Ebean.getDefaultServer());
    }


    /**
     * Retrieve all objectives that use the destination.
     *
     * @param destination   the destination being checked for usage within objectives.
     * @return              a potentially empty list of objectives that contain contain the destination parameter.
     */
    public List<Objective> findAllUsing(Destination destination) {
        return query()
                .where()
                .eq(DESTINATION, destination)
                .findList();
    }

    /**
     * Returns a list of objectives that are equal, excluding the given objective.
     *
     * @param objective     objective to search with.
     * @return              list of objectives that are equal.
     */
    public List<Objective> findEqual(Objective objective) {
        System.out.println(Json.toJson(objective));
        return query()
                .where()
                .eq(RIDDLE, objective.getRiddle())
                .eq(RADIUS, objective.getRadius())
                .eq(DESTINATION, objective.getDestination())
                .eq(ID, objective.getId())
                .findList();
    }
}
