package com.asalavei.tennisscoreboard.services;

import com.asalavei.tennisscoreboard.dto.Match;
import com.asalavei.tennisscoreboard.dto.Player;
import com.asalavei.tennisscoreboard.dto.PlayerScore;

import java.util.Map;
import java.util.UUID;

public class MatchScoreCalculationService {

    private static final int MIN_POINTS_TO_WIN_A_GAME = 4;
    private static final int MIN_POINTS_TO_WIN_A_SET = 6;
    private static final int MIN_POINTS_TO_WIN_TIE_BREAK = 7;
    private static final String GAME_ADVANTAGE = "AD";
    private static final String SCORE_40_POINTS = "40";

    private static final Map<Integer, Integer> TENNIS_POINTS_MAPPING = Map.of(
            1, 15,
            2, 30,
            3, 40
    );

    public Match calculate(Match match, UUID pointWinnerUuid) {
        Player firstPlayer = match.getFirstPlayer();
        Player secondPlayer = match.getSecondPlayer();

        PlayerScore firstPlayerScore = firstPlayer.getPlayerScore();
        PlayerScore secondPlayerScore = secondPlayer.getPlayerScore();

        Player pointWinner = determinePointWinner(match, pointWinnerUuid);
        PlayerScore pointWinnerScore = pointWinner.getPlayerScore();

        wonPoint(pointWinnerScore);

        if (isGameFinished(firstPlayerScore, secondPlayerScore)) {
            wonGame(pointWinnerScore);

            resetPoints(firstPlayerScore, secondPlayerScore);
        }

        if (isSetFinished(firstPlayerScore, secondPlayerScore)) {
            wonSet(pointWinnerScore);

            resetPoints(firstPlayerScore, secondPlayerScore);
            resetGames(firstPlayerScore, secondPlayerScore);
        }

        assignGamePoints(firstPlayerScore, secondPlayerScore);

        if (isMatchFinished(firstPlayerScore, secondPlayerScore)) {
            return Match.builder()
                    .uuid(match.getUuid())
                    .firstPlayer(firstPlayer)
                    .secondPlayer(secondPlayer)
                    .winner(pointWinner)
                    .build();
        }

        return Match.builder()
                .uuid(match.getUuid())
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .build();
    }

    private void wonPoint(PlayerScore playerScore) {
        playerScore.setPoints(playerScore.getPoints() + 1);
    }

    private void wonGame(PlayerScore playerScore) {
        playerScore.setGames(playerScore.getGames() + 1);
    }

    private void wonSet(PlayerScore playerScore) {
        playerScore.setSets(playerScore.getSets() + 1);
    }

    private Player determinePointWinner(Match match, UUID pointWinnerUuid) {
        if (pointWinnerUuid.equals(match.getFirstPlayer().getUuid())) {
            return match.getFirstPlayer();
        }

        if (pointWinnerUuid.equals(match.getSecondPlayer().getUuid())) {
            return match.getSecondPlayer();
        }

        throw new IllegalArgumentException("Invalid player UUID: " + pointWinnerUuid);
    }

    private boolean isGameFinished(PlayerScore firstPlayerScore, PlayerScore secondPlayerScore) {
        int firstPlayerPoints = firstPlayerScore.getPoints();
        int secondPlayerPoints = secondPlayerScore.getPoints();

        if (isTieBreak(firstPlayerScore, secondPlayerScore)) {
            return Math.max(firstPlayerPoints, secondPlayerPoints) >= MIN_POINTS_TO_WIN_TIE_BREAK &&
                    Math.abs(firstPlayerPoints - secondPlayerPoints) > 1;
        }

        return Math.max(firstPlayerPoints, secondPlayerPoints) >= MIN_POINTS_TO_WIN_A_GAME &&
                Math.abs(firstPlayerPoints - secondPlayerPoints) > 1;
    }

    private boolean isSetFinished(PlayerScore firstPlayerScore, PlayerScore secondPlayerScore) {
        int firstPlayerGames = firstPlayerScore.getGames();
        int secondPlayerGames = secondPlayerScore.getGames();

        if (Math.max(firstPlayerGames, secondPlayerGames) >= MIN_POINTS_TO_WIN_A_SET &&
                Math.abs(firstPlayerGames - secondPlayerGames) > 1) {
            // The score is 6-x or x-6 where x <= 4, or 7-5 or 5-7
            return true;
        }

        if (Math.max(firstPlayerGames, secondPlayerGames) > MIN_POINTS_TO_WIN_A_SET &&
                Math.min(firstPlayerGames, secondPlayerGames) == MIN_POINTS_TO_WIN_A_SET) {
            // The score is 6-7 or 7-6
            return true;
        }

        return false;
    }

