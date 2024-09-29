package com.asalavei.tennisscoreboard.dbaccess.mapper;

import com.asalavei.tennisscoreboard.dbaccess.entities.MatchEntity;
import com.asalavei.tennisscoreboard.dto.Match;
import org.mapstruct.Mapper;

@Mapper
public interface MatchEntityMapper extends EntityMapper<Match, MatchEntity> {
}
