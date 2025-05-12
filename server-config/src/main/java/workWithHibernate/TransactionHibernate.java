package workWithHibernate;

import entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.HashMap;
import java.util.List;

import java.awt.*;
import java.util.Map;
import java.util.Objects;

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

    public static User findByID(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNativeQuery("SELECT *FROM users WHERE id = :id", User.class)
                    .setParameter("id", id).getSingleResultOrNull();
        }
    }

    public static List<User> findAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createNativeQuery("SELECT * FROM users", User.class)
                    .getResultList();
        }
    }

    public static boolean deleteUserByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            int deletedCount = session.createNativeQuery("DELETE FROM users WHERE name = :name")
                    .setParameter("name", name).executeUpdate();
            transaction.commit();
            if (deletedCount > 0)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Clothes> getClothes() {
        try (Session session = sessionFactory.openSession()){
            return session.createNativeQuery("SELECT * FROM clothes", Clothes.class)
                    .getResultList();
        }
    }

    public static void createRequestOnTheClothes(Request request) {
        try (Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            session.persist(request);
            transaction.commit();
        }
    }

    public static void createRequestOnTheBudget(Request request) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(request);
            transaction.commit();
        }
    }

    public static void addReqOnTheLog(RequestHistory requestHistory) {
        try (Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            session.persist(requestHistory);
            transaction.commit();
        }
    }


}