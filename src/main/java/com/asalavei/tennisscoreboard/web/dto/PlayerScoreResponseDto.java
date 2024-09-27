package com.asalavei.tennisscoreboard.web.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PlayerScoreResponseDto {

    int sets;

    int games;

    String gamePoints;
}
