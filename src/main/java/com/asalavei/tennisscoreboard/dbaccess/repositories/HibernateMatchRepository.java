package com.asalavei.tennisscoreboard.dbaccess.repositories;

import com.asalavei.tennisscoreboard.dbaccess.HibernateUtil;
import com.asalavei.tennisscoreboard.dbaccess.entities.MatchEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class HibernateMatchRepository implements MatchRepository {

    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public MatchEntity save(MatchEntity match) {
        Transaction transaction = null;

        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();

            session.persist(match);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // TODO: handle custom exception
        }

        return match;
    }
}
