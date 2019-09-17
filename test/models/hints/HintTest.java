package models.hints;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HintTest {

    private Hint hint;

    @Before
    public void setUp() {
        // Arrange
        hint = new Hint();
    }

    @Test
    public void testUpVote() {
        // Act
        hint.upVote();

        // Assert
        Assert.assertEquals("Up votes did not increase.",1, hint.getUpVotes());
    }


    @Test
    public void testDownVote() {
        // Act
        hint.downVote();
        hint.downVote();

        // Assert
        Assert.assertEquals("Down votes did not increase.", 2, hint.getDownVotes());
    }


    @Test
    public void testVoteSum() {
        // Arrange
        hint.upVote();
        hint.upVote();
        hint.upVote();
        hint.upVote();
        hint.upVote();

        hint.downVote();
        hint.downVote();
        hint.downVote();

        // Act
        int voteSum = hint.getVoteSum();

        // Assert
        Assert.assertEquals("The vote sum calculation was incorrect.", 2, voteSum);
    }

}
