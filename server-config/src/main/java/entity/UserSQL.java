package entity;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "users")
public class UserSQL implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "UserName", nullable = false)
    private String username;

    @Column(name = "HashPassword", nullable = false)
    private String hashPassword;

    @Column(name = "Salt", nullable = false)
    private String salt;

    // 0 - no access
    // 1 - have access
    @Column(name = "Access", nullable = false, columnDefinition = "varchar(3) default '1'")
    private String access;

    // 0 - без роли
    // 1 - глав бух, ответственный за учет бюджета
    // 2 - ответственный за учет материалов (затраты, закуп)
    // 3 - ответственный за отправку товара на розницу
    // 4 - администратор назначения должностей

    @Column(name = "Role", nullable = false, columnDefinition = "varchar(3) default '0'")
    private String role;

    public UserSQL(String username, String hashPassword, String salt, String access, String role) {
        this.username = username;
        this.hashPassword = hashPassword;
        this.salt = salt;
        this.access = access;
        this.role = role;
    }

    public UserSQL() {
        this.role = "1";
        this.access = "1";
    }

    @Override
    public String toString() {
        return "UserSQL{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", hashPassword='" + hashPassword + '\'' +
                ", salt='" + salt + '\'' +
                ", access='" + access + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public UserSQL(Long id, String username, String hashPassword, String salt, String access, String role) {
        this.id = id;
        this.username = username;
        this.hashPassword = hashPassword;
        this.salt = salt;
        this.access = access;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public String getSalt() {
        return salt;
    }

    public String getAccess() {
        return access;
    }

    public String getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            this.username = "defaultUserName";
        } else
            this.username = username;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

