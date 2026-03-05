package Module2Tests;

import Module2.config.HibernateConfig;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Module2.models.User;
import Module2.dao.UserDAO;

public class UserDAOTests {

    private UserDAO userDAO;

    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();

        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);
        transaction = mock(Transaction.class);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test
    void create_shouldPersistUser() {

        User user = new User();
        user.setName("Ivan");
        user.setEmail("ivan@test.com");

        try (MockedStatic<HibernateConfig> mocked = mockStatic(HibernateConfig.class)) {

            mocked.when(HibernateConfig::getSessionFactory).thenReturn(sessionFactory);

            User result = userDAO.create(user);

            verify(session).persist(user);
            verify(transaction).commit();

            assertEquals(user, result);
        }
    }

    @Test
    void get_shouldReturnUser() {

        User user = new User();
        user.setId(1L);

        try (MockedStatic<HibernateConfig> mocked = mockStatic(HibernateConfig.class)) {

            mocked.when(HibernateConfig::getSessionFactory).thenReturn(sessionFactory);
            when(session.find(User.class, 1L)).thenReturn(user);

            User result = userDAO.get(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
        }
    }

    @Test
    void getAll_shouldReturnUserList() {

        List<User> users = List.of(new User(), new User());

        try (MockedStatic<HibernateConfig> mocked = mockStatic(HibernateConfig.class)) {

            mocked.when(HibernateConfig::getSessionFactory).thenReturn(sessionFactory);

            var query = mock(org.hibernate.query.Query.class);
            when(session.createQuery("from User", User.class)).thenReturn(query);
            when(query.list()).thenReturn(users);

            List<User> result = userDAO.getAll();

            assertEquals(2, result.size());
        }
    }

    @Test
    void update_shouldMergeUser() {

        User user = new User();
        user.setId(1L);

        try (MockedStatic<HibernateConfig> mocked = mockStatic(HibernateConfig.class)) {

            mocked.when(HibernateConfig::getSessionFactory).thenReturn(sessionFactory);
            when(session.merge(user)).thenReturn(user);

            User result = userDAO.update(user);

            verify(session).merge(user);
            verify(transaction).commit();

            assertEquals(user, result);
        }
    }

    @Test
    void delete_shouldRemoveUser() {

        User user = new User();
        user.setId(1L);

        try (MockedStatic<HibernateConfig> mocked = mockStatic(HibernateConfig.class)) {

            mocked.when(HibernateConfig::getSessionFactory).thenReturn(sessionFactory);
            when(session.find(User.class, 1L)).thenReturn(user);

            User result = userDAO.delete(1L);

            verify(session).remove(user);
            verify(transaction).commit();

            assertEquals(user, result);
        }
    }

    @Test
    void delete_shouldReturnNullIfUserNotFound() {

        try (MockedStatic<HibernateConfig> mocked = mockStatic(HibernateConfig.class)) {

            mocked.when(HibernateConfig::getSessionFactory).thenReturn(sessionFactory);
            when(session.find(User.class, 1L)).thenReturn(null);

            User result = userDAO.delete(1L);

            assertNull(result);
            verify(transaction).rollback();
        }
    }
}
