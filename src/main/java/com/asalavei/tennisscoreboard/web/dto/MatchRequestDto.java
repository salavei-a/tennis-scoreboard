package com.asalavei.tennisscoreboard.web.dto;

import com.asalavei.tennisscoreboard.validation.scenario.Create;
import com.asalavei.tennisscoreboard.validation.scenario.Find;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import java.util.UUID;

@Value
@Builder
public class MatchRequestDto {

    @NotNull(groups = Find.class)
    UUID uuid;

    @NotNull(groups = Create.class)
    PlayerRequestDto firstPlayer;

    @NotNull(groups = Create.class)
    PlayerRequestDto secondPlayer;

    PlayerRequestDto playerWinnerPoint;
}
