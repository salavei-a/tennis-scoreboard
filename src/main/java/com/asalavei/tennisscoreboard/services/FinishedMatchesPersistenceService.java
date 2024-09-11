package com.asalavei.tennisscoreboard.services;

import com.asalavei.tennisscoreboard.dbaccess.entities.MatchEntity;
import com.asalavei.tennisscoreboard.dbaccess.mapper.EntityMapper;
import com.asalavei.tennisscoreboard.dbaccess.mapper.MatchEntityMapper;
import com.asalavei.tennisscoreboard.dbaccess.repositories.HibernateMatchRepository;
import com.asalavei.tennisscoreboard.dbaccess.repositories.MatchRepository;
import com.asalavei.tennisscoreboard.dto.Match;
import org.mapstruct.factory.Mappers;

public class FinishedMatchesPersistenceService {

    private final MatchRepository matchRepository = new HibernateMatchRepository();

    private final EntityMapper<Match, MatchEntity> mapper = Mappers.getMapper(MatchEntityMapper.class);

    public Match persist(Match match) {
        return mapper.toDto(matchRepository.save(mapper.toEntity(match)));
    }
}
