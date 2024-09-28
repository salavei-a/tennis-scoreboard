package com.asalavei.tennisscoreboard.dbaccess.repositories;

import com.asalavei.tennisscoreboard.dbaccess.config.HibernateConfig;
import com.asalavei.tennisscoreboard.exceptions.DatabaseOperationException;
import jakarta.persistence.PersistenceException;
import lombok.extern.java.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.function.Function;
import java.util.logging.Level;

@Log
public abstract class BaseHibernateRepository<T> implements CrudRepository<T> {

    protected static final SessionFactory sessionFactory = HibernateConfig.getSessionFactory();

    @Override
    public T save(T entity) {
        return executeInTransaction(session -> {
            session.persist(entity);
            return entity;
        });
    }

    protected <R> R executeInTransaction(Function<Session, R> action) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            R result = action.apply(session);

            transaction.commit();

            return result;
        } catch (PersistenceException e) {
            rollbackTransaction(transaction);
            throw new DatabaseOperationException("Error occurred while performing database operation", e);
        } finally {
            closeSession(session);
        }
    }

    protected static void rollbackTransaction(Transaction transaction) {
        try {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception while rolling back transaction", e);
        }
    }

    protected static void closeSession(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                log.log(Level.SEVERE, "Exception while closing session", e);
            }
        }
    }
}
