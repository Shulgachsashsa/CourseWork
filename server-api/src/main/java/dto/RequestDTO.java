package dto;

import connect.Request;
import enums.RequestStatus;
import enums.RequestType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class RequestDTO implements Serializable {
    private Long id;
    private Long userId;
    private RequestType requestType;
    private String reason;
    private String clothesDetails;
    private RequestStatus requestStatus;
    private Double amount;
    private LocalDateTime createdAt;

    public RequestDTO(Long id, Long userId, RequestType requestType, String reason, String clothesDetails, RequestStatus requestStatus, Double amount) {
        this.id = id;
        this.userId = userId;
        this.requestType = requestType;
        this.reason = reason;
        this.clothesDetails = clothesDetails;
        this.requestStatus = requestStatus;
        this.amount = amount;
    }

    public RequestDTO(Long id, RequestStatus status) {
        this.id = id;
        this.requestStatus = status;
    }

    public RequestDTO(Long userId, RequestType requestType, String reason, String clothesDetails, RequestStatus requestStatus) {
        this.userId = userId;
        this.requestType = requestType;
        this.reason = reason;
        this.clothesDetails = clothesDetails;
        this.requestStatus = requestStatus;
    }

    public RequestDTO(Long userId, RequestType requestType, Double amount, String reason, RequestStatus requestStatus) {
        this.userId = userId;
        this.requestType = requestType;
        this.amount = amount;
        this.reason = reason;
        this.requestStatus = requestStatus;
    }

    public RequestDTO() {
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getClothesDetails() {
        return clothesDetails;
    }

    public void setClothesDetails(String clothesDetails) {
        this.clothesDetails = clothesDetails;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}
