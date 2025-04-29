package service_impl;

import connect.Response;
import dto.UserDTO;
import entity.User;
import service_interface.RegistrationService;
import workWithHibernate.TransactionHibernate;

import java.util.Random;

public class RegistrationServiceImpl implements RegistrationService {

    @Override
    public Response registration(UserDTO dto) {
        try {
            if (TransactionHibernate.findByUsername(dto.getLogin()) != null) {
                return new Response(1, "Логин уже занят");
            }
            User newUser = new User();
            newUser.setName(dto.getLogin());
            String salt = generateSalt();
            newUser.setHash(hashPassword(dto.getPassword(), salt));
            newUser.setSalt(salt);
            TransactionHibernate.addUser(newUser);
            return new Response(0, "Регистрация успешна");
        } catch (Exception e) {
            return new Response(-1, "Ошибка сервера: " + e.getMessage());
        }
    }

    public static String hashPassword(String password, String salt) {
        int hash = 7;
        String saltInput = password + salt;
        for (int i = 0; i < saltInput.length(); i++) {
            hash = hash * 31 + saltInput.charAt(i);
        }
        return String.valueOf(hash);
    }

    private String generateSalt() {
        Random random = new Random();
        return String.valueOf(100_00 + random.nextInt(9_900_000));
    }
}