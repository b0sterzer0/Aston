package Module2.config;

import Module2.models.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HibernateConfig {
    private static final SessionFactory sessionFactory;
    private static final Properties dbProperties = new Properties();

    static {
        String dbUrl = constructUrlToDatabase();
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.url", dbUrl);
        configuration.setProperty("hibernate.connection.username", dbProperties.getProperty("db.username"));
        configuration.setProperty("hibernate.connection.password", dbProperties.getProperty("db.password"));
        configuration.addAnnotatedClass(User.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    private static String constructUrlToDatabase() {
        try (InputStream in = HibernateConfig.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (in == null) throw new IllegalStateException("Cannot find database properties file.");
            dbProperties.load(in);
            String host = dbProperties.getProperty("db.host");
            String port = dbProperties.getProperty("db.port");
            String dbName = dbProperties.getProperty("db.db_name");

            String urlFormat = "jdbc:postgresql://%s:%s/%s";
            return String.format(urlFormat, host, port, dbName);

        } catch (IOException e) {
            throw new RuntimeException("Loading database properties failed.", e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
