package controllers;

import models.Profile;
import org.junit.Assert;
import org.junit.Test;
import util.AuthenticationUtil;

public class AuthenticationUtilTest {

    @Test
    public void validUserTestIsAdmin() {
        //Arrange
        Integer admin = null;
        Integer ownerId = 1234;

        //Act
        boolean result = AuthenticationUtil.validUser(admin, ownerId);

        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void validUserTestIsNotAdmin() {
        //Arrange
        Integer notAdmin = null;
        Integer ownerId = 1234;

        //Act
        boolean result = AuthenticationUtil.validUser(notAdmin, ownerId);

        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void validUserTestIsOwner() {
        //Arrange
        Integer owner = 1234;
        Integer ownerId = 1234;

        //Act
        boolean result = AuthenticationUtil.validUser(owner, ownerId);

        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void validUserTestIsNotOwner() {
        //Arrange
        Integer notOwner = null;
        Integer ownerId = 1234;

        //Act
        boolean result = AuthenticationUtil.validUser(notOwner, ownerId);

        //Assert
        Assert.assertFalse(result);
    }
}
