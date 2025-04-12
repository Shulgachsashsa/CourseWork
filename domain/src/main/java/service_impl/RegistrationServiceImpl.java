package service_impl;

import connect.Response;
import dto.RegistrationDTO;
import entity.UserSQL;
import service_interface.RegistrationService;
import workWithHibernate.TransactionHibernate;

public class RegistrationServiceImpl implements RegistrationService {

    @Override
    public Response registration(RegistrationDTO dto) {
        try {
            if (TransactionHibernate.findByUsername(dto.getLogin()) != null) {
                return new Response(1, "Логин уже занят");
            }
            UserSQL newUser = new UserSQL();
            newUser.setUsername(dto.getLogin());
            newUser.setHashPassword(hashPassword(dto.getPassword()));
            newUser.setSalt(generateSalt());
            TransactionHibernate.addUser(newUser);
            return new Response(0, "Регистрация успешна");
        } catch (Exception e) {
            return new Response(-1, "Ошибка сервера: " + e.getMessage());
        }
    }

    private String hashPassword(String password) {
        return password + "_hashed";
    }

    private String generateSalt() {
        return "random_salt";
    }
}