package com.asalavei.tennisscoreboard.web.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PlayerResponseDto {

    String name;

    PlayerScoreResponseDto playerScore;
}
