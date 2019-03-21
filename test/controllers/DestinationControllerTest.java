package controllers;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import javax.inject.Inject;

import com.google.inject.Module;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.ApplicationLoader;
import play.Environment;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class DestinationControllerTest {

    @Inject Application application;

    @Before
    public void setup() {
        Module testModule = new AbstractModule() { };

        GuiceApplicationBuilder builder = new GuiceApplicationLoader()
                .builder(new ApplicationLoader.Context(Environment.simple()))
                .overrides(testModule);
        Guice.createInjector(builder.applicationModule()).injectMembers(this);

        Helpers.start(application);
    }

    @After
    public void tearDown() {
        Helpers.stop(application);
    }

    @Test
    public void testGetAllDestinations() {
        //Arrange
        Http.RequestBuilder request = fakeRequest()
                .method(GET)
                .uri("/destinations");

        //Act
        Result result = route(application, request);

        //Assert
        assertEquals(OK, result.status());
    }
}