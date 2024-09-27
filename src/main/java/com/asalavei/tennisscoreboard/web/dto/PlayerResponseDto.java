package com.asalavei.tennisscoreboard.web.dto;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class PlayerResponseDto {

    UUID uuid;

    String name;

    int sets;

    int games;

    String gamePoints;
}
