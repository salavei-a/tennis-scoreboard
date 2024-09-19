package com.asalavei.tennisscoreboard.web.listeners;

import com.asalavei.tennisscoreboard.dbaccess.config.HibernateUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.java.Log;

@Log
@WebListener
public class HibernateContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HibernateUtil.getSessionFactory();
        log.info("Hibernate SessionFactory initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.shutdown();
        log.info("Hibernate SessionFactory closed");
    }
}
