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

    @Column(nullable = false)
    private Long userId; // Кто создал запрос

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

    @Column(nullable = false)
    private LocalDateTime createdAt; // Дата создания

    @Column
    private LocalDateTime processedAt; // Дата обработки

    @Column(length = 500)
    private String accountantComment; // Комментарий админа

    @Column(length = 500)
    private String reason;

    public RequestHistory(Long userId, RequestType type, Double amount, RequestStatus status, String reason) {
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.reason = reason;
        this.createdAt = LocalDateTime.now();
    }

    public RequestHistory(Long userId, RequestType type, String details, RequestStatus status, String reason) {
        this.userId = userId;
        this.type = type;
        this.details = details;
        this.status = status;
        this.reason = reason;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
}