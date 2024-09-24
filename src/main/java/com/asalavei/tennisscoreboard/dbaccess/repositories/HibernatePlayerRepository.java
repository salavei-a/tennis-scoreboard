package com.asalavei.tennisscoreboard.dbaccess.repositories;

import com.asalavei.tennisscoreboard.dbaccess.config.HibernateConfig;
import com.asalavei.tennisscoreboard.dbaccess.entities.PlayerEntity;
import com.asalavei.tennisscoreboard.exceptions.DatabaseOperationException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;

public class HibernatePlayerRepository implements PlayerRepository {

    private static final SessionFactory sessionFactory = HibernateConfig.getSessionFactory();

    private static final String ERROR_OCCURRED = "Error occurred while performing database operation";

    @Override
    public PlayerEntity save(PlayerEntity entity) {
        Transaction transaction = null;

        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();

            session.persist(entity);

            transaction.commit();

            return entity;
        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new DatabaseOperationException(ERROR_OCCURRED, e);
        }
    }

    @Override
    public Optional<PlayerEntity> findByName(String name) {
        Transaction transaction = null;

        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();

            PlayerEntity playerEntity = session.createQuery("from PlayerEntity where name = :name", PlayerEntity.class)
                    .setParameter("name", name)
                    .uniqueResult();

            transaction.commit();

            return Optional.ofNullable(playerEntity);
        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new DatabaseOperationException(ERROR_OCCURRED, e);
        }
    }
}
