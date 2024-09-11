package com.asalavei.tennisscoreboard.services;

import com.asalavei.tennisscoreboard.dto.Match;
import com.asalavei.tennisscoreboard.dto.Player;

import java.util.Map;

public class MatchScoreCalculationService {

    private static final int MIN_POINTS_TO_WIN_A_GAME = 4;
    private static final int MIN_POINTS_TO_WIN_A_SET = 6;
    private static final int MIN_POINTS_TO_WIN_TIE_BREAK = 7;

    private static final Map<Integer, Integer> TENNIS_POINTS_MAPPING = Map.of(
            1, 15,
            2, 30,
            3, 40
    );

    public Match calculate(Match match, Integer pointWinnerId) {
        Player firstPlayer = match.getFirstPlayer();
        Player secondPlayer = match.getSecondPlayer();

        wonPoint(determinePointWinner(match, pointWinnerId));

        if (isGameFinished(match)) {
            wonGame(determinePointWinner(match, pointWinnerId));

            resetPoints(firstPlayer);
            resetPoints(secondPlayer);
        }

        if (isSetFinished(match)) {
            wonSet(determinePointWinner(match, pointWinnerId));

            resetGames(firstPlayer);
            resetGames(secondPlayer);

            resetPoints(firstPlayer);
            resetPoints(secondPlayer);
        }

        firstPlayer.setGamePoints(formatGamePoints(firstPlayer));
        secondPlayer.setGamePoints(formatGamePoints(secondPlayer));

        if (isMatchFinished(match)) {
            return Match.builder()
                    .firstPlayer(firstPlayer)
                    .secondPlayer(secondPlayer)
                    .winner(determinePointWinner(match, pointWinnerId))
                    .build();
        }

        return Match.builder()
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .build();
    }

    private Player determinePointWinner(Match match, Integer pointWinnerId) {
        if (pointWinnerId.equals(match.getFirstPlayer().getId())) {
            return match.getFirstPlayer();
        }

        if (pointWinnerId.equals(match.getSecondPlayer().getId())) {
            return match.getSecondPlayer();
        }

        throw new IllegalArgumentException("Invalid player ID: " + pointWinnerId);
    }

    private static void wonPoint(Player player) {
        player.setPoints(player.getPoints() + 1);
    }

    private static void wonGame(Player player) {
        player.setGames(player.getGames() + 1);
    }

    private static void wonSet(Player player) {
        player.setSets(player.getSets() + 1);
    }

    private static void resetPoints(Player player) {
        player.setPoints(0);
    }

    private static void resetGames(Player player) {
        player.setGames(0);
    }

    public static String formatGamePoints(Player player) {
        return String.valueOf(mapGamePoints(player.getPoints()));
    }

    public static int mapGamePoints(int gamePoints) {
        return TENNIS_POINTS_MAPPING.getOrDefault(gamePoints, gamePoints);
    }

    private static boolean isGameFinished(Match match) {
        int firstPlayerPoints = match.getFirstPlayer().getPoints();
        int secondPlayerPoints = match.getSecondPlayer().getPoints();

        if (isTieBreak(match)) {
            return Math.max(firstPlayerPoints, secondPlayerPoints) >= MIN_POINTS_TO_WIN_TIE_BREAK &&
                    Math.abs(firstPlayerPoints - secondPlayerPoints) > 1;
        }

        return Math.max(firstPlayerPoints, secondPlayerPoints) >= MIN_POINTS_TO_WIN_A_GAME &&
                Math.abs(firstPlayerPoints - secondPlayerPoints) > 1;
    }

    public static boolean isSetFinished(Match match) {
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

    private static boolean isTieBreak(Match match) {
        return match.getFirstPlayer().getGames() == 6 && match.getSecondPlayer().getGames() == 6;
    }

    public static boolean isMatchFinished(Match match) {
        return match.getFirstPlayer().getSets() == 2 || match.getSecondPlayer().getSets() == 2;
    }
}
