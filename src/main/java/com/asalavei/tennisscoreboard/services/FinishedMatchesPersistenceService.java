package com.asalavei.tennisscoreboard.services;

import com.asalavei.tennisscoreboard.dbaccess.mapper.MatchEntityMapper;
import com.asalavei.tennisscoreboard.dbaccess.repositories.MatchHibernateRepository;
import com.asalavei.tennisscoreboard.dbaccess.repositories.MatchRepository;
import com.asalavei.tennisscoreboard.dto.FinishedMatches;
import com.asalavei.tennisscoreboard.dto.Match;
import org.mapstruct.factory.Mappers;

public class FinishedMatchesPersistenceService {

    private final MatchRepository matchRepository = new MatchHibernateRepository();

    private final MatchEntityMapper mapper = Mappers.getMapper(MatchEntityMapper.class);

    public Match persist(Match match) {
        return mapper.toDto(matchRepository.save(mapper.toEntity(match)));
    }

    public FinishedMatches findAllFinishedMatches(int pageNumber, int pageSize) {
        return FinishedMatches.builder()
                .matches(mapper.toDto(matchRepository.findAll(pageNumber, pageSize)))
                .totalPage(matchRepository.countTotalPages(pageSize))
                .pageNumber(pageNumber)
                .build();

    }

    public FinishedMatches findAllFinishedMatchesByPlayer(String name, int pageNumber, int pageSize) {
        return FinishedMatches.builder()
                .matches(mapper.toDto(matchRepository.findAllByPlayerName(name, pageNumber, pageSize)))
                .totalPage(matchRepository.countTotalPagesByPlayerName(name, pageSize))
                .pageNumber(pageNumber)
                .playerName(name)
                .build();
    }
}
