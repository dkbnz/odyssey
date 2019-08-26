package controllers.points;

import models.destinations.Destination;
import models.objectives.Objective;
import models.quests.Quest;
import play.mvc.Controller;
import play.mvc.Result;

public class AchievementTrackerController extends Controller {

    private static final String AUTHORISED = "authorized";
    private static final int DEFAULT_ADMON_ID = 1;

    public Result completed(Quest quest) {
        return null;
        // TODO Matthew Implement
    }

    public Result completed(Objective objective) {
        return null;
        // TODO Matthew Implement
    }

    public Result created(Destination destination) {
        return null;
        // TODO Matthew Implement
    }

}
