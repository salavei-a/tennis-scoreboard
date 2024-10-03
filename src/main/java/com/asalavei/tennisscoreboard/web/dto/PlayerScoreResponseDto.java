package com.asalavei.tennisscoreboard.web.dto;

import com.asalavei.tennisscoreboard.enums.GameScore;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PlayerScoreResponseDto {

    int sets;

    int games;

    GameScore gameScore;

    Integer tiebreakPoints;
}
