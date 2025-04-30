package dto;

import java.io.Serializable;
import enums.Role;


public class UserDTO implements Serializable {

    public String login;
    public String password;
    public String salt;
    public String access;
    private Role role;

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

    public UserDTO() {}

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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
