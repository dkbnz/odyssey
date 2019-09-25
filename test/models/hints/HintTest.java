package models.hints;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HintTest {

    private Hint hint;

    private static final String UP_VOTE_NOT_INCREASE = "Up votes did not increase.";
    private static final String DOWN_VOTE_NOT_INCREASE = "Down votes did not increase.";
    private static final String VOTE_SUM_CALCULATION_INCORRECT = "The vote sum calculation was incorrect.";


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
        Assert.assertEquals(UP_VOTE_NOT_INCREASE,1, hint.getUpVotes());
    }


    @Test
    public void testDownVote() {
        // Act
        hint.downVote();
        hint.downVote();

        // Assert
        Assert.assertEquals(DOWN_VOTE_NOT_INCREASE, 2, hint.getDownVotes());
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
        Assert.assertEquals(VOTE_SUM_CALCULATION_INCORRECT, 2, voteSum);
    }

}
