package steps;

import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import models.objectives.Objective;
import models.profiles.Profile;
import org.junit.Assert;
import repositories.objectives.ObjectiveRepository;
import repositories.profiles.ProfileRepository;

import java.util.Collection;

public class HintTestSteps {

    /**
     * Singleton class which stores generally used variables.
     */
    private TestContext testContext = TestContext.getInstance();


    /**
     * New instance of the general test steps.
     */
    private GeneralTestSteps generalTestSteps = new GeneralTestSteps();


    /**
     * Repository to access the objectives in the running application.
     */
    private ObjectiveRepository objectiveRepository =
            testContext.getApplication().injector().instanceOf(ObjectiveRepository.class);

    /**
     * Repository to access the profiles in the running application.
     */
    private ProfileRepository profileRepository =
            testContext.getApplication().injector().instanceOf(ProfileRepository.class);


    @Given("^I own the objective with id (\\d+)$")
    public void iOwnTheObjectiveWithId(Integer objectiveId) {
        Objective objective = objectiveRepository.findById(Long.valueOf(objectiveId));
        Assert.assertEquals(testContext.getLoggedInId(), objective.getOwner().getId().toString());
    }

    @Given("^I do not own the objective with id (\\d+)$")
    public void iDoNotOwnTheObjectiveWithId(Integer objectiveId) {
        Objective objective = objectiveRepository.findById(Long.valueOf(objectiveId));
        Assert.assertNotEquals(testContext.getLoggedInId(), objective.getOwner().getId().toString());
    }

    @Given("^I have solved the objective with id (\\d+)$")
    public void iHaveSolvedTheObjectiveWithId(Integer objectiveId) {
        Profile loggedInUser = profileRepository.findById(Long.parseLong(testContext.getLoggedInId()));
        Collection<Objective> solvedObjectives = objectiveRepository.findAllCompletedUsing(loggedInUser);
        Objective objective = objectiveRepository.findById(Long.valueOf(objectiveId));
        Assert.assertTrue(solvedObjectives.contains(objective));
    }

    @Given("^I have not solved the objective with id (\\d+)$")
    public void iHaveNotSolvedTheObjectiveWithId(Integer objectiveId) {
        Profile loggedInUser = profileRepository.findById(Long.parseLong(testContext.getLoggedInId()));
        Collection<Objective> solvedObjectives = objectiveRepository.findAllCompletedUsing(loggedInUser);
        Objective objective = objectiveRepository.findById(Long.valueOf(objectiveId));
        Assert.assertFalse(solvedObjectives.contains(objective));
    }


    @When("^I attempt to create a hint with the following values for the objective with id (\\d+)$")
    public void iAttemptToCreateAHintWithTheFollowingValuesForTheObjectiveWithId(Integer objectiveId, io.cucumber.datatable.DataTable dataTable) {
//        throw new cucumber.api.PendingException()
    }
}
