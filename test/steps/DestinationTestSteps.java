package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.BeansException;

public class DestinationTestSteps {

    @Given("I have a running application")
    public void i_have_a_running_application() throws BeansException {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("I send a GET request to the \\/destinations endpoint")
    public void i_send_a_GET_request_to_the_destinations_endpoint() throws BeansException {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the status code received is {int}")
    public void the_status_code_received_is(Integer int1) throws BeansException {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }
}
