package com.asalavei.tennisscoreboard.dbaccess.config;

import com.asalavei.tennisscoreboard.dbaccess.entities.MatchEntity;
import com.asalavei.tennisscoreboard.dbaccess.entities.PlayerEntity;
import com.asalavei.tennisscoreboard.exceptions.AppRuntimeException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HibernateConfig {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static final HikariDataSource hikariDataSource = configureDataSource();

    private HibernateConfig() {
    }

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(PlayerEntity.class);
        configuration.addAnnotatedClass(MatchEntity.class);

        return configuration.buildSessionFactory();
    }

    private static HikariDataSource configureDataSource() {
        Properties properties = getProperties();

        HikariConfig config = new HikariConfig();

        config.setDriverClassName(properties.getProperty("hibernate.connection.driver_class"));
        config.setJdbcUrl(properties.getProperty("hibernate.connection.url"));
        config.setUsername(properties.getProperty("hibernate.connection.username"));
        config.setPassword(properties.getProperty("hibernate.connection.password"));

        config.setConnectionTimeout(Long.parseLong(properties.getProperty("hibernate.hikari.connectionTimeout")));
        config.setMinimumIdle(Integer.parseInt(properties.getProperty("hibernate.hikari.minimumIdle")));
        config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("hibernate.hikari.maximumPoolSize")));
        config.setIdleTimeout(Long.parseLong(properties.getProperty("hibernate.hikari.idleTimeout")));

        return new HikariDataSource(config);
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static HikariDataSource getHikariDataSource() {
        return hikariDataSource;
    }

    private static Properties getProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream = HibernateConfig.class.getClassLoader().getResourceAsStream("hibernate.properties")) {
            if (inputStream == null) {
                throw new AppRuntimeException("Configuration file 'hibernate.properties' not found in the classpath");
            }

            properties.load(inputStream);
        } catch (IOException e) {
            throw new AppRuntimeException("Failed to load 'hibernate.properties'", e);
        }

        return properties;
    }

    public static void shutdown() {
        sessionFactory.close();
        hikariDataSource.close();
    }
}
