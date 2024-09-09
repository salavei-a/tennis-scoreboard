package com.asalavei.tennisscoreboard.dbaccess.mapper;

import com.asalavei.tennisscoreboard.dbaccess.entities.MatchEntity;
import com.asalavei.tennisscoreboard.dbaccess.entities.PlayerEntity;
import com.asalavei.tennisscoreboard.dto.Match;
import com.asalavei.tennisscoreboard.dto.Player;
import org.mapstruct.Mapper;

@Mapper
public interface EntityMapper {

    PlayerEntity toEntity(Player player);

    Player toDto(PlayerEntity player);

    MatchEntity toEntity(Match player);

    Match toDto(MatchEntity player);
}
