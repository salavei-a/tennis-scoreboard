package com.asalavei.tennisscoreboard.dto;

import lombok.Value;
import lombok.Builder;

@Value
@Builder
public class Player {

    Integer id;

    String name;
}
