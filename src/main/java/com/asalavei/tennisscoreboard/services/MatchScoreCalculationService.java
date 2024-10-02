package com.asalavei.tennisscoreboard.services;

import com.asalavei.tennisscoreboard.enums.GameScore;
import com.asalavei.tennisscoreboard.dto.Match;
import com.asalavei.tennisscoreboard.dto.Player;
import com.asalavei.tennisscoreboard.dto.PlayerScore;
import com.asalavei.tennisscoreboard.enums.PlayerNumber;

public class MatchScoreCalculationService {

    private static final int MIN_POINTS_TO_WIN_TIEBREAK = 7;
    private static final int MIN_GAMES_TO_WIN_A_SET = 6;
    private static final int MIN_SETS_TO_WIN_A_MATCH = 2;
    private static final int MIN_GAME_DIFFERENCE = 1;
    private static final int MIN_TIEBREAK_POINT_DIFFERENCE = 1;
    private static final int SIX_GAMES = 6;

    public Match calculate(Match match, int pointWinnerNumber) {
        Player firstPlayer = match.getFirstPlayer();
        Player secondPlayer = match.getSecondPlayer();

        PlayerScore firstPlayerScore = firstPlayer.getPlayerScore();
        PlayerScore secondPlayerScore = secondPlayer.getPlayerScore();

        Player pointWinner = determinePlayer(match, pointWinnerNumber);

        PlayerScore pointWinnerScore = pointWinner.getPlayerScore();
        PlayerScore opponentScore = determinePlayer(match, PlayerNumber.opposite(pointWinnerNumber)).getPlayerScore();

        boolean isTiebreak = isTiebreak(firstPlayerScore, secondPlayerScore);

        wonPoint(pointWinnerScore, opponentScore, isTiebreak);

        if (isGameFinished(firstPlayerScore, secondPlayerScore, isTiebreak)) {
            wonGame(pointWinnerScore);
            resetPoints(firstPlayerScore, secondPlayerScore);

            if (isSetFinished(firstPlayerScore, secondPlayerScore)) {
                wonSet(pointWinnerScore);
                resetGames(firstPlayerScore, secondPlayerScore);
            }

            if (isMatchFinished(firstPlayerScore, secondPlayerScore)) {
                return Match.builder()
                        .uuid(match.getUuid())
                        .firstPlayer(firstPlayer)
                        .secondPlayer(secondPlayer)
                        .winner(pointWinner)
                        .build();
            }
        }

        return Match.builder()
                .uuid(match.getUuid())
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .build();
    }

    private Player determinePlayer(Match match, int playerNumber) {
        if (PlayerNumber.FIRST_PLAYER.getNumber() == playerNumber) {
            return match.getFirstPlayer();
        }

        if (PlayerNumber.SECOND_PLAYER.getNumber() == playerNumber) {
            return match.getSecondPlayer();
        }

        throw new IllegalArgumentException("Invalid player number: " + playerNumber);
    }

    private void wonPoint(PlayerScore pointWinnerScore, PlayerScore opponentScore, boolean isTiebreak) {
        if (isTiebreak) {
            if (pointWinnerScore.getTiebreakPoints() == null && opponentScore.getTiebreakPoints() == null) {
                pointWinnerScore.setTiebreakPoints(0);
                opponentScore.setTiebreakPoints(0);
            }

            wonTiebreakPoint(pointWinnerScore);
            return;
        }

        GameScore pointWinnerPoints = pointWinnerScore.getGameScore();
        GameScore opponentPoints = opponentScore.getGameScore();

        if (isForty(pointWinnerPoints)) {
            if (isAdvantage(opponentPoints)) {
                opponentScore.setGameScore(GameScore.FORTY);
                return;
            }

            if (isForty(opponentPoints)) {
                pointWinnerScore.setGameScore(GameScore.ADVANTAGE);
                return;
            }

            pointWinnerScore.setGameScore(GameScore.GAME_WON);
            return;
        }

        if (isAdvantage(pointWinnerPoints)) {
            pointWinnerScore.setGameScore(GameScore.GAME_WON);
            return;
        }

        pointWinnerScore.setGameScore(GameScore.next(pointWinnerPoints));
    }

