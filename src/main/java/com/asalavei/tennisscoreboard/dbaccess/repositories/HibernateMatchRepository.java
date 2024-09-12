package com.asalavei.tennisscoreboard.dbaccess.repositories;

import com.asalavei.tennisscoreboard.dbaccess.HibernateUtil;
import com.asalavei.tennisscoreboard.dbaccess.entities.MatchEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateMatchRepository implements MatchRepository {

    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public MatchEntity save(MatchEntity match) {
        Transaction transaction = null;

        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();

            session.persist(match);

            transaction.commit();

            return match;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException(e); // TODO: handle custom exception
        }
    }

    @Override
    public List<MatchEntity> findAll() {
        Transaction transaction = null;

        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();

            List<MatchEntity> matches = session.createQuery(
                            "from MatchEntity m join fetch m.firstPlayer join fetch m.secondPlayer", MatchEntity.class
                    )
                    .list();

            transaction.commit();

            return matches;
        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException(e); // TODO: handle custom exception
        }
    }

    @Override
    public List<MatchEntity> findAllByPlayerName(String name) {
        Transaction transaction = null;

        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();

            List<MatchEntity> matches = session.createQuery(
                            "from MatchEntity m join fetch m.firstPlayer join fetch m.secondPlayer " +
                               "where m.firstPlayer.name = :name or m.secondPlayer.name = :name", MatchEntity.class
                    )
                    .setParameter("name", name)
                    .list();

            transaction.commit();

            return matches;
        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException(e); // TODO: handle custom exception
        }
    }
}
