package com.asalavei.tennisscoreboard.web.dto;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class MatchResponseDto {

    UUID uuid;

    PlayerResponseDto firstPlayer;

    PlayerResponseDto secondPlayer;

    PlayerResponseDto winner;
}
