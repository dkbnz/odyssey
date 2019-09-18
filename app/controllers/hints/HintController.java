package controllers.hints;

import play.mvc.Http;
import play.mvc.Result;

import static play.mvc.Results.created;

public class HintController {


    /**
     * Creates a new hint for an objective in a quest.
     *
     * @param request           the Http request containing a Json body of the new hint.
     * @param objectiveId       the Id of the objective the hint will be created for.
     * @param userId            the Id of the user who will own the hint created.
     *
     * @return
     */
    public Result create(Http.Request request, Long objectiveId, Long userId) {
        return created();
    }
}
