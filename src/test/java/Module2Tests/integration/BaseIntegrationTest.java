package Module2Tests.integration;
import Module2.config.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class BaseIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(BaseIntegrationTest.class);

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @BeforeAll
    static void beforeAll() {
        // передаем параметры контейнера Hibernate
        System.setProperty("hibernate.connection.url", postgresContainer.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgresContainer.getUsername());
        System.setProperty("hibernate.connection.password", postgresContainer.getPassword());

        // Hibernate создаст таблицы автоматически
        System.setProperty("hibernate.hbm2ddl.auto", "create-drop");

        log.info("Testcontainer started at {}", postgresContainer.getJdbcUrl());
    }

    protected SessionFactory getSessionFactory() {
        return HibernateConfig.getSessionFactory();
    }

    protected void cleanDatabase() {

        try (Session session = getSessionFactory().openSession()) {

            session.beginTransaction();

            session.createMutationQuery("delete from User").executeUpdate();

            session.getTransaction().commit();
        }
    }
}
