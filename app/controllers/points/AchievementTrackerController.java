package controllers.points;

import models.destinations.Destination;
import models.objectives.Objective;
import models.quests.Quest;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public class AchievementTrackerController extends Controller {

    private static final String AUTHORISED = "authorized";
    private static final int DEFAULT_ADMON_ID = 1;

    public Result completeAction(Http.Request request) {
        return internalServerError();
    }

    public boolean completed(Quest quest) {
        return false;
        // TODO Matthew Implement
    }

    public boolean completed(Objective objective) {
        return false;
        // TODO Matthew Implement
    }

    public boolean created(Destination destination) {
        return false;
        // TODO Matthew Implement
    }

}
