package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import repositories.hints.HintRepository;
import repositories.objectives.ObjectiveRepository;

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
     * Repository to access the profiles in the running application.
     */
    private HintRepository hintRepository =
            testContext.getApplication().injector().instanceOf(HintRepository.class);


    @Given("^a hint with id (\\d+) does not exist$")
    public void aHintWithIdDoesNotExist(Integer hintId) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Given("^a hint with id (\\d+) exists for objective with id (\\d+)$")
    public void aHintWithIdExistsForObjectiveWithId(Integer hintId, Integer objectiveId) {

        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Given("^the hint with id (\\d+) has (\\d+) (upvotes|downvotes)$")
    public void theHintWithIdHasVotes(Integer hintId, Integer numberOfVotes, String upvoteOrDownvote) {


        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Given("^I have already (upvoted|downvoted) the hint with id (\\d+)$")
    public void iHaveAlreadyVotedTheHintWithId(String upvoteOrDownvote, Integer hintId) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("^I attempt to (upvote|downvote) a hint with id (\\d+)$")
    public void iAttemptToVoteOnHintWithId(String upvoteOrDownvote, Integer hintId) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

// TODO: Doug, it seems this conflicts with the @Given test.
//    @Then("^the hint with id (\\d+) now has (\\d+) (upvotes|downvotes)$")
//    public void theHintWithIdHasDownvotes(Integer hintId, Integer numberOfVotes, String upvoteOrDownvote) {
//        // Write code here that turns the phrase above into concrete actions
//        throw new cucumber.api.PendingException();
//    }

}
