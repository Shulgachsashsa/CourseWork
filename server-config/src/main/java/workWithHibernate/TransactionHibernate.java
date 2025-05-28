package workWithHibernate;

import entity.*;
import enums.RequestStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    public static List<RequestHistory> getRequestsHistoryByID(Long user_id) {
        if (user_id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM RequestHistory rh WHERE rh.user.id = :user_id",
                            RequestHistory.class)
                    .setParameter("user_id", user_id)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch request history", e);
        }
    }

    public static boolean reqMinesClothes(String clothes) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                String[] items = clothes.split(";");
                for (String item : items) {
                    String[] parts = item.split(",");
                    String typePart = parts[0].trim();
                    String quantityPart = parts[1].trim();
                    String type = typePart.substring(typePart.indexOf('=') + 1).trim();
                    int quantity = Integer.parseInt(quantityPart.substring(quantityPart.indexOf('=') + 1).trim());
                    Clothes clothesEntity = session.createQuery("FROM Clothes WHERE clothesType = :type", Clothes.class)
                            .setParameter("type", type)
                            .uniqueResult();
                    if (clothesEntity != null) {
                        if (clothesEntity.getCounter() >= quantity) {
                            clothesEntity.setCounter(clothesEntity.getCounter() - quantity);
                            session.update(clothesEntity);
                        } else {
                            throw new RuntimeException("Not enough " + type + " in stock. Available: " + clothesEntity.getCounter() + ", requested: " + quantity);
                        }
                    } else {
                        throw new RuntimeException("Clothes type not found: " + type);
                    }
                }
                transaction.commit();
                return true;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    public static boolean updateUserAccess(String username, int newAccess) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.createQuery(
                                "UPDATE User SET access = :newAccess WHERE name = :username")
                        .setParameter("newAccess", newAccess)
                        .setParameter("username", username)
                        .executeUpdate();

                transaction.commit();
                return true;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }

    public static List<Request> getRequestsHistoryWithPending(RequestStatus reqStatus) {
        String status = reqStatus.toString();
        try (Session session = sessionFactory.openSession()) {
            return session.createNativeQuery("SELECT *FROM request WHERE status = :status",
                            Request.class)
                    .setParameter("status", status)
                    .getResultList();
        }
    }

    public static boolean setNewStateRequest(Long id, RequestStatus requestStatus) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            String status = requestStatus.toString();
            session.createNativeQuery("UPDATE request SET status = :status WHERE id = :id")
                    .setParameter("status", requestStatus.toString())
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
            return true;
        } catch (Exception e) {
            System.err.println("Error updating request status: " + e.getMessage());
            throw e;
        }
    }

    public static boolean setNewHistoryReq(User user, RequestStatus requestStatus, LocalDateTime time,
                                           String reason, Long request_id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            String sql = "UPDATE request_history " +
                    "SET user_accountant_id = :userId, " +
                    "status = :status, " +
                    "processedat = :processedAt, " +
                    "accountantcomment = :comment " +
                    "WHERE request_id = :request_id";

            int updatedRows = session.createNativeQuery(sql)
                    .setParameter("userId", user.getId())
                    .setParameter("status", requestStatus.toString())
                    .setParameter("processedAt", time)
                    .setParameter("comment", reason)
                    .setParameter("request_id", request_id)
                    .executeUpdate();
            transaction.commit();
            return updatedRows > 0;
        } catch (Exception e) {
            System.err.println("Ошибка при обновлении истории запроса: " + e.getMessage());
            return false;
        }
    }

    public static Double getBudget() {
        try (Session session = sessionFactory.openSession()) {
            return session.createNativeQuery("SELECT *FROM budget WHERE id = :id", Budget.class)
                    .setParameter("id", 1).getSingleResultOrNull().getCurrentAmount();
        }
    }

    public static Request getClothesByIdReq(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNativeQuery("SELECT *FROM request WHERE id = :id", Request.class)
                    .setParameter("id", id).getSingleResultOrNull();
        }
    }

    public static boolean addBudget(Double price) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Budget budget = session.get(Budget.class, 1L);
                if (budget == null) {
                    budget = new Budget();
                    budget.setCurrentAmount(price);
                } else {
                    budget.setCurrentAmount(budget.getCurrentAmount() + price);
                }
                session.saveOrUpdate(budget);
                transaction.commit();
                return true;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
                return false;
            }
        }
    }

    public static void saveFinancialHistory(FinancialHistory financialHistory) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(financialHistory);
            transaction.commit();
        }
    }

    public static void backClothes(Map<String, Integer> map) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    String clothesType = entry.getKey();
                    Integer quantityToAdd = entry.getValue();
                    Clothes clothes = session.createQuery(
                                    "FROM Clothes WHERE clothesType = :type", Clothes.class)
                            .setParameter("type", clothesType)
                            .uniqueResult();
                    if (clothes != null) {
                        clothes.setCounter(clothes.getCounter() + quantityToAdd);
                        session.update(clothes);
                    } else {
                        Clothes newClothes = new Clothes();
                        newClothes.setClothesType(clothesType);
                        newClothes.setCounter(quantityToAdd);
                        newClothes.setPrice(0.0);
                        session.persist(newClothes);
                    }
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
    }

}