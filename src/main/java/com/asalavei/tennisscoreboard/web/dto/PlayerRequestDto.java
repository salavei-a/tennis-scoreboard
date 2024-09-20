package com.asalavei.tennisscoreboard.web.dto;

import com.asalavei.tennisscoreboard.validation.scenario.Create;
import com.asalavei.tennisscoreboard.validation.scenario.FindById;
import com.asalavei.tennisscoreboard.validation.scenario.FindByName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PlayerRequestDto {

    @NotNull(groups = FindById.class, message = "Player ID is required")
    Integer id;

    @Size(max = 50, groups = {Create.class, FindByName.class}, message = "Player name is too long")
    @NotBlank(groups = {Create.class, FindByName.class}, message = "Player name is required")
    String name;
}
