package Module2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import Module2.config.HibernateConfig;
import Module2.models.User;

public class UserDAO implements DaoInterface<User> {
    public User create(User user) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            return user;
        } catch (Exception e) {
            if  (tx != null) tx.rollback();
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
            tx = session.beginTransaction();
            User updatedUser = session.merge(userForUpdate);
            tx.commit();
            return updatedUser;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public User delete(long id) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            User user = session.find(User.class, id);
            if (user == null) return null;
            session.remove(user);
            tx.commit();
            return user;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}
