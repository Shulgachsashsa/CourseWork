package entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_history")
public class FinancialHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private Request request; // Ссылка на запрос

    @Column(nullable = false)
    private Double amountChange; // Изменение бюджета (может быть + или -)

    @Column(nullable = false)
    private Double newBalance; // Итоговый бюджет после операции

    @Column(nullable = false)
    private LocalDateTime operationDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "processed_by", nullable = false)
    private User processedBy;

    @Column(length = 500)
    private String comment; // Комментарий к операции

    public FinancialHistory(Request request, Double amountChange, Double newBalance, User processedBy, String comment) {
        this.request = request;
        this.amountChange = amountChange;
        this.newBalance = newBalance;
        this.processedBy = processedBy;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
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

    public LocalDateTime getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(LocalDateTime operationDate) {
        this.operationDate = operationDate;
    }

    public User getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(User processedBy) {
        this.processedBy = processedBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}