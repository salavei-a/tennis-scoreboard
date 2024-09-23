package com.asalavei.tennisscoreboard.web.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PlayerResponseDto {

    Integer id;

    String name;

    int sets;

    int games;

    String gamePoints;
}
