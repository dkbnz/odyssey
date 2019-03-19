package controllers;

import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;

public class DestinationControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testGet() {
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri("/destinations");
        Result result = Helpers.route(app, request);
        assertEquals(OK, result.status());

    }
}
