package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Http.RequestImpl;
import static org.junit.Assert.*;
import play.mvc.Result;
import static play.test.Helpers.POST;
import static play.test.Helpers.fakeRequest;

public class AuthControllerTest {



    /** Attempt to log in with no authorized token in the current session **/
    @Test
    public void loginNoToken() {




    }

    /** Attempt to log in with an authorized token given, but not associated with any user **/
    @Test
    public void loginValidToken() {
    }


    /** Attempt to log in with an authorized token given, and associated with a user **/
    @Test
    public void loginInvalidToken() {

    }


    /** Attempt to log out with no authorized token given **/
    @Test
    public void logoutNoToken() {
    }


    /** Attempt to log out with an authorized token given, but not associated with a user **/
    @Test
    public void logoutInvalidToken() {

    }

    /** Attempt to log out with a valid authorized token given **/
    @Test
    public void logoutValidToken() {

    }
}