package com.asalavei.tennisscoreboard.dbaccess.repositories;

import com.asalavei.tennisscoreboard.dbaccess.entities.PlayerEntity;
import com.asalavei.tennisscoreboard.exceptions.DatabaseOperationException;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

public class PlayerHibernateRepository extends BaseHibernateRepository<PlayerEntity> implements PlayerRepository {

    @Override
    public Optional<PlayerEntity> findByName(String name) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            PlayerEntity playerEntity = session.createQuery("from PlayerEntity where name = :name", PlayerEntity.class)
                    .setParameter("name", name)
                    .uniqueResult();

            transaction.commit();

            return Optional.ofNullable(playerEntity);
        } catch (PersistenceException e) {
            rollbackTransaction(transaction);
            throw new DatabaseOperationException(ERROR_OCCURRED, e);
        } finally {
            closeSession(session);
        }
    }
}