    private void wonTiebreakPoint(PlayerScore pointWinnerScore) {
        pointWinnerScore.setTiebreakPoints(pointWinnerScore.getTiebreakPoints() + 1);
    }

    private boolean isForty(GameScore gameScore) {
        return gameScore == GameScore.FORTY;
    }

    private boolean isAdvantage(GameScore gameScore) {
        return gameScore == GameScore.ADVANTAGE;
    }

    private void wonGame(PlayerScore playerScore) {
        playerScore.setGames(playerScore.getGames() + 1);
    }

    private void wonSet(PlayerScore playerScore) {
        playerScore.setSets(playerScore.getSets() + 1);
    }

    private boolean isGameFinished(PlayerScore firstPlayerScore, PlayerScore secondPlayerScore, boolean isTiebreak) {
        if (isTiebreak) {
            Integer firstPlayerPoints = firstPlayerScore.getTiebreakPoints();
            Integer secondPlayerPoints = secondPlayerScore.getTiebreakPoints();

            return Math.max(firstPlayerPoints, secondPlayerPoints) >= MIN_POINTS_TO_WIN_TIEBREAK &&
                    Math.abs(firstPlayerPoints - secondPlayerPoints) > MIN_TIEBREAK_POINT_DIFFERENCE;
        }

        return GameScore.GAME_WON == firstPlayerScore.getGameScore() ||
                GameScore.GAME_WON == secondPlayerScore.getGameScore();
    }

    private boolean isSetFinished(PlayerScore firstPlayerScore, PlayerScore secondPlayerScore) {
        int firstPlayerGames = firstPlayerScore.getGames();
        int secondPlayerGames = secondPlayerScore.getGames();

        if (Math.max(firstPlayerGames, secondPlayerGames) >= MIN_GAMES_TO_WIN_A_SET &&
                Math.abs(firstPlayerGames - secondPlayerGames) > MIN_GAME_DIFFERENCE) {
            return true;
        }

        if (Math.max(firstPlayerGames, secondPlayerGames) > MIN_GAMES_TO_WIN_A_SET &&
                Math.min(firstPlayerGames, secondPlayerGames) == MIN_GAMES_TO_WIN_A_SET) {
            return true;
        }

        return false;
    }

    private boolean isMatchFinished(PlayerScore firstPlayerScore, PlayerScore secondPlayerScore) {
        return firstPlayerScore.getSets() == MIN_SETS_TO_WIN_A_MATCH ||
                secondPlayerScore.getSets() == MIN_SETS_TO_WIN_A_MATCH;
    }

    private void resetPoints(PlayerScore firstPlayerScore, PlayerScore secondPlayerScore) {
        firstPlayerScore.setGameScore(GameScore.LOVE);
        secondPlayerScore.setGameScore(GameScore.LOVE);

        if (isTiebreak(firstPlayerScore, secondPlayerScore)) {
            firstPlayerScore.setTiebreakPoints(0);
            secondPlayerScore.setTiebreakPoints(0);
        } else {
            firstPlayerScore.setTiebreakPoints(null);
            secondPlayerScore.setTiebreakPoints(null);
        }
    }

    private boolean isTiebreak(PlayerScore firstPlayerScore, PlayerScore secondPlayerScore) {
        return firstPlayerScore.getGames() == SIX_GAMES && secondPlayerScore.getGames() == SIX_GAMES;
    }

    private void resetGames(PlayerScore firstPlayerScore, PlayerScore secondPlayerScore) {
        firstPlayerScore.setGames(0);
        secondPlayerScore.setGames(0);
    }
}