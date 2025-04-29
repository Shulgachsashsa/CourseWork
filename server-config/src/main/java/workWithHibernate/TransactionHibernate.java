package workWithHibernate;

import entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;

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

}