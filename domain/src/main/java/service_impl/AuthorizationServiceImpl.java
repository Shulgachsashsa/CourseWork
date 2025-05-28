package service_impl;

import connect.Response;
import dto.UserDTO;
import entity.User;
import workWithHibernate.TransactionHibernate;
import java.util.ArrayList;
import java.util.List;

import static workWithHibernate.TransactionHibernate.*;

public class AuthorizationServiceImpl {

    public Response authorization(UserDTO dto) {
        try {
            User user = findByUsername(dto.getLogin());
            System.out.println(user.getName());
            String passwordCheck = RegistrationServiceImpl.hashPassword(dto.getPassword(), user.getSalt());
            if (user != null && passwordCheck.equals(user.getHash()) && user.getAccess().equals("1")) {
                return new Response(1, user.getRole());
            } else
                return new Response(0, false);
        } catch (Exception e) {
            return new Response(-1, "Ошибка сервера: " + e.getMessage());
        }
    }

    public Response getID(String username) {
        User user = findByUsername(username);
        return new Response(1, user.getId());
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
        return new Response(1, deleteUserByName(login));
    }

    public Response editAccess(UserDTO userDTO) {
        try {
            TransactionHibernate.updateUserAccess(userDTO.getLogin(), Integer.parseInt(userDTO.getAccess()));
            return new Response(1, "true");
        } catch (Exception e) {
            return new Response(-1, "false");
        }
    }

}
