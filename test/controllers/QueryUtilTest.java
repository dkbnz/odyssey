package controllers;


import org.junit.Assert;
import org.junit.Test;

import static util.QueryUtil.queryComparator;

public class QueryUtilTest {

    @Test
    public void queryComparatorTest() {
        //Arrange
        String field = "Canterbury";
        String expectedQuery = "%Canterbury%";

        //Act
        String result = queryComparator(field);

        //Assert
        Assert.assertEquals(expectedQuery, result);
    }

}
