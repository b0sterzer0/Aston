package Module2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import Module2.config.HibernateConfig;
import Module2.models.User;

public class UserDAO {
    public void create(User user) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        }
    }

    public List<User> getUsers() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {

        }
        return null;
    }

    public User getUser(long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            User user = session.find(User.class, id);
            if (user == null) throw new IllegalArgumentException("User with id " + id + " is not found");
            return user;
        }
    }

    public User update(long id, User updatedUser) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            User userForUpdate = session.find(User.class, id);
            if (userForUpdate == null) throw new IllegalArgumentException("User with id " + id + " is not found");
            userForUpdate.setName(updatedUser.getName());
            userForUpdate.setEmail(updatedUser.getEmail());
            userForUpdate.setAge(updatedUser.getAge());
            tx.commit();
            return userForUpdate;
        }
    }

    public void delete(long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            User user = session.find(User.class, id);
            session.remove(user);
            tx.commit();
        }
    }
}
