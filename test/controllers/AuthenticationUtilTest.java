package controllers;

import models.Profile;
import org.junit.Assert;
import org.junit.Test;
import util.AuthenticationUtil;


public class AuthenticationUtilTest {

    private static final long ADMIN_ID = 12L;
    private static final long OWNER_ID = 17L;



    @Test
    public void validUserTestIsAdmin() {
        //Arrange
        Profile admin = new Profile();
        admin.setId(ADMIN_ID);
        admin.setIsAdmin(true);

        Profile owner = new Profile();
        owner.setId(OWNER_ID);

        //Act
        boolean result = AuthenticationUtil.validUser(admin, owner);

        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void validUserTestIsNotAdmin() {
        //Arrange
        Profile notAdmin = new Profile();
        notAdmin.setId(ADMIN_ID);
        notAdmin.setIsAdmin(false);

        Profile owner = new Profile();
        owner.setId(OWNER_ID);

        //Act
        boolean result = AuthenticationUtil.validUser(notAdmin, owner);

        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void validUserTestIsOwner() {
        //Arrange
        Profile user = new Profile();
        user.setId(ADMIN_ID);
        user.setIsAdmin(false);

        Profile owner = new Profile();
        owner.setId(ADMIN_ID);

        //Act
        boolean result = AuthenticationUtil.validUser(user, owner);

        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void validUserTestIsNotOwner() {
        //Arrange
        Profile notOwner = new Profile();

        Profile owner = new Profile();
        owner.setId(ADMIN_ID);

        notOwner.setId(OWNER_ID);
        notOwner.setIsAdmin(false);

        //Act
        boolean result = AuthenticationUtil.validUser(notOwner, owner);

        //Assert
        Assert.assertFalse(result);
    }
}
