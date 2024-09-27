package com.asalavei.tennisscoreboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerScore {

    private int sets;

    private int games;

    private int points;

    private String gamePoints;
}
