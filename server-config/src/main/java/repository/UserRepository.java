package repository;

import entity.UserSQL;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    private final EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Сохранение пользователя (регистрация)
    public UserSQL save(UserSQL user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(user); // INSERT
            transaction.commit();
            return user;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при сохранении пользователя", e);
        }
    }

    // Поиск по ID
    public Optional<UserSQL> findById(Long id) {
        UserSQL user = entityManager.find(UserSQL.class, id);
        return Optional.ofNullable(user);
    }

    // Поиск по логину (для авторизации)
    public Optional<UserSQL> findByUsername(String username) {
        TypedQuery<UserSQL> query = entityManager.createQuery(
                "SELECT u FROM UserSQL u WHERE u.username = :username",
                UserSQL.class
        );
        query.setParameter("username", username);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    // Поиск по роли
    public List<UserSQL> findByRole(String role) {
        TypedQuery<UserSQL> query = entityManager.createQuery(
                "SELECT u FROM UserSQL u WHERE u.role = :role",
                UserSQL.class
        );
        query.setParameter("role", role);
        return query.getResultList();
    }

    // Обновление пользователя
    public UserSQL update(UserSQL user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            UserSQL mergedUser = entityManager.merge(user); // UPDATE
            transaction.commit();
            return mergedUser;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при обновлении пользователя", e);
        }
    }

    // Удаление пользователя
    public void delete(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            UserSQL user = entityManager.find(UserSQL.class, id);
            if (user != null) {
                entityManager.remove(user); // DELETE
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }
    }
}