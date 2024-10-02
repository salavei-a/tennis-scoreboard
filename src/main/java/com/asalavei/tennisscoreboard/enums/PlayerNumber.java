package com.asalavei.tennisscoreboard.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PlayerNumber {
    FIRST_PLAYER(1), SECOND_PLAYER(2);

    private final int number;

    public static int opposite(int playerNumber) {
        return playerNumber == FIRST_PLAYER.number ?
                SECOND_PLAYER.number : FIRST_PLAYER.number;
    }
}