package steps;

import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import models.objectives.Objective;
import org.junit.Assert;
import repositories.objectives.ObjectiveRepository;

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
//        TODO: Vinnie + Matilda
//        Collection<Objective> solvedObjectives = objectiveRepository.findAllCompletedUsing((testContext.getLoggedInId());
//        Objective objective = objectiveRepository.findById(Long.valueOf(objectiveId));
//        Assert.assertTrue(solvedObjectives.contains(objective));
    }

    @Given("^I have not solved the objective with id (\\d+)$")
    public void iHaveNotSolvedTheObjectiveWithId(Integer objectiveId) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("^I attempt to create a hint with the following values for the objective with id (\\d+)$")
    public void iAttemptToCreateAHintWithTheFollowingValuesForTheObjectiveWithIdNumber(io.cucumber.datatable.DataTable dataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        throw new cucumber.api.PendingException();
    }
}
