package entity;

import enums.ClothesType;
import enums.TransactionType;
import jakarta.persistence.*;

@Entity
@Table(name = "Transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "request_id")
    private Request request;

    @Enumerated(EnumType.STRING)
    private TransactionType type; // INCOME или OUTCOME

    @Column(name = "amount")
    private Double amount;

    @Enumerated(EnumType.STRING)
    private ClothesType clothesType;

    @Column(name = "clothes_quantity")
    private Integer clothesQuantity;

    @ManyToOne
    @JoinColumn(name = "performed_by")
    private User performedBy;

    public Transaction() {}

    // Геттеры и сеттеры
}