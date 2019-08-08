package repositories.treasureHunts;

import com.google.inject.Inject;
import io.ebean.BeanRepository;
import io.ebean.Ebean;
import models.destinations.Destination;
import models.treasureHunts.TreasureHunt;

import java.util.List;


public class TreasureHuntRepository extends BeanRepository<Long, TreasureHunt> {

    @Inject
    public TreasureHuntRepository() {
        super(TreasureHunt.class, Ebean.getDefaultServer());
    }



    /**
     * Retrieve all treasure hunts that use the destination.
     *
     * @param destination   the destination being checked for usage within treasure hunts.
     * @return              a potentially empty list of treasure hunts that contain contain the destination parameter.
     */
    public List<TreasureHunt> getTreasureHuntsWithDestination(Destination destination) {
        return query()
                .where()
                .eq("destination", destination)
                .findList();
    }
}
