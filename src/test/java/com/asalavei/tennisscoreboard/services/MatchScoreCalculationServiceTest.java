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

    @Test
    void calculate_shouldNotReturnWinner_whenDeuce() {
        UUID pointWinnerUuid = UUID.randomUUID();

        PlayerScore deuceScore = buildPlayerScore(0, 0, GamePoint.FORTY.getInternal());

        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(deuceScore)
                                .uuid(pointWinnerUuid)
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(deuceScore)
                                .uuid(UUID.randomUUID())
                                .build()
                )
                .build();

        Match result = service.calculate(match, pointWinnerUuid);

        assertNull(result.getWinner());
    }

    @Test
    void calculate_shouldReturnWinner_whenMatchFinished() {
        UUID pointWinnerUuid = UUID.randomUUID();

        Match match = Match.builder()
                .firstPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(1, 5, GamePoint.FORTY.getInternal())
                                )
                                .uuid(pointWinnerUuid)
                                .build()
                )
                .secondPlayer(
                        Player.builder()
                                .playerScore(buildPlayerScore(0, 4, GamePoint.THIRTY.getInternal())
                                )
                                .uuid(UUID.randomUUID())
                                .build()
                )
                .build();

        Match result = service.calculate(match, pointWinnerUuid);

        assertEquals(pointWinnerUuid, result.getWinner().getUuid());
    }

    PlayerScore buildPlayerScore(int sets, int games, int internalPoints) {
        return PlayerScore.builder()
                .sets(sets)
                .games(games)
                .internalPoints(internalPoints)
                .build();
    }
}