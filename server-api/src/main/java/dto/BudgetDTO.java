package dto;

import java.io.Serializable;

public class BudgetDTO implements Serializable {
    private Double currentAmount;

    public BudgetDTO(Double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public BudgetDTO() {}

    public Double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Double currentAmount) {
        this.currentAmount = currentAmount;
    }
}
