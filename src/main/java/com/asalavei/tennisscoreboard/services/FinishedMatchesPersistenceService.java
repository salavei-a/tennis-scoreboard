package com.asalavei.tennisscoreboard.services;

import com.asalavei.tennisscoreboard.dbaccess.entities.MatchEntity;
import com.asalavei.tennisscoreboard.dbaccess.mapper.EntityMapper;
import com.asalavei.tennisscoreboard.dbaccess.mapper.MatchEntityMapper;
import com.asalavei.tennisscoreboard.dbaccess.repositories.HibernateMatchRepository;
import com.asalavei.tennisscoreboard.dbaccess.repositories.MatchRepository;
import com.asalavei.tennisscoreboard.dto.Match;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class FinishedMatchesPersistenceService {

    private final MatchRepository matchRepository = new HibernateMatchRepository();

    private final EntityMapper<Match, MatchEntity> mapper = Mappers.getMapper(MatchEntityMapper.class);

    public Match persist(Match match) {
        return mapper.toDto(matchRepository.save(mapper.toEntity(match)));
    }

    public List<Match> findAll(int pageNumber, int pageSize) {
        return mapper.toDto(matchRepository.findAll(pageNumber, pageSize));
    }

    public List<Match> findAllByPlayerName(String name, int pageNumber, int pageSize) {
        return mapper.toDto(matchRepository.findAllByPlayerName(name, pageNumber, pageSize));
    }

    public int countTotalPages(int pageSize) {
        return matchRepository.countTotalPages(pageSize);
    }

    public int countTotalPagesByPlayerName(String name, int pageSize) {
        return matchRepository.countTotalPagesByPlayerName(name, pageSize);
    }
}
