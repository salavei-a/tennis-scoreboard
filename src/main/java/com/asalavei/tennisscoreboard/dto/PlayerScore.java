package com.asalavei.tennisscoreboard.dto;

import com.asalavei.tennisscoreboard.enums.GameScore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerScore {

    private int sets;

    private int games;

    private GameScore gameScore;

    private Integer tiebreakPoints;
}
