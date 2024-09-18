package com.asalavei.tennisscoreboard;

import com.asalavei.tennisscoreboard.dbaccess.entities.MatchEntity;
import com.asalavei.tennisscoreboard.dbaccess.entities.PlayerEntity;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.java.Log;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Log
@WebListener
public class HibernateContextListener implements ServletContextListener {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Hibernate SessionFactory initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (sessionFactory != null) {
            sessionFactory.close();
        }

        log.info("Hibernate SessionFactory closed");
    }

    public static SessionFactory getSessionFactory() {
        return HibernateContextListener.sessionFactory;
    }

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(PlayerEntity.class);
        configuration.addAnnotatedClass(MatchEntity.class);

        return configuration.buildSessionFactory();
    }
}
