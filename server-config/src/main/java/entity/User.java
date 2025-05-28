package entity;

import enums.Role;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "hash", nullable = false, length = 255)
    private String hash;

    @Column(name = "salt", nullable = false, length = 255)
    private String salt;

    @Column(name = "access", length = 50)
    private String access;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Request> requests;

    @OneToMany(mappedBy = "processedBy")
    private List<FinancialHistory> processedOperations;

    @OneToMany(mappedBy = "user")
    private List<RequestHistory> requestHistories;

    public User(String name, String hash, String salt, String access, Role role) {
        this.name = name;
        this.hash = hash;
        this.salt = salt;
        this.access = access;
        this.role = role;
    }

    public User(String name, String access) {
        this.name = name;
        this.access = access;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}