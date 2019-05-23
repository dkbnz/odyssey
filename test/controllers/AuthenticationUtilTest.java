package controllers;

import models.Profile;
import org.junit.Assert;
import org.junit.Test;
import util.AuthenticationUtil;


public class AuthenticationUtilTest {

    @Test
    public void validUserTestIsAdmin() {
        //Arrange
        Profile admin = new Profile();
        admin.setId(12L);
        admin.setIsAdmin(true);

        Profile owner = new Profile();
        owner.setId(17L);

        //Act
        boolean result = AuthenticationUtil.validUser(admin, owner);

        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void validUserTestIsNotAdmin() {
        //Arrange
        Profile notAdmin = new Profile();
        notAdmin.setId(12L);
        notAdmin.setIsAdmin(false);

        Profile owner = new Profile();
        owner.setId(17L);

        //Act
        boolean result = AuthenticationUtil.validUser(notAdmin, owner);

        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void validUserTestIsOwner() {
        //Arrange
        Profile user = new Profile();
        user.setId(12L);
        user.setIsAdmin(false);

        Profile owner = new Profile();
        owner.setId(12L);

        //Act
        boolean result = AuthenticationUtil.validUser(user, owner);

        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void validUserTestIsNotOwner() {
        //Arrange
        Profile notOwner = new Profile();
        notOwner.setId(17L);
        notOwner.setIsAdmin(false);

        Profile owner = new Profile();
        owner.setId(12L);

        //Act
        boolean result = AuthenticationUtil.validUser(notOwner, owner);

        //Assert
        Assert.assertFalse(result);
    }
}
