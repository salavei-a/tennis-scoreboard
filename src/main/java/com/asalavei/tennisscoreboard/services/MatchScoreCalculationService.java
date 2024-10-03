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
        Player pointWinner = determinePlayer(match, pointWinnerNumber);

        PlayerScore pointWinnerScore = pointWinner.getPlayerScore();
        PlayerScore opponentScore = determinePlayer(match, PlayerNumber.opposite(pointWinnerNumber)).getPlayerScore();

        wonPoint(pointWinnerScore, opponentScore);

        if (isGameFinished(pointWinnerScore, opponentScore)) {
            wonGame(pointWinnerScore);
            resetPoints(pointWinnerScore, opponentScore);

            if (isSetFinished(pointWinnerScore, opponentScore)) {
                wonSet(pointWinnerScore);
                resetGames(pointWinnerScore, opponentScore);
            }

            if (isMatchFinished(pointWinnerScore, opponentScore)) {
                match.setWinner(pointWinner);
            }
        }

        return match;
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

    private void wonPoint(PlayerScore pointWinnerScore, PlayerScore opponentScore) {
        if (isTiebreak(pointWinnerScore, opponentScore)) {
            ensureTiebreakPointsInitialized(pointWinnerScore, opponentScore);
            wonTiebreakPoint(pointWinnerScore);
            return;
        }

        GameScore pointWinnerGameScore = pointWinnerScore.getGameScore();

        if (isForty(pointWinnerGameScore)) {
            handleFortyPointWinner(pointWinnerScore, opponentScore, opponentScore.getGameScore());
            return;
        }

        if (isAdvantage(pointWinnerGameScore)) {
            pointWinnerScore.setGameScore(GameScore.GAME_WON);
            return;
        }

        pointWinnerScore.setGameScore(GameScore.next(pointWinnerGameScore));
    }

    private static void ensureTiebreakPointsInitialized(PlayerScore pointWinnerScore, PlayerScore opponentScore) {
        if (pointWinnerScore.getTiebreakPoints() == null && opponentScore.getTiebreakPoints() == null) {
            pointWinnerScore.setTiebreakPoints(0);
            opponentScore.setTiebreakPoints(0);
        }
    }

    private void wonTiebreakPoint(PlayerScore pointWinnerScore) {
        pointWinnerScore.setTiebreakPoints(pointWinnerScore.getTiebreakPoints() + 1);
    }

    private void handleFortyPointWinner(PlayerScore pointWinnerScore, PlayerScore opponentScore, GameScore opponentGameScore) {
        if (isAdvantage(opponentGameScore)) {
            opponentScore.setGameScore(GameScore.FORTY);
            return;
        }

        if (isForty(opponentGameScore)) {
            pointWinnerScore.setGameScore(GameScore.ADVANTAGE);
            return;
        }

        pointWinnerScore.setGameScore(GameScore.GAME_WON);
    }

    private boolean isForty(GameScore gameScore) {
        return gameScore == GameScore.FORTY;
    }

    private boolean isAdvantage(GameScore gameScore) {
        return gameScore == GameScore.ADVANTAGE;
    }

    private void wonGame(PlayerScore pointWinnerScore) {
        pointWinnerScore.setGames(pointWinnerScore.getGames() + 1);
    }

    private void wonSet(PlayerScore pointWinnerScore) {
        pointWinnerScore.setSets(pointWinnerScore.getSets() + 1);
    }

    private boolean isGameFinished(PlayerScore pointWinnerScore, PlayerScore opponentScore) {
        if (isTiebreak(pointWinnerScore, opponentScore)) {
            Integer pointWinnerTiebreakPoints = pointWinnerScore.getTiebreakPoints();
            Integer opponentTiebreakPoints = opponentScore.getTiebreakPoints();

            return Math.max(pointWinnerTiebreakPoints, opponentTiebreakPoints) >= MIN_POINTS_TO_WIN_TIEBREAK &&
                    Math.abs(pointWinnerTiebreakPoints - opponentTiebreakPoints) > MIN_TIEBREAK_POINT_DIFFERENCE;
        }

        return GameScore.GAME_WON == pointWinnerScore.getGameScore() ||
                GameScore.GAME_WON == opponentScore.getGameScore();
    }

    private boolean isSetFinished(PlayerScore pointWinnerScore, PlayerScore opponentScore) {
        int pointWinnerGames = pointWinnerScore.getGames();
        int opponentGames = opponentScore.getGames();

        if (Math.max(pointWinnerGames, opponentGames) >= MIN_GAMES_TO_WIN_A_SET &&
                Math.abs(pointWinnerGames - opponentGames) > MIN_GAME_DIFFERENCE) {
            return true;
        }

        if (Math.max(pointWinnerGames, opponentGames) > MIN_GAMES_TO_WIN_A_SET &&
                Math.min(pointWinnerGames, opponentGames) == MIN_GAMES_TO_WIN_A_SET) {
            return true;
        }

        return false;
    }

    private boolean isMatchFinished(PlayerScore pointWinnerScore, PlayerScore opponentScore) {
        return pointWinnerScore.getSets() == MIN_SETS_TO_WIN_A_MATCH ||
                opponentScore.getSets() == MIN_SETS_TO_WIN_A_MATCH;
    }

    private void resetPoints(PlayerScore pointWinnerScore, PlayerScore opponentScore) {
        pointWinnerScore.setGameScore(GameScore.LOVE);
        opponentScore.setGameScore(GameScore.LOVE);

        if (isTiebreak(pointWinnerScore, opponentScore)) {
            pointWinnerScore.setTiebreakPoints(0);
            opponentScore.setTiebreakPoints(0);
        } else {
            pointWinnerScore.setTiebreakPoints(null);
            opponentScore.setTiebreakPoints(null);
        }
    }

    private void resetGames(PlayerScore pointWinnerScore, PlayerScore opponentScore) {
        pointWinnerScore.setGames(0);
        opponentScore.setGames(0);
    }

    private boolean isTiebreak(PlayerScore pointWinnerScore, PlayerScore opponentScore) {
        return pointWinnerScore.getGames() == SIX_GAMES && opponentScore.getGames() == SIX_GAMES;
    }
}