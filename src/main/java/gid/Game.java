package gid;

/**
 * Created by Anton_Serediuk on 3/15/2017.
 */
public class Game {
    public static final String STRING_FORMAT_SCORE_STRING = "%s (%d) - (%d) %s";
    public static final String STRING_FORMAT_SCORE_STRING_WITH_COSTS = "[%d] %s (%d) - (%d) %s [%d]";
    private int[] score = new int[2];
    private int series;
    private int lastShootTeamIndex = -1;
    private GameService gameService;
    private String team1Name;
    private String team2Name;
    private long[] losersCosts = new long[2];

    public Game(GameService gameService, String team1Name, String team2Name) {
        this.gameService = gameService;
        this.team1Name = team1Name;
        this.team2Name = team2Name;
    }

    public boolean isFinished() {
        if ((lastShootTeamIndex == 1)
                && (score[0] != score[1])
                && ((series >= 5) && (Math.abs(score[0] - score[1]) >= 3))) {
            return true;
        }
        return false;
    }

    public boolean[] shoot(int teamIndex, boolean isGoal, String playerName) {
        if (teamIndex < 0 || teamIndex > 1) {
            throw new IllegalArgumentException("Invalid team index");
        }
        if (teamIndex == lastShootTeamIndex) {
            throw new IllegalArgumentException("Invalid shoot turn");
        }
        if (isFinished()) {
            throw new IllegalArgumentException("Can't shoot after finished");
        }
        if (isGoal) {
            score[teamIndex]++;
        } else {
            losersCosts[teamIndex] += gameService.getPriceOfPlayer(playerName);
        }
        if (teamIndex == 1) {
            series++;
        }
        lastShootTeamIndex = teamIndex;
        return gameService.getPlayerStatistics(playerName);
    }

    public String getScoreString() {
        if (series > 7) {
            return String.format(STRING_FORMAT_SCORE_STRING_WITH_COSTS,
                    losersCosts[0],
                    team1Name,
                    score[0],
                    score[1],
                    team2Name,
                    losersCosts[1]);
        } else {
            return String.format(STRING_FORMAT_SCORE_STRING,
                    team1Name,
                    score[0],
                    score[1],
                    team2Name);
        }
    }

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    public int[] getScore() {
        return score;
    }
}
