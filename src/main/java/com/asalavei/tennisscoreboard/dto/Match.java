package com.asalavei.tennisscoreboard.dto;

import lombok.Data;
import lombok.Builder;

import java.util.UUID;

@Data
@Builder
public class Match {

    private final UUID uuid;

    private final Integer id;

    private final Player firstPlayer;

    private final Player secondPlayer;

    private Player winner;
}
