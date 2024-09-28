package com.asalavei.tennisscoreboard.web.dto;

import com.asalavei.tennisscoreboard.validation.annotation.ValidName;
import com.asalavei.tennisscoreboard.validation.groups.Create;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PlayerRequestDto {

    @ValidName(groups = Create.class)
    @Size(groups = {Create.class, Default.class}, max = 50, message = "Player name is too long")
    @NotBlank(groups = {Create.class, Default.class}, message = "Player name is required")
    String name;
}
