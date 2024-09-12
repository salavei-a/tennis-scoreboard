package com.asalavei.tennisscoreboard.dbaccess.repositories;

import com.asalavei.tennisscoreboard.dbaccess.entities.MatchEntity;

import java.util.List;

public interface MatchRepository extends CrudRepository<MatchEntity> {
    List<MatchEntity> findAll(int pageNumber, int pageSize);

    List<MatchEntity> findAllByPlayerName(String name, int pageNumber, int pageSize);

    int countTotalPages(int pageSize);

    int countTotalPagesByPlayerName(String name, int pageSize);
}
