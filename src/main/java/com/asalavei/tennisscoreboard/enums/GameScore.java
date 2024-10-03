package com.asalavei.tennisscoreboard.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GameScore {
    LOVE(0, "0"),
    FIFTEEN(1, "15"),
    THIRTY(2, "30"),
    FORTY(3, "40"),
    ADVANTAGE(4, "AD"),
    GAME_WON(5, "Game Won");

    private final int value;
    private final String display;

    public static GameScore next(GameScore gameScore) {
        return gameScore.ordinal() < GameScore.ADVANTAGE.ordinal() ?
                GameScore.values()[gameScore.ordinal() + 1] : gameScore;
    }
}
