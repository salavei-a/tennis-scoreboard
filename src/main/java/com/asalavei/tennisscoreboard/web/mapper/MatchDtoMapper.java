package com.asalavei.tennisscoreboard.web.mapper;

import com.asalavei.tennisscoreboard.dto.Match;
import com.asalavei.tennisscoreboard.web.dto.MatchResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface MatchDtoMapper {

    MatchResponseDto toResponseDto(Match match);

    List<MatchResponseDto> toResponseDto(List<Match> match);
}
