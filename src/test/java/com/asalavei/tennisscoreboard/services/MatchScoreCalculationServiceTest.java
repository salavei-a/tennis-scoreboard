package com.asalavei.tennisscoreboard.services;

import com.asalavei.tennisscoreboard.enums.GameScore;
import com.asalavei.tennisscoreboard.dto.Match;
import com.asalavei.tennisscoreboard.dto.Player;
import com.asalavei.tennisscoreboard.dto.PlayerScore;
import com.asalavei.tennisscoreboard.enums.PlayerNumber;
import org.junit.jupiter.api.Test;

import static com.asalavei.tennisscoreboard.enums.GameScore.*;

import static org.junit.jupiter.api.Assertions.*;

class MatchScoreCalculationServiceTest {

    MatchScoreCalculationService service = new MatchScoreCalculationService();

    private static final int FIRST_PLAYER_NUMBER = PlayerNumber.FIRST_PLAYER.getNumber();
    private static final int SECOND_PLAYER_NUMBER = PlayerNumber.SECOND_PLAYER.getNumber();
    private static final int ZERO_GAMES = 0;
    private static final int ONE_GAME = 1;
    private static final int FOUR_GAMES = 4;
    private static final int FIVE_GAMES = 5;
    private static final int SIX_GAMES = 6;
    private static final int ZERO_SETS = 0;
    private static final int ONE_SET = 1;

