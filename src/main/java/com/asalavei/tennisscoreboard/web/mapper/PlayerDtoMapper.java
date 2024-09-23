package com.asalavei.tennisscoreboard.web.mapper;

import com.asalavei.tennisscoreboard.web.dto.PlayerRequestDto;
import com.asalavei.tennisscoreboard.dto.Player;
import org.mapstruct.Mapper;

@Mapper
public interface PlayerDtoMapper {

    Player toDto(PlayerRequestDto playerRequestDto);
}
