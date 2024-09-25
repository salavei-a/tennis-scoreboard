package com.asalavei.tennisscoreboard.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PlayerRequestDto {

    @Size(max = 50, message = "Player name is too long")
    @NotBlank(message = "Player name is required")
    String name;
}
