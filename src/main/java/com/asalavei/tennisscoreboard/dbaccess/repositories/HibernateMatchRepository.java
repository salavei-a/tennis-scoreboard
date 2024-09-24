package com.asalavei.tennisscoreboard.dbaccess.repositories;

import com.asalavei.tennisscoreboard.dbaccess.config.HibernateConfig;
import com.asalavei.tennisscoreboard.dbaccess.entities.MatchEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateMatchRepository implements MatchRepository {

    private static final SessionFactory sessionFactory = HibernateConfig.getSessionFactory();

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
    public List<MatchEntity> findAll(int pageNumber, int pageSize) {
        Transaction transaction = null;

        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();

            String query = "from MatchEntity m join fetch m.firstPlayer join fetch m.secondPlayer order by m.id desc";

            List<MatchEntity> matches = session.createQuery(query, MatchEntity.class)
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize)
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
    public List<MatchEntity> findAllByPlayerName(String name, int pageNumber, int pageSize) {
        Transaction transaction = null;

        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();

            String query = "from MatchEntity m join fetch m.firstPlayer fp join fetch m.secondPlayer sp " +
                           "where fp.name = :name or sp.name = :name order by m.id desc";

            List<MatchEntity> matches = session.createQuery(query, MatchEntity.class)
                    .setParameter("name", name)
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize)
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
    public int countTotalPages(int pageSize) {
        Transaction transaction = null;

        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();

            int totalPages;

            Long countResults = session.createQuery("select count (m.id) from MatchEntity m", Long.class)
                    .uniqueResult();

            totalPages = (int) Math.ceil((double) countResults / pageSize);

            transaction.commit();

            return totalPages;
        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException(e); // TODO: handle custom exception
        }
    }

    @Override
    public int countTotalPagesByPlayerName(String name, int pageSize) {
        Transaction transaction = null;

        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();

            int totalPages;

            String query = "select count (m.id) from MatchEntity m " +
                           "join m.firstPlayer fp join m.secondPlayer sp " +
                           "where fp.name = :name or sp.name = :name";

            Long countResults = session.createQuery(query, Long.class)
                    .setParameter("name", name)
                    .uniqueResult();

            totalPages = (int) Math.ceil((double) countResults / pageSize);

            transaction.commit();

            return totalPages;
        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException(e); // TODO: handle custom exception
        }
    }
}
