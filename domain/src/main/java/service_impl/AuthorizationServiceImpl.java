package service_impl;

import connect.Response;
import dto.UserDTO;
import entity.User;
import workWithHibernate.TransactionHibernate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationServiceImpl {

    public Response authorization(UserDTO dto) {
        try {
            User user = TransactionHibernate.findByUsername(dto.getLogin());
            System.out.println(user.getName());
            String passwordCheck = RegistrationServiceImpl.hashPassword(dto.getPassword(), user.getSalt());
            if (user != null && passwordCheck.equals(user.getHash())) {
                return new Response(1, true); // добавить передачу роли и доступа
            } else
                return new Response(0, false);
        } catch (Exception e) {
            return new Response(-1, "Ошибка сервера: " + e.getMessage());
        }

    }

    public Response getListUsers() {
        List<User> users = TransactionHibernate.findAllUsers();
        List<UserDTO> userDTO = new ArrayList<>();
        for (User user: users) {
            UserDTO userDTO1 = new UserDTO(user.getName(), user.getHash(), user.getSalt(), user.getAccess(), user.getRole());
            userDTO.add(userDTO1);
        }
        return new Response(1, userDTO);
    }

    public Response deleteUser(String login) {
        return new Response(1, TransactionHibernate.deleteUserByName(login));
    }

}
