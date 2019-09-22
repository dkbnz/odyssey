package controllers.hints;

import controllers.points.AchievementTrackerController;
import models.hints.Hint;
import models.hints.Vote;
import models.profiles.Profile;
import models.util.ApiError;
import models.util.Errors;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repositories.hints.HintRepository;
import repositories.hints.VoteRepository;
import repositories.objectives.ObjectiveRepository;
import repositories.profiles.ProfileRepository;
import util.AuthenticationUtil;

import javax.inject.Inject;

import static play.mvc.Results.*;

public class VoteController {

    private ProfileRepository profileRepository;
    private HintRepository hintRepository;
    private ObjectiveRepository objectiveRepository;
    private VoteRepository voteRepository;
    private AchievementTrackerController achievementTrackerController;


    @Inject
    public VoteController(ProfileRepository profileRepository,
                          HintRepository hintRepository,
                          VoteRepository voteRepository,
                          AchievementTrackerController achievementTrackerController,
                          ObjectiveRepository objectiveRepository) {
        this.profileRepository = profileRepository;
        this.hintRepository = hintRepository;
        this.achievementTrackerController = achievementTrackerController;
        this.objectiveRepository = objectiveRepository;
        this.voteRepository = voteRepository;
    }


    /**
     * Controller method to handle the upvote or downvote of a given hint.
     *
     * @param request       the Http request containing the clients session information.
     * @param hintId        the id of the hint to vote on.
     * @param userId        the id of the user voting on the hint.
     * @param isUpvote      boolean to dictate if the intended vote is an upvote.
     * @return              ok() (Http 200) response containing the new hint json if everything is successful
     *                      badRequest() (Http 400) response if there exists an identical hint in the system.
     *                      unauthorized() (Http 401) response if the user is not logged into the system.
     *                      forbidden() (Http 403) response if the user is not allowed to vote on the given hint
     *                      notFound() (Http 404) response if the hint or target user does not exist.
     */
    public Result vote(Http.Request request, Long hintId, Long userId, boolean isUpvote) {

        Profile loggedInUser = AuthenticationUtil.validateAuthentication(profileRepository, request);
        if (loggedInUser == null) {
            return unauthorized(ApiError.unauthorized());
        }

        Hint hintToVoteOn = hintRepository.findById(hintId);
        if (hintToVoteOn == null) {
            return notFound(ApiError.notFound(Errors.HINT_NOT_FOUND));
        }

        Profile targetUser = profileRepository.findById(userId);

        if (targetUser == null) {
            return notFound(ApiError.notFound(Errors.PROFILE_NOT_FOUND));
        }

        Vote vote = voteRepository.findUsing(targetUser, hintToVoteOn);

        // Check if they have an existing vote
        if (vote != null) {

            // If they do, check if current vote is same as new vote
            if (vote.isUpVote() == isUpvote) {
                return badRequest(ApiError.badRequest(Errors.VOTE_ALREADY_EXISTS));
            } else if (isUpvote) {
                hintToVoteOn.removeDownVote();
            } else {
                achievementTrackerController.handleHintUpvote(hintToVoteOn, false);
                hintToVoteOn.removeUpVote();
            }
        } else if (objectiveRepository.hasSolved(targetUser, hintToVoteOn.getObjective())) {
            // User does not have an existing vote and they are allowed to vote.
            vote = new Vote(targetUser, hintToVoteOn);
        } else {
            // Votes do not exist and the user is not allowed to vote.
            return forbidden(ApiError.forbidden());
        }

        vote.setUpVote(isUpvote);

        // Increment upvote or downvote
        if (isUpvote) {
            achievementTrackerController.handleHintUpvote(hintToVoteOn, true);
            hintToVoteOn.upVote();
        } else {
            hintToVoteOn.downVote();
        }

        hintRepository.save(hintToVoteOn);
        voteRepository.save(vote);

        return ok(Json.toJson(hintToVoteOn));
    }
}
