package workWithHibernate;

import entity.UserSQL;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class TransactionHibernate {
    private static Configuration configuration = new Configuration().configure();
    private static SessionFactory sessionFactory = configuration.buildSessionFactory();

    public static void addUser(UserSQL userSQL) {
        try (Session session = sessionFactory.openSession()) {  // есть merge  -persist
            Transaction transaction = session.beginTransaction();
            session.persist(userSQL);
            transaction.commit();
        }
    }

    public static UserSQL findByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNativeQuery("SELECT *FROM users WHERE username = :username", UserSQL.class)
                    .setParameter("username", username).getSingleResultOrNull();
        }
    }
}