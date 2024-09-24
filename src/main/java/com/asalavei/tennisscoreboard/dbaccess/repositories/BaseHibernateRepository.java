package com.asalavei.tennisscoreboard.dbaccess.repositories;

import com.asalavei.tennisscoreboard.dbaccess.config.HibernateConfig;
import com.asalavei.tennisscoreboard.exceptions.DatabaseOperationException;
import jakarta.persistence.PersistenceException;
import lombok.extern.java.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.logging.Level;

@Log
public abstract class BaseHibernateRepository<T> implements CrudRepository<T> {

    protected static final SessionFactory sessionFactory = HibernateConfig.getSessionFactory();

    protected static final String ERROR_OCCURRED = "Error occurred while performing database operation";

    @Override
    public T save(T entity) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            session.persist(entity);

            transaction.commit();

            return entity;
        } catch (PersistenceException e) {
            rollbackTransaction(transaction);
            throw new DatabaseOperationException(ERROR_OCCURRED, e);
        } finally {
            closeSession(session);
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

    protected static void rollbackTransaction(Transaction transaction) {
        try {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception while rolling back transaction", e);
        }
    }
}
