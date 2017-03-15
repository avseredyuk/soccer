package gid;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Anton_Serediuk on 3/15/2017.
 */
public class GameTest {
    public static final String STRING_PLAYER_MESSI = "Messi";
    public static final String STRING_PLAYER_RONALDO = "Ronaldo";
    public static final String STRING_TEAM_BAVARIA = "Bavaria";
    public static final String STRING_TEAM_ARSENAL = "Arsenal";
    public static final long LONG_RONALDO_PRICE = 100000000L;
    public static final long LONG_MESSI_PRICE = 200000000L;
    boolean[] bool = new boolean[]{true, false, true, true};
    Game g;

    @Before
    public void setUp() throws Exception {
        g = new Game(new GameService(), STRING_TEAM_ARSENAL, STRING_TEAM_BAVARIA);
        GameService service = mock(GameService.class);
        when(service.getPriceOfPlayer(STRING_PLAYER_RONALDO)).thenReturn(LONG_RONALDO_PRICE);
        when(service.getPriceOfPlayer(STRING_PLAYER_MESSI)).thenReturn(LONG_MESSI_PRICE);
        when(service.getPlayerStatistics(STRING_PLAYER_MESSI)).thenReturn(bool);
        g.setGameService(service);
    }

    private void shootScore(int firstScore, int secondScore, int total) {
        for (int i = 1; i <= total; i++, firstScore--, secondScore--) {
            g.shoot(0, firstScore > 0, STRING_PLAYER_RONALDO);
            g.shoot(1, secondScore > 0, STRING_PLAYER_MESSI);
        }
    }

    @Test
    public void testGetScore() {
        int[] score = g.getScore();
        assertEquals(0, score[0]);
        assertEquals(0, score[1]);
    }

    @Test
    public void testIsFinished() {
        assertEquals(false, g.isFinished());
    }

    @Test
    public void testShoot() {
        shootScore(5, 0, 5);
        int[] score = g.getScore();
        assertEquals(5, score[0]);
        assertEquals(0, score[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTeamIndex() throws Exception {
        g.shoot(-100500, true, STRING_PLAYER_MESSI);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShootingOrder() {
        g.shoot(0, true, STRING_PLAYER_MESSI);
        g.shoot(0, true, STRING_PLAYER_MESSI);
    }

    @Test
    public void testFinishedAfterFirstWon() {
        shootScore(5, 0, 5);
        assertEquals(true, g.isFinished());
    }

    @Test
    public void testFinishedEarly() throws Exception {
        shootScore(3, 0, 5);
        assertEquals(true, g.isFinished());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShootAfterFinished() {
        shootScore(5, 0, 5);
        g.shoot(0, false, STRING_PLAYER_MESSI);
    }

    @Test
    public void testReturnPlayerStatisticsAfterShoot() {
        boolean[] playerResults = g.shoot(0, false, STRING_PLAYER_MESSI);
        assertArrayEquals(bool, playerResults);
    }

    @Test
    public void testScoreStringWithCostsOfPlayers() throws Exception {
        shootScore(7, 7, 8);
        assertEquals("[100000000] Arsenal (7) - (7) Bavaria [200000000]",
                g.getScoreString());
    }

    @Test
    public void testScoreStringBeforeSeventhSeries() {
        shootScore(5, 5, 5);
        assertEquals("Arsenal (5) - (5) Bavaria",
                g.getScoreString());

    }
}
