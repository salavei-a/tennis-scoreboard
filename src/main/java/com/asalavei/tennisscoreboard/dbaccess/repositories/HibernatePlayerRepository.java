package com.asalavei.tennisscoreboard.dbaccess.repositories;

import com.asalavei.tennisscoreboard.dbaccess.config.HibernateUtil;
import com.asalavei.tennisscoreboard.dbaccess.entities.PlayerEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;

public class HibernatePlayerRepository implements PlayerRepository {

    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

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

            throw new RuntimeException(e); // TODO: handle custom exception
        }
    }

    @Override
    public Optional<PlayerEntity> findByName(String name) {
        Transaction transaction = null;

        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();

            PlayerEntity playerEntity = session.createQuery("FROM PlayerEntity WHERE name = :name", PlayerEntity.class)
                    .setParameter("name", name)
                    .uniqueResult();

            transaction.commit();

            return Optional.ofNullable(playerEntity);
        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException(e); // TODO: handle custom exception
        }
    }
}
