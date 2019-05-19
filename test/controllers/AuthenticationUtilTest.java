package controllers;

import org.junit.Assert;
import org.junit.Test;
import util.AuthenticationUtil;


/**
 *
 *
 * ========== TESTS ARE FAILING AS WE REQUIRE POWERMOCK TO MOCK STATIC AND PRIVATE METHODS ============
 * =============================== BUSINESS CASE PENDING... ==================================
 *
 *
 */
public class AuthenticationUtilTest {

    @Test
    public void validUserTestIsAdmin() {
        //Arrange
        Integer admin = 1;
        Integer ownerId = 1234;

        //Act
        boolean result = AuthenticationUtil.validUser(admin, ownerId);

        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void validUserTestIsNotAdmin() {
        //Arrange
        Integer notAdmin = 2;
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
        Integer notOwner = 5;
        Integer ownerId = 1234;

        //Act
        boolean result = AuthenticationUtil.validUser(notOwner, ownerId);

        //Assert
        Assert.assertFalse(result);
    }
}
