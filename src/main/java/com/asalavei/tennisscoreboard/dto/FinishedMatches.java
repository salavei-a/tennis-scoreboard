package com.asalavei.tennisscoreboard.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class FinishedMatches {

    List<Match> matches;

    String playerName;

    int pageNumber;

    int totalPage;
}
