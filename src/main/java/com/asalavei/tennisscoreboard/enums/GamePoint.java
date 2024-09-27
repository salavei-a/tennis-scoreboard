package com.asalavei.tennisscoreboard.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GamePoint {
    LOVE(0, "0"),
    FIFTEEN(1, "15"),
    THIRTY(2, "30"),
    FORTY(3, "40"),
    ADVANTAGE(4, "AD");

    private final int internal;
    private final String display;

    /**
     * Converts the internal game point (used in business logic for calculation) to its display
     * representation according to tennis rules for regular play (not tie-break stage).
     *
     * @param internal the business logic game point
     * @return the display representation of the game point
     * @throws IllegalArgumentException if the internal point is not valid for regular play
     */
    public static String fromInternal(int internal) {
        for (GamePoint gamePoint : values()) {
            if (gamePoint.internal == internal) {
                return gamePoint.getDisplay();
            }
        }

        throw new IllegalArgumentException("Invalid internal game point for regular play: " + internal);
    }
}
