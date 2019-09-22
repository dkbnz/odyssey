package steps;

import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import models.hints.Hint;
import models.hints.Vote;
import models.objectives.Objective;
import models.profiles.Profile;
import org.junit.Assert;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import repositories.hints.HintRepository;
import repositories.hints.VoteRepository;
import repositories.objectives.ObjectiveRepository;
import repositories.profiles.ProfileRepository;

import java.util.Arrays;

import static play.test.Helpers.*;

public class VoteTestSteps {

    /**
     * Singleton class which stores generally used variables.
     */
    private TestContext testContext = TestContext.getInstance();

    /**
     * Repository to access the objectives in the running application.
     */
    private ObjectiveRepository objectiveRepository =
            testContext.getApplication().injector().instanceOf(ObjectiveRepository.class);

    /**
     * Repository to access the objectives in the running application.
     */
    private ProfileRepository profileRepository =
            testContext.getApplication().injector().instanceOf(ProfileRepository.class);

    /**
     * Repository to access the hints in the running application.
     */
    private HintRepository hintRepository =
            testContext.getApplication().injector().instanceOf(HintRepository.class);


    /**
     * Repository to access the votes in the running application.
     */
    private VoteRepository voteRepository =
            testContext.getApplication().injector().instanceOf(VoteRepository.class);


    private static final String[] VOTE_STRINGS = {"upvote", "upvoted", "upvotes", "Upvote", "Upvoted", "Upvotes"};
    private static final String AUTHORIZED = "authorized";
    private static final String HINT_URI = "/v1/hints/";
    private static final String UPVOTE_URI = "/upvote/";
    private static final String DOWNVOTE_URI = "/downvote/";


    /**
     * Sends a request to create a vote for a given hint.
     */
    private void sendVoteRequest(int hintId, boolean isUpvote) {
        Http.RequestBuilder request = fakeRequest()
                .method(POST)
                .uri(HINT_URI + hintId + (isUpvote ? UPVOTE_URI : DOWNVOTE_URI ) + testContext.getTargetId())
                .session(AUTHORIZED, testContext.getLoggedInId());
        Result result = route(testContext.getApplication(), request);
        testContext.setStatusCode(result.status());
        testContext.setResponseBody(Helpers.contentAsString(result));
    }

    /**
     * Takes a vote string and converts it to a boolean value.
     * If the string matches any value within VOTE_STRINGS
     * will return true, or false otherwise.
     *
     * @param voteString    string to check the value of.
     * @return              true if the string is within VOTE_STRINGS.
     */
    private boolean voteToBool(String voteString) {
        return Arrays.stream(VOTE_STRINGS).parallel().anyMatch(voteString::contains);
    }

    @Given("^a hint with id (\\d+) does not exist$")
    public void aHintWithIdDoesNotExist(Integer hintId) {
        Assert.assertNull(hintRepository.findById(Long.valueOf(hintId)));
    }

    @Given("^a hint with id (\\d+) exists for objective with id (\\d+)$")
    public void aHintWithIdExistsForObjectiveWithId(Integer hintId, Integer objectiveId) {
        Hint hint = hintRepository.findById(Long.valueOf(hintId));
        Objective objective = objectiveRepository.findById(Long.valueOf(objectiveId));

        Assert.assertNotNull(hint);
        Assert.assertNotNull(objective);

        Assert.assertTrue(objective.getHints().contains(hint));
    }

    @Given("^the hint with id (\\d+) has (\\d+) (upvotes|downvotes)$")
    public void theHintWithIdHasVotes(Integer hintId, Integer numberOfVotes, String upvoteOrDownvote) {
        // Check if we are testing for upvotes or downvotes
        boolean isUpvote = voteToBool(upvoteOrDownvote);

        // Find the hint we are checking
        Hint hintToCheck = hintRepository.findById(Long.valueOf(hintId));
        Assert.assertNotNull(hintToCheck);

        // Get the actual votes from the hint
        Integer actualVotes = isUpvote ? hintToCheck.getUpVotes() : hintToCheck.getDownVotes();
        Assert.assertEquals(numberOfVotes, actualVotes);
    }

    @Given("^I have already (upvoted|downvoted) the hint with id (\\d+)$")
    public void iHaveAlreadyVotedTheHintWithId(String upvoteOrDownvote, Integer hintId) {
        // Check if we are testing for upvotes or downvotes
        boolean isUpvote = voteToBool(upvoteOrDownvote);

        Profile profile = profileRepository.findById(Long.valueOf(testContext.getLoggedInId()));
        Hint hintToVote = hintRepository.findById(Long.valueOf(hintId));

        Vote existingVote = voteRepository.findUsing(profile, hintToVote);

        Assert.assertNotNull(existingVote);
        Assert.assertEquals(isUpvote, existingVote.isUpVote());
    }

    @When("^I attempt to (upvote|downvote) a hint with id (\\d+) for user with id (\\d+)$")
    public void iAttemptToVoteOnHintWithId(String upvoteOrDownvote, Integer hintId, Integer targetUserId) {
        // Check if we are testing for upvotes or downvotes
        boolean isUpvote = voteToBool(upvoteOrDownvote);

        testContext.setTargetId(targetUserId.toString());
        sendVoteRequest(hintId, isUpvote);
    }
}
