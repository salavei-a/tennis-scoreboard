package com.asalavei.tennisscoreboard.dbaccess.repositories;

import com.asalavei.tennisscoreboard.dbaccess.HibernateUtil;
import com.asalavei.tennisscoreboard.dbaccess.entities.PlayerEntity;
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
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // TODO: handle custom exception
        }

        return entity;
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
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // TODO: handle custom exception
        }

        return Optional.empty();
    }
}
