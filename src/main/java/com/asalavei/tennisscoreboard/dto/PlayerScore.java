package com.asalavei.tennisscoreboard.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO representing the score of a player, including internal (business logic) points
 * for calculation and display points according to tennis rules.
 */
@Data
@Builder
public class PlayerScore {

    private int sets;

    private int games;

    private int internalPoints;

    private String displayPoints;
}
