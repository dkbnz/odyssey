//package steps;
//
//import cucumber.api.java.en.Given;
//import cucumber.api.java.en.Then;
//import org.junit.Assert;
//import play.Application;
//
//import static org.junit.Assert.assertEquals;
//import static play.mvc.Http.Status.BAD_REQUEST;
//import static play.mvc.Http.Status.OK;
//import static play.mvc.Http.Status.UNAUTHORIZED;
//import static play.test.Helpers.fakeApplication;
//
//public class BaseTestSteps {
//
//    private int statusCode;
//    private Application application = fakeApplication();
//
//
//    @Given("I have a running application")
//    public void i_have_a_running_application() {
//        Assert.assertTrue(application.isTest());
//    }
//
//    @Then("the status code received is OK")
//    public void the_status_code_received_is_OK() {
//        assertEquals(OK, statusCode);
//    }
//
//    @Then("the status code received is BadRequest")
//    public void the_status_code_received_is_BadRequest() {
//        assertEquals(BAD_REQUEST, statusCode);
//    }
//
//    @Then("the status code received is Unauthorised")
//    public void the_status_code_received_is_Unauthorised() {
//        assertEquals(UNAUTHORIZED, statusCode);
//    }
//}
