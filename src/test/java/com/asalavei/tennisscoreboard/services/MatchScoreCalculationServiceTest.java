package com.asalavei.tennisscoreboard.services;

import com.asalavei.tennisscoreboard.enums.GamePoint;
import com.asalavei.tennisscoreboard.dto.Match;
import com.asalavei.tennisscoreboard.dto.Player;
import com.asalavei.tennisscoreboard.dto.PlayerScore;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MatchScoreCalculationServiceTest {

    MatchScoreCalculationService service = new MatchScoreCalculationService();

    private static final UUID POINT_WINNER_UUID = UUID.randomUUID();

    private static final int ZERO_GAMES = 0;
    private static final int ONE_GAME = 1;
    private static final int FOUR_GAMES = 4;
    private static final int FIVE_GAMES = 5;
    private static final int SIX_GAMES = 6;
    private static final int ZERO_SETS = 0;
    private static final int ONE_SET = 1;
    private static final int LOVE_POINTS = GamePoint.LOVE.getInternal();
    private static final int THIRTY_POINTS = GamePoint.THIRTY.getInternal();
    private static final int FORTY_POINTS = GamePoint.FORTY.getInternal();
    private static final int ADVANTAGE = GamePoint.ADVANTAGE.getInternal();

    @Test
    void calculate_shouldReturnWinner_whenMatchFinished() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ONE_SET, FIVE_GAMES, FORTY_POINTS)
                                )
                                .uuid(POINT_WINNER_UUID)
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, FOUR_GAMES, THIRTY_POINTS)
                                )
                                .uuid(UUID.randomUUID())
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, POINT_WINNER_UUID);

        assertEquals(POINT_WINNER_UUID, calculatedMatch.getWinner().getUuid());
    }

    @Test
    void calculate_shouldNotReturnWinner_whenOneSetIsWonAndGamesAreFiveAll() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ONE_SET, FIVE_GAMES, FORTY_POINTS))
                                .uuid(POINT_WINNER_UUID)
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ONE_SET, FIVE_GAMES, LOVE_POINTS))
                                .uuid(UUID.randomUUID())
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, POINT_WINNER_UUID);

        assertNull(calculatedMatch.getWinner());
        assertEquals(SIX_GAMES, calculatedMatch.getFirstPlayer().getPlayerScore().getGames());
        assertEquals(FIVE_GAMES, calculatedMatch.getSecondPlayer().getPlayerScore().getGames());
    }

    @Test
    void calculate_shouldNotFinishGame_whenDeuce() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, FORTY_POINTS))
                                .uuid(POINT_WINNER_UUID)
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, FORTY_POINTS))
                                .uuid(UUID.randomUUID())
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, POINT_WINNER_UUID);

        assertEquals(ZERO_GAMES, calculatedMatch.getFirstPlayer().getPlayerScore().getGames());
        assertEquals(GamePoint.ADVANTAGE.getDisplay(), calculatedMatch.getFirstPlayer().getPlayerScore().getDisplayPoints());
        assertEquals(GamePoint.FORTY.getDisplay(), calculatedMatch.getSecondPlayer().getPlayerScore().getDisplayPoints());
    }

    @Test
    void calculate_shouldIncrementPointsForWinner_whenPlayerWinsPoint() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, LOVE_POINTS))
                                .uuid(UUID.randomUUID())
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, THIRTY_POINTS))
                                .uuid(POINT_WINNER_UUID)
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, POINT_WINNER_UUID);

        assertEquals(GamePoint.FORTY.getDisplay(), calculatedMatch.getSecondPlayer().getPlayerScore().getDisplayPoints());
        assertEquals(GamePoint.LOVE.getDisplay(), calculatedMatch.getFirstPlayer().getPlayerScore().getDisplayPoints());
    }

    @Test
    void calculate_shouldIncrementGamesForWinnerPoint_whenFortyAndLovePoints() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, FORTY_POINTS))
                                .uuid(POINT_WINNER_UUID)
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, LOVE_POINTS))
                                .uuid(UUID.randomUUID())
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, POINT_WINNER_UUID);

        assertEquals(ONE_GAME, calculatedMatch.getFirstPlayer().getPlayerScore().getGames());
        assertEquals(ZERO_GAMES, calculatedMatch.getSecondPlayer().getPlayerScore().getGames());
    }

    @Test
    void calculate_shouldIncrementSetsForWinnerPoint_whenPlayerWinsSet() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, FIVE_GAMES, FORTY_POINTS))
                                .uuid(POINT_WINNER_UUID)
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, FOUR_GAMES, LOVE_POINTS))
                                .uuid(UUID.randomUUID())
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, POINT_WINNER_UUID);

        assertEquals(ONE_SET, calculatedMatch.getFirstPlayer().getPlayerScore().getSets());
        assertEquals(ZERO_SETS, calculatedMatch.getSecondPlayer().getPlayerScore().getSets());
    }

    @Test
    void calculate_shouldStartTiebreak_whenGamesAreSixAll() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, SIX_GAMES, 0))
                                .uuid(POINT_WINNER_UUID)
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, SIX_GAMES, 0))
                                .uuid(UUID.randomUUID())
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, POINT_WINNER_UUID);

        assertEquals("1", calculatedMatch.getFirstPlayer().getPlayerScore().getDisplayPoints());
    }

    @Test
    void calculate_shouldContinueTiebreak_whenTiebreakPointsLessThanSeven() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, SIX_GAMES, 0))
                                .uuid(UUID.randomUUID())
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, SIX_GAMES, 5))
                                .uuid(POINT_WINNER_UUID)
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, POINT_WINNER_UUID);

        assertEquals(ZERO_SETS, calculatedMatch.getSecondPlayer().getPlayerScore().getSets());
        assertEquals("6", calculatedMatch.getSecondPlayer().getPlayerScore().getDisplayPoints());
        assertEquals("0", calculatedMatch.getFirstPlayer().getPlayerScore().getDisplayPoints());
    }

    @Test
    void calculate_shouldContinueTiebreak_whenTiebreakPointsAreEqualAndMoreThanSeven() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ONE_SET, SIX_GAMES, 10))
                                .uuid(POINT_WINNER_UUID)
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, SIX_GAMES, 10))
                                .uuid(UUID.randomUUID())
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, POINT_WINNER_UUID);

        assertNull(calculatedMatch.getWinner());
        assertEquals(ONE_SET, calculatedMatch.getFirstPlayer().getPlayerScore().getSets());
        assertEquals("11", calculatedMatch.getFirstPlayer().getPlayerScore().getDisplayPoints());
        assertEquals("10", calculatedMatch.getSecondPlayer().getPlayerScore().getDisplayPoints());
    }

    @Test
    void calculate_shouldIncreaseSets_whenTiebreakPointsReachThreshold() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, SIX_GAMES, 6))
                                .uuid(POINT_WINNER_UUID)
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, SIX_GAMES, 2))
                                .uuid(UUID.randomUUID())
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, POINT_WINNER_UUID);

        assertAll(
                () -> assertEquals(ONE_SET, calculatedMatch.getFirstPlayer().getPlayerScore().getSets()),
                () -> assertEquals(ZERO_SETS, calculatedMatch.getSecondPlayer().getPlayerScore().getSets()),
                () -> assertEquals(ZERO_GAMES, calculatedMatch.getFirstPlayer().getPlayerScore().getGames()),
                () -> assertEquals(ZERO_GAMES, calculatedMatch.getSecondPlayer().getPlayerScore().getGames()),
                () -> assertEquals("0", calculatedMatch.getFirstPlayer().getPlayerScore().getDisplayPoints()),
                () -> assertEquals("0", calculatedMatch.getSecondPlayer().getPlayerScore().getDisplayPoints())
        );
    }

    @Test
    void calculate_shouldThrowIllegalArgumentException_whenInvalidPlayerUuid() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, LOVE_POINTS))
                                .uuid(UUID.randomUUID())
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, LOVE_POINTS))
                                .uuid(UUID.randomUUID())
                                .build()
                )
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.calculate(match, POINT_WINNER_UUID));
    }

    @Test
    void calculate_shouldResetToDeuce_whenPlayerLosesAdvantage() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, FORTY_POINTS))
                                .uuid(POINT_WINNER_UUID)
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, ADVANTAGE))
                                .uuid(UUID.randomUUID())
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, POINT_WINNER_UUID);

        assertEquals(GamePoint.FORTY.getDisplay(), calculatedMatch.getFirstPlayer().getPlayerScore().getDisplayPoints());
        assertEquals(GamePoint.FORTY.getDisplay(), calculatedMatch.getSecondPlayer().getPlayerScore().getDisplayPoints());
    }

    PlayerScore buildPlayerScore(int sets, int games, int internalPoints) {
        return PlayerScore.builder()
                .sets(sets)
                .games(games)
                .internalPoints(internalPoints)
                .build();
    }
}