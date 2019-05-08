package controllers;


import org.junit.Before;
import org.junit.Test;

import play.Application;
import play.db.Database;
import play.db.evolutions.Evolutions;
import play.test.Helpers;

import static play.test.Helpers.fakeApplication;

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