package com.asalavei.tennisscoreboard.dbaccess.repositories;

import com.asalavei.tennisscoreboard.dbaccess.entities.PlayerEntity;

import java.util.Optional;

public class PlayerHibernateRepository extends BaseHibernateRepository<PlayerEntity> implements PlayerRepository {

    @Override
    public Optional<PlayerEntity> findByName(String name) {
        return executeInTransaction(session -> {
            PlayerEntity playerEntity = session.createQuery("from PlayerEntity where name = :name", PlayerEntity.class)
                    .setParameter("name", name)
                    .uniqueResult();
            return Optional.ofNullable(playerEntity);
        });
    }
}
