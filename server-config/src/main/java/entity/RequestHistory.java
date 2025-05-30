package entity;

import enums.RequestStatus;
import enums.RequestType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "request_history")
public class RequestHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Кто создал запрос

    @ManyToOne
    @JoinColumn(name = "user_accountant_id")
    private User accountantUser;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request requestId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType type; // MONEY или CLOTHES

    @Column
    private Double amount; // Для денежных запросов

    @Column(columnDefinition = "TEXT")
    private String details; // Детали запроса в JSON

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status; // PENDING, APPROVED, REJECTED

    @Column
    private LocalDateTime createdAt; // Дата создания

    @Column
    private LocalDateTime processedAt; // Дата обработки

    @Column(length = 500)
    private String accountantComment; // Комментарий админа

    @Column(length = 500)
    private String reason;

    public RequestHistory() {}

    public RequestHistory(User user, User accountantUser, RequestType type, Double amount, String details, RequestStatus status, LocalDateTime createdAt, LocalDateTime processedAt, String accountantComment, String reason, Request requestId) {
        this.user = user;
        this.accountantUser = accountantUser;
        this.type = type;
        this.amount = amount;
        this.details = details;
        this.status = status;
        this.createdAt = createdAt;
        this.processedAt = processedAt;
        this.accountantComment = accountantComment;
        this.reason = reason;
        this.requestId = requestId;
    }

    public RequestHistory(User user, RequestType type, Double amount, RequestStatus status, String reason, Request requestId) {
        this.user = user;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.reason = reason;
        this.createdAt = LocalDateTime.now();
        this.requestId = requestId;
    }

    public RequestHistory(User user, RequestType type, String details, RequestStatus status, String reason, Request requestId) {
        this.user = user;
        this.type = type;
        this.details = details;
        this.status = status;
        this.reason = reason;
        this.createdAt = LocalDateTime.now();
        this.requestId = requestId;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getAccountantComment() {
        return accountantComment;
    }

    public void setAccountantComment(String accountantComment) {
        this.accountantComment = accountantComment;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public User getAccountantUser() {
        return accountantUser;
    }

    public void setAccountantUser(User accountantUser) {
        this.accountantUser = accountantUser;
    }

    public Request getRequestId() {
        return requestId;
    }

    public void setRequestId(Request requestId) {
        this.requestId = requestId;
    }
}