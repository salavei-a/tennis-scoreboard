package com.asalavei.tennisscoreboard.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MatchRequestDto {

    @NotNull
    PlayerRequestDto firstPlayer;

    @NotNull
    PlayerRequestDto secondPlayer;
}
