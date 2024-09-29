package com.asalavei.tennisscoreboard.web.mapper;

import com.asalavei.tennisscoreboard.dto.Match;
import com.asalavei.tennisscoreboard.dto.PlayerScore;
import com.asalavei.tennisscoreboard.web.dto.MatchRequestDto;
import com.asalavei.tennisscoreboard.web.dto.MatchResponseDto;
import com.asalavei.tennisscoreboard.web.dto.PlayerScoreResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface MatchDtoMapper {

    Match toDto(MatchRequestDto matchRequestDto);

    MatchResponseDto toResponseDto(Match match);

    List<MatchResponseDto> toResponseDto(List<Match> match);

    @Mapping(target = "points", source = "displayPoints")
    PlayerScoreResponseDto toResponseDto(PlayerScore playerScore);
}
