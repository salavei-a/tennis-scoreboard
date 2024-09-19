package com.asalavei.tennisscoreboard.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PlayerRequestDto {

    @NotBlank
    Integer id;

    @Size(max = 50)
    @NotBlank
    String name;
}
