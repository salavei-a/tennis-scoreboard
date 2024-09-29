package com.asalavei.tennisscoreboard.dto;

import lombok.Value;
import lombok.Builder;

import java.util.UUID;

@Value
@Builder
public class Match {

    UUID uuid;

    Integer id;

    Player firstPlayer;

    Player secondPlayer;

    Player winner;
}