    private boolean isTieBreak(PlayerScore firstPlayerScore, PlayerScore secondPlayerScore) {
        return firstPlayerScore.getGames() == 6 && secondPlayerScore.getGames() == 6;
    }

    private boolean isMatchFinished(PlayerScore firstPlayerScore, PlayerScore secondPlayerScore) {
        return firstPlayerScore.getSets() == 2 || secondPlayerScore.getSets() == 2;
    }

    private void resetPoints(PlayerScore firstPlayerScore, PlayerScore secondPlayerScore) {
        firstPlayerScore.setPoints(0);
        secondPlayerScore.setPoints(0);
    }

    private void resetGames(PlayerScore firstPlayerScore, PlayerScore secondPlayerScore) {
        firstPlayerScore.setGames(0);
        secondPlayerScore.setGames(0);
    }

    private void assignGamePoints(PlayerScore firstPlayerScore, PlayerScore secondPlayerScore) {
        int firstPlayerPoints = firstPlayerScore.getPoints();
        int secondPlayerPoints = secondPlayerScore.getPoints();

        if (isTieBreak(firstPlayerScore, secondPlayerScore)) {
            assignGamePointsForTieBreak(firstPlayerScore, secondPlayerScore);
            return;
        }

        if (isWithinRegularGamePoints(firstPlayerPoints, secondPlayerPoints)) {
            assignRegularGamePoints(firstPlayerScore, secondPlayerScore);
            return;
        }

        if (hasPlayerAdvantage(firstPlayerPoints, secondPlayerPoints)) {
            assignGameAdvantage(firstPlayerScore, secondPlayerScore);
            return;
        }

        if (hasPlayerAdvantage(secondPlayerPoints, firstPlayerPoints)) {
            assignGameAdvantage(secondPlayerScore, firstPlayerScore);
            return;
        }

        if (isDeuce(firstPlayerPoints, secondPlayerPoints)) {
            assignDeuceGamePoints(firstPlayerScore, secondPlayerScore);
        }
    }

    private void assignGamePointsForTieBreak(PlayerScore firstPlayerScore, PlayerScore secondPlayerScore) {
        firstPlayerScore.setGamePoints(String.valueOf(firstPlayerScore.getPoints()));
        secondPlayerScore.setGamePoints(String.valueOf(secondPlayerScore.getPoints()));
    }

    private boolean isWithinRegularGamePoints(int firstPlayerPoints, int secondPlayerPoints) {
        return firstPlayerPoints < MIN_POINTS_TO_WIN_A_GAME && secondPlayerPoints < MIN_POINTS_TO_WIN_A_GAME;
    }

    private void assignRegularGamePoints(PlayerScore firstPlayerScore, PlayerScore secondPlayerScore) {
        firstPlayerScore.setGamePoints(String.valueOf(getFormattedGamePoints(firstPlayerScore.getPoints())));
        secondPlayerScore.setGamePoints(String.valueOf(getFormattedGamePoints(secondPlayerScore.getPoints())));
    }

    private int getFormattedGamePoints(int gamePoints) {
        return TENNIS_POINTS_MAPPING.getOrDefault(gamePoints, gamePoints);
    }

    private boolean hasPlayerAdvantage(int playerPoints, int opponentPoints) {
        return playerPoints >= MIN_POINTS_TO_WIN_A_GAME && playerPoints > opponentPoints;
    }

    private void assignGameAdvantage(PlayerScore playerWithAdvantage, PlayerScore otherPlayer) {
        playerWithAdvantage.setGamePoints(GAME_ADVANTAGE);
        otherPlayer.setGamePoints(SCORE_40_POINTS);
    }

    private boolean isDeuce(int firstPlayerPoints, int secondPlayerPoints) {
        return firstPlayerPoints >= MIN_POINTS_TO_WIN_A_GAME && firstPlayerPoints == secondPlayerPoints;
    }

    private void assignDeuceGamePoints(PlayerScore firstPlayerScore, PlayerScore secondPlayerScore) {
        firstPlayerScore.setGamePoints(SCORE_40_POINTS);
        secondPlayerScore.setGamePoints(SCORE_40_POINTS);
    }
}