    @Test
    void calculate_shouldReturnWinner_whenMatchFinished() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ONE_SET, FIVE_GAMES, FORTY)
                                )
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, FOUR_GAMES, THIRTY)
                                )
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, FIRST_PLAYER_NUMBER);

        assertEquals(match.getFirstPlayer(), calculatedMatch.getWinner());
    }

    @Test
    void calculate_shouldNotReturnWinner_whenOneSetIsWonAndGamesAreFiveAll() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ONE_SET, FIVE_GAMES, FORTY))
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ONE_SET, FIVE_GAMES, LOVE))
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, FIRST_PLAYER_NUMBER);

        assertNull(calculatedMatch.getWinner());
        assertEquals(SIX_GAMES, calculatedMatch.getFirstPlayer().getPlayerScore().getGames());
        assertEquals(FIVE_GAMES, calculatedMatch.getSecondPlayer().getPlayerScore().getGames());
    }

    @Test
    void calculate_shouldNotFinishGame_whenDeuce() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, FORTY))
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, FORTY))
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, FIRST_PLAYER_NUMBER);

        assertEquals(ZERO_GAMES, calculatedMatch.getFirstPlayer().getPlayerScore().getGames());
        assertEquals(ADVANTAGE, calculatedMatch.getFirstPlayer().getPlayerScore().getGameScore());
        assertEquals(FORTY, calculatedMatch.getSecondPlayer().getPlayerScore().getGameScore());
    }

    @Test
    void calculate_shouldIncrementPointsForWinner_whenPlayerWinsPoint() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, LOVE))
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, THIRTY))
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, SECOND_PLAYER_NUMBER);

        assertEquals(FORTY, calculatedMatch.getSecondPlayer().getPlayerScore().getGameScore());
        assertEquals(LOVE, calculatedMatch.getFirstPlayer().getPlayerScore().getGameScore());
    }


    @Test
    void calculate_shouldIncrementGamesForWinnerPoint_whenFortyAndLovePoints() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, FORTY))
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, LOVE))
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, FIRST_PLAYER_NUMBER);

        assertEquals(ONE_GAME, calculatedMatch.getFirstPlayer().getPlayerScore().getGames());
        assertEquals(ZERO_GAMES, calculatedMatch.getSecondPlayer().getPlayerScore().getGames());
    }

    @Test
    void calculate_shouldIncrementSetsForWinnerPoint_whenPlayerWinsSet() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, FIVE_GAMES, FORTY))
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, FOUR_GAMES, LOVE))
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, FIRST_PLAYER_NUMBER);

        assertEquals(ONE_SET, calculatedMatch.getFirstPlayer().getPlayerScore().getSets());
        assertEquals(ZERO_SETS, calculatedMatch.getSecondPlayer().getPlayerScore().getSets());
    }

    @Test
    void calculate_shouldStartTiebreak_whenGamesAreSixAll() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, SIX_GAMES, 0))
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, SIX_GAMES, 0))
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, FIRST_PLAYER_NUMBER);

        assertEquals(1, calculatedMatch.getFirstPlayer().getPlayerScore().getTiebreakPoints());
    }

    @Test
    void calculate_shouldContinueTiebreak_whenTiebreakPointsLessThanSeven() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, SIX_GAMES, 0))
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, SIX_GAMES, 5))
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, SECOND_PLAYER_NUMBER);

        assertEquals(ZERO_SETS, calculatedMatch.getSecondPlayer().getPlayerScore().getSets());
        assertEquals(6, calculatedMatch.getSecondPlayer().getPlayerScore().getTiebreakPoints());
        assertEquals(0, calculatedMatch.getFirstPlayer().getPlayerScore().getTiebreakPoints());
    }

    @Test
    void calculate_shouldContinueTiebreak_whenTiebreakPointsAreEqualAndMoreThanSeven() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ONE_SET, SIX_GAMES, 10))
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, SIX_GAMES, 10))
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, FIRST_PLAYER_NUMBER);

        assertNull(calculatedMatch.getWinner());
        assertEquals(ONE_SET, calculatedMatch.getFirstPlayer().getPlayerScore().getSets());
        assertEquals(11, calculatedMatch.getFirstPlayer().getPlayerScore().getTiebreakPoints());
        assertEquals(10, calculatedMatch.getSecondPlayer().getPlayerScore().getTiebreakPoints());
    }

    @Test
    void calculate_shouldIncreaseSets_whenTiebreakOver() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, SIX_GAMES, 6))
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, SIX_GAMES, 2))
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, FIRST_PLAYER_NUMBER);

        assertAll(
                () -> assertEquals(ONE_SET, calculatedMatch.getFirstPlayer().getPlayerScore().getSets()),
                () -> assertEquals(ZERO_SETS, calculatedMatch.getSecondPlayer().getPlayerScore().getSets()),
                () -> assertEquals(ZERO_GAMES, calculatedMatch.getFirstPlayer().getPlayerScore().getGames()),
                () -> assertEquals(ZERO_GAMES, calculatedMatch.getSecondPlayer().getPlayerScore().getGames()),
                () -> assertNull(calculatedMatch.getFirstPlayer().getPlayerScore().getTiebreakPoints()),
                () -> assertNull(calculatedMatch.getSecondPlayer().getPlayerScore().getTiebreakPoints())
        );
    }

    @Test
    void calculate_shouldThrowIllegalArgumentException_whenInvalidPlayerNumber() {
        int invalidPlayerNumber = 3;

        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, LOVE))
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, LOVE))
                                .build()
                )
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.calculate(match, invalidPlayerNumber));
    }


    @Test
    void calculate_shouldResetToDeuce_whenPlayerLosesAdvantage() {
        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, FORTY))
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(ZERO_SETS, ZERO_GAMES, ADVANTAGE))
                                .build()
                )
                .build();

        Match calculatedMatch = service.calculate(match, FIRST_PLAYER_NUMBER);

        assertEquals(FORTY, calculatedMatch.getFirstPlayer().getPlayerScore().getGameScore());
        assertEquals(FORTY, calculatedMatch.getSecondPlayer().getPlayerScore().getGameScore());
    }

    PlayerScore buildPlayerScore(int sets, int games, GameScore gameScore) {
        return PlayerScore.builder()
                .sets(sets)
                .games(games)
                .gameScore(gameScore)
                .build();
    }

    PlayerScore buildPlayerScore(int sets, int games, Integer tiebreakPoints) {
        return PlayerScore.builder()
                .sets(sets)
                .games(games)
                .tiebreakPoints(tiebreakPoints)
                .build();
    }
}