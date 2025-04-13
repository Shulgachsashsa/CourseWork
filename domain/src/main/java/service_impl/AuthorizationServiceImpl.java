package service_impl;

import connect.Response;
import dto.UserDTO;
import entity.UserSQL;
import workWithHibernate.TransactionHibernate;

public class AuthorizationServiceImpl {

    public Response authorization(UserDTO dto) {
        try {
            UserSQL user = TransactionHibernate.findByUsername(dto.getLogin());
            String passwordCheck = RegistrationServiceImpl.hashPassword(dto.getPassword(), user.getSalt());
            if (user != null && passwordCheck.equals(user.getHashPassword())) {
                return new Response(1, true); // добавить передачу роли и доступа
            } else
                return new Response(0, false);
        } catch (Exception e) {
            return new Response(-1, "Ошибка сервера: " + e.getMessage());
        }

    }
}
