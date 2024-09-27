package com.asalavei.tennisscoreboard.services;

import com.asalavei.tennisscoreboard.dto.Match;
import com.asalavei.tennisscoreboard.dto.Player;

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

        Player pointWinner = determinePointWinner(match, pointWinnerUuid);

        wonPoint(pointWinner);

        if (isGameFinished(match)) {
            wonGame(pointWinner);

            resetPoints(match);
        }

        if (isSetFinished(match)) {
            wonSet(pointWinner);

            resetPoints(match);
            resetGames(match);
        }

        assignGamePoints(match);

        if (isMatchFinished(match)) {
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

    private void wonPoint(Player player) {
        player.setPoints(player.getPoints() + 1);
    }

    private void wonGame(Player player) {
        player.setGames(player.getGames() + 1);
    }

    private void wonSet(Player player) {
        player.setSets(player.getSets() + 1);
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

    private boolean isGameFinished(Match match) {
        int firstPlayerPoints = match.getFirstPlayer().getPoints();
        int secondPlayerPoints = match.getSecondPlayer().getPoints();

        if (isTieBreak(match)) {
            return Math.max(firstPlayerPoints, secondPlayerPoints) >= MIN_POINTS_TO_WIN_TIE_BREAK &&
                    Math.abs(firstPlayerPoints - secondPlayerPoints) > 1;
        }

        return Math.max(firstPlayerPoints, secondPlayerPoints) >= MIN_POINTS_TO_WIN_A_GAME &&
                Math.abs(firstPlayerPoints - secondPlayerPoints) > 1;
    }

    private boolean isSetFinished(Match match) {
        int firstPlayerGames = match.getFirstPlayer().getGames();
        int secondPlayerGames = match.getSecondPlayer().getGames();

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

    private boolean isTieBreak(Match match) {
        return match.getFirstPlayer().getGames() == 6 && match.getSecondPlayer().getGames() == 6;
    }

    private boolean isMatchFinished(Match match) {
        return match.getFirstPlayer().getSets() == 2 || match.getSecondPlayer().getSets() == 2;
    }

    private void resetPoints(Match match) {
        match.getFirstPlayer().setPoints(0);
        match.getSecondPlayer().setPoints(0);
    }

    private void resetGames(Match match) {
        match.getFirstPlayer().setGames(0);
        match.getSecondPlayer().setGames(0);
    }

    private void assignGamePoints(Match match) {
        Player firstPlayer = match.getFirstPlayer();
        Player secondPlayer = match.getSecondPlayer();

        int firstPlayerPoints = firstPlayer.getPoints();
        int secondPlayerPoints = secondPlayer.getPoints();

        if (isTieBreak(match)) {
            assignGamePointsForTieBreak(firstPlayer, secondPlayer);
            return;
        }

        if (isWithinRegularGamePoints(firstPlayerPoints, secondPlayerPoints)) {
            assignRegularGamePoints(firstPlayer, secondPlayer);
            return;
        }

        if (hasPlayerAdvantage(firstPlayerPoints, secondPlayerPoints)) {
            assignGameAdvantage(firstPlayer, secondPlayer);
            return;
        }

        if (hasPlayerAdvantage(secondPlayerPoints, firstPlayerPoints)) {
            assignGameAdvantage(secondPlayer, firstPlayer);
            return;
        }

        if (isDeuce(firstPlayerPoints, secondPlayerPoints)) {
            assignDeuceGamePoints(firstPlayer, secondPlayer);
        }
    }

    private void assignGamePointsForTieBreak(Player firstPlayer, Player secondPlayer) {
        firstPlayer.setGamePoints(String.valueOf(firstPlayer.getPoints()));
        secondPlayer.setGamePoints(String.valueOf(secondPlayer.getPoints()));
    }

    private boolean isWithinRegularGamePoints(int firstPlayerPoints, int secondPlayerPoints) {
        return firstPlayerPoints < MIN_POINTS_TO_WIN_A_GAME && secondPlayerPoints < MIN_POINTS_TO_WIN_A_GAME;
    }

    private void assignRegularGamePoints(Player firstPlayer, Player secondPlayer) {
        firstPlayer.setGamePoints(String.valueOf(getFormattedGamePoints(firstPlayer.getPoints())));
        secondPlayer.setGamePoints(String.valueOf(getFormattedGamePoints(secondPlayer.getPoints())));
    }

    private int getFormattedGamePoints(int gamePoints) {
        return TENNIS_POINTS_MAPPING.getOrDefault(gamePoints, gamePoints);
    }

    private boolean hasPlayerAdvantage(int playerPoints, int opponentPoints) {
        return playerPoints >= MIN_POINTS_TO_WIN_A_GAME && playerPoints > opponentPoints;
    }

    private void assignGameAdvantage(Player playerWithAdvantage, Player otherPlayer) {
        playerWithAdvantage.setGamePoints(GAME_ADVANTAGE);
        otherPlayer.setGamePoints(SCORE_40_POINTS);
    }

    private boolean isDeuce(int firstPlayerPoints, int secondPlayerPoints) {
        return firstPlayerPoints >= MIN_POINTS_TO_WIN_A_GAME && firstPlayerPoints == secondPlayerPoints;
    }

    private void assignDeuceGamePoints(Player firstPlayer, Player secondPlayer) {
        firstPlayer.setGamePoints(SCORE_40_POINTS);
        secondPlayer.setGamePoints(SCORE_40_POINTS);
    }
}
