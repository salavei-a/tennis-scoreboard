package com.asalavei.tennisscoreboard.web.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MatchResponseDto {

    PlayerResponseDto firstPlayer;

    PlayerResponseDto secondPlayer;

    PlayerResponseDto winner;
}
