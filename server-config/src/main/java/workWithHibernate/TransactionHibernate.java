package workWithHibernate;

import entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;

import java.awt.*;

public class TransactionHibernate {
    private static Configuration configuration = new Configuration().configure();
    private static SessionFactory sessionFactory = configuration.buildSessionFactory();

    public static void addUser(User userSQL) {
        try (Session session = sessionFactory.openSession()) {  // есть merge  -persist
            Transaction transaction = session.beginTransaction();
            session.persist(userSQL);
            transaction.commit();
        }
    }

    public static User findByUsername(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNativeQuery("SELECT *FROM users WHERE name = :name", User.class)
                    .setParameter("name", name).getSingleResultOrNull();
        }
    }

    public static List<User> findAllUsers() {
        try (Session session = sessionFactory.openSession()){
            return session.createNativeQuery("SELECT * FROM users", User.class)
                    .getResultList();
        }
    }

}