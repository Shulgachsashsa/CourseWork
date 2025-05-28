package dto;

import enums.RequestStatus;
import enums.RequestType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FinancialHistoryDTO implements Serializable {
    private Double amountChange; // Изменение бюджета (может быть + или -)
    private Double newBalance; // Итоговый бюджет после операции
    private Long processedBy;
    private String comment;
    private Long requestId;

    public FinancialHistoryDTO(Double amountChange, Double newBalance, Long processedBy, String comment, Long requestId) {
        this.amountChange = amountChange;
        this.newBalance = newBalance;
        this.processedBy = processedBy;
        this.comment = comment;
        this.requestId = requestId;
    }

    public Double getAmountChange() {
        return amountChange;
    }

    public void setAmountChange(Double amountChange) {
        this.amountChange = amountChange;
    }

    public Double getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(Double newBalance) {
        this.newBalance = newBalance;
    }

    public Long getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(Long processedBy) {
        this.processedBy = processedBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
}
