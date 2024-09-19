package com.asalavei.tennisscoreboard.controllers.mapper;

import com.asalavei.tennisscoreboard.controllers.dto.PlayerRequestDto;
import com.asalavei.tennisscoreboard.dto.Player;
import org.mapstruct.Mapper;

@Mapper
public interface PlayerDtoMapper {

    Player toDto(PlayerRequestDto requestDto);
}
