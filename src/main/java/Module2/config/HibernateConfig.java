package Module2.config;

import Module2.models.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HibernateConfig {
    private static final SessionFactory sessionFactory = createSessionFactory();

    private static SessionFactory createSessionFactory() {
        try (InputStream in = HibernateConfig.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (in == null) throw new IllegalStateException("Cannot find database properties file.");
            Properties dbProperties = new Properties();
            dbProperties.load(in);
            String dbUrl = String.format("jdbc:postgresql://%s:%s/%s",
                    dbProperties.getProperty("db.host"),
                    dbProperties.getProperty("db.port"),
                    dbProperties.getProperty("db.db_name")
            );

            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.url", dbUrl);
            configuration.setProperty("hibernate.connection.username", dbProperties.getProperty("db.username"));
            configuration.setProperty("hibernate.connection.password", dbProperties.getProperty("db.password"));
            configuration.addAnnotatedClass(User.class);
            return configuration.buildSessionFactory();

        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
