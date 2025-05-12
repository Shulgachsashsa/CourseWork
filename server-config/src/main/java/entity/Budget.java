package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Budget")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double currentAmount = 0.0;

    public Budget(Long id, Double currentAmount) {
        this.id = id;
        this.currentAmount = currentAmount;
    }

    public Budget() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Double currentAmount) {
        this.currentAmount = currentAmount;
    }
}