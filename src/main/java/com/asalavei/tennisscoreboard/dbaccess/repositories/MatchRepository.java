package com.asalavei.tennisscoreboard.dbaccess.repositories;

import com.asalavei.tennisscoreboard.dbaccess.entities.MatchEntity;

import java.util.List;

public interface MatchRepository extends CrudRepository<MatchEntity> {
    List<MatchEntity> findAll();

    List<MatchEntity> findAllByPlayerName(String name);
}
