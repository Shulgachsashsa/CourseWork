package dto;

import enums.RequestStatus;
import enums.RequestType;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RequestHistoryDTO implements Serializable {
    private Long user; // Кто создал запрос
    private Long accountantUser;
    private RequestType type; // MONEY или CLOTHES
    private Double amount; // Для денежных запросов
    private String details; // Детали запроса в JSON
    private RequestStatus status; // PENDING, APPROVED, REJECTED
    private LocalDateTime createdAt; // Дата создания
    private LocalDateTime processedAt; // Дата обработки
    private String accountantComment; // Комментарий админа
    private String reason;
    private Long requestID;

    public RequestHistoryDTO(Long user, Long accountantUser, RequestType type, Double amount, String details, RequestStatus status, LocalDateTime createdAt, LocalDateTime processedAt, String accountantComment, String reason) {
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
    }

    public RequestHistoryDTO(Long accountantUser, RequestStatus status, LocalDateTime processedAt, String accountantComment, LocalDateTime createdAt, Long requestID) {
        this.accountantUser = accountantUser;
        this.status = status;
        this.processedAt = processedAt;
        this.accountantComment = accountantComment;
        this.requestID = requestID;
    }

    public RequestHistoryDTO(Long accountantUser, RequestStatus status, LocalDateTime processedAt, LocalDateTime createdAt, Long requestID) {
        this.accountantUser = accountantUser;
        this.status = status;
        this.processedAt = processedAt;
        this.requestID = requestID;
    }

    public RequestHistoryDTO() {}

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getAccountantUser() {
        return accountantUser;
    }

    public void setAccountantUser(Long accountantUser) {
        this.accountantUser = accountantUser;
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

    public Long getRequestID() {
        return requestID;
    }

    public void setRequestID(Long requestID) {
        this.requestID = requestID;
    }
}
