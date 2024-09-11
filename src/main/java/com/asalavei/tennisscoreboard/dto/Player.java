package com.asalavei.tennisscoreboard.dto;

import lombok.Data;
import lombok.Builder;


@Data
@Builder
public class Player {

    private final Integer id;

    private final String name;

    private String gamePoints;

    private int sets;

    private int games;

    private int points;
}
