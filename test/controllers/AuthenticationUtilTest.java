package controllers;

import models.Profile;
import org.junit.Assert;
import org.junit.Test;
import util.AuthenticationUtil;

public class AuthenticationUtilTest {

    @Test
    public void validUserTestIsAdmin() {
        //Arrange
        Profile admin = null;
        Long ownerId = 1234L;

        //Act
        boolean result = AuthenticationUtil.validUser(admin, ownerId);

        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void validUserTestIsNotAdmin() {
        //Arrange
        Profile notAdmin = null;
        Long ownerId = 1234L;

        //Act
        boolean result = AuthenticationUtil.validUser(notAdmin, ownerId);

        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void validUserTestIsOwner() {
        //Arrange
        Profile owner = null;
        Long ownerId = 1234L;

        //Act
        boolean result = AuthenticationUtil.validUser(owner, ownerId);

        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void validUserTestIsNotOwner() {
        //Arrange
        Profile notOwner = null;
        Long ownerId = 1234L;

        //Act
        boolean result = AuthenticationUtil.validUser(notOwner, ownerId);

        //Assert
        Assert.assertTrue(result);
    }
}
