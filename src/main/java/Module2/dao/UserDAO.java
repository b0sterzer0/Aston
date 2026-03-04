package Module2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Module2.config.HibernateConfig;
import Module2.models.User;

public class UserDAO implements DaoInterface<User> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);

    public User create(User user) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            LOGGER.info("Creating User. Username: {} Email: {}", user.getName(), user.getEmail());
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            LOGGER.info("User created with id: {}",  user.getId());
            return user;
        } catch (Exception e) {
            if  (tx != null) tx.rollback();
            LOGGER.error("User creation failed", e);
            throw e;
        }
    }

    public List<User> getAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    public User get(long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.find(User.class, id);
        }
    }

    public User update(User userForUpdate) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            LOGGER.info("Updating User. ID: {} Username: {} Email: {} Age: {}",
                    userForUpdate.getId(),
                    userForUpdate.getName(),
                    userForUpdate.getEmail(),
                    userForUpdate.getAge()
            );
            tx = session.beginTransaction();
            User updatedUser = session.merge(userForUpdate);
            tx.commit();
            LOGGER.info("Successful updating User with ID: {}",  userForUpdate.getId());
            return updatedUser;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOGGER.error("User update failed with ID: {}",  userForUpdate.getId(), e);
            throw e;
        }
    }

    public User delete(long id) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            LOGGER.info("Deleting User with ID: {}", id);
            tx = session.beginTransaction();
            User user = session.find(User.class, id);
            if (user == null) {
                tx.rollback();
                return null;
            }
            session.remove(user);
            tx.commit();
            LOGGER.info("Successful deleting User with ID: {}",  user.getId());
            return user;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            LOGGER.error("Deleting User with ID: {} failed",  id, e);
            throw e;
        }
    }
}
