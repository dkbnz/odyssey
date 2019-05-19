package controllers;

import models.Profile;
import org.junit.Assert;
import org.junit.Test;
import util.AuthenticationUtil;

public class AuthenticationUtilTest {

    @Test
    public void validUserTestIsAdmin() {
        //Arrange
        Long ownerId = 1234L;
        Long adminId = 1L;

        Profile admin = new Profile();
        admin.setIsAdmin(true);
        admin.setId(adminId);

        //Act
        boolean result = AuthenticationUtil.validUser(admin, ownerId);

        //Assert
        Assert.assertTrue(result);
        Assert.assertNotEquals(ownerId, adminId);
    }

    @Test
    public void validUserTestIsNotAdmin() {
        //Arrange
        Long ownerId = 1234L;
        Long userId = 321L;

        Profile notAdmin = new Profile();
        notAdmin.setIsAdmin(false);
        notAdmin.setId(userId);

        //Act
        boolean result = AuthenticationUtil.validUser(notAdmin, ownerId);

        //Assert
        Assert.assertFalse(result);
        Assert.assertNotEquals(ownerId, userId);
    }

    @Test
    public void validUserTestIsOwner() {
        //Arrange
        Long ownerId = 1234L;

        Profile owner = new Profile();
        owner.setIsAdmin(false);
        owner.setId(ownerId);

        //Act
        boolean result = AuthenticationUtil.validUser(owner, ownerId);

        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void validUserTestIsNotOwner() {
        //Arrange
        Long ownerId = 1234L;
        Long notOwnerId = 321L;

        Profile notOwner = new Profile();
        notOwner.setIsAdmin(false);
        notOwner.setId(notOwnerId);

        //Act
        boolean result = AuthenticationUtil.validUser(notOwner, ownerId);

        //Assert
        Assert.assertFalse(result);
    }
}
