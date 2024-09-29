package com.asalavei.tennisscoreboard.web.listeners;

import com.asalavei.tennisscoreboard.dbaccess.config.HibernateConfig;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.java.Log;
import org.flywaydb.core.Flyway;

@Log
@WebListener
public class FlywayContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Flyway flyway = Flyway.configure()
                .dataSource(HibernateConfig.getHikariDataSource())
                .load();

        flyway.migrate();
        log.info("Flyway migration completed successfully");
    }
}
