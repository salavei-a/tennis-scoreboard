package com.asalavei.tennisscoreboard.dto;

import lombok.Value;
import lombok.Builder;

@Value
@Builder
public class Match {

    Integer id;

    Player firstPlayer;

    Player secondPlayer;

    Player winner;
}
