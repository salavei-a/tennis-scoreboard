package com.asalavei.tennisscoreboard.dbaccess;

import com.asalavei.tennisscoreboard.dbaccess.entities.MatchEntity;
import com.asalavei.tennisscoreboard.dbaccess.entities.PlayerEntity;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(PlayerEntity.class);
        configuration.addAnnotatedClass(MatchEntity.class);

        return configuration.buildSessionFactory();
    }
}
