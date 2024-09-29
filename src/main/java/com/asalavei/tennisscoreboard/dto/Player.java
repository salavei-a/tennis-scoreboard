package com.asalavei.tennisscoreboard.dto;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;


@Value
@Builder
public class Player {

    UUID uuid;

    Integer id;

    String name;

    PlayerScore playerScore;
}
