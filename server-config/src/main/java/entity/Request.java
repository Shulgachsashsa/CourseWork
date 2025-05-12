package entity;

import enums.RequestStatus;
import enums.RequestType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private RequestType type; // MONEY или CLOTHES

    @Column(name = "amount")
    private Double amount;

    @Column(name = "clothes_details", columnDefinition = "TEXT")
    private String clothesDetails;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status; // PENDING, APPROVED, REJECTED

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Request(User user, RequestType type, String clothesDetails, String reason, RequestStatus status) {
        this.user = user;
        this.type = type;
        this.clothesDetails = clothesDetails;
        this.reason = reason;
        this.status = status;
    }

    public Request(User user, RequestType type, Double amount, String reason, RequestStatus status) {
        this.user = user;
        this.type = type;
        this.amount = amount;
        this.reason = reason;
        this.status = status;
    }

    public Request() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getClothesDetails() {
        return clothesDetails;
    }

    public void setClothesDetails(String clothesDetails) {
        this.clothesDetails = clothesDetails;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}