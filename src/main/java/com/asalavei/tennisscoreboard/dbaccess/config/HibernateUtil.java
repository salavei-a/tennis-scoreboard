package com.asalavei.tennisscoreboard.dbaccess.config;

import com.asalavei.tennisscoreboard.dbaccess.entities.MatchEntity;
import com.asalavei.tennisscoreboard.dbaccess.entities.PlayerEntity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(PlayerEntity.class);
        configuration.addAnnotatedClass(MatchEntity.class);

        return configuration.buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return HibernateUtil.sessionFactory;
    }

    public static void shutdown() {
        sessionFactory.close();
    }
}
