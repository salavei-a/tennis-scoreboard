package com.asalavei.tennisscoreboard.web.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class FinishedMatchesResponseDto {

    List<MatchResponseDto> matches;

    String playerName;

    int pageNumber;

    int totalPage;
}
