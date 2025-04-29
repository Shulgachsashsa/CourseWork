package dto;

import javax.management.relation.Role;
import java.io.Serializable;

public class UserDTO implements Serializable {

    public String login;
    public String password;
    public String salt;
    public String access;
    public Role role;

    public UserDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserDTO(String login, String password, String salt, String access, Role role) {
        this.login = login;
        this.password = password;
        this.salt = salt;
        this.access = access;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
