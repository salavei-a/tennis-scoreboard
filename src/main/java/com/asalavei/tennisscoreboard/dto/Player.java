package com.asalavei.tennisscoreboard.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Player {

    Integer id;

    String name;

    PlayerScore playerScore;
}
