package com.asalavei.tennisscoreboard.dbaccess.repositories;

import com.asalavei.tennisscoreboard.dbaccess.entities.PlayerEntity;

import java.util.Optional;

public interface PlayerRepository extends CrudRepository<PlayerEntity> {
    Optional<PlayerEntity> findByName(String name);
}
