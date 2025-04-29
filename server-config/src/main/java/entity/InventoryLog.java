package entity;
import enums.ClothesType;
import enums.InventoryOperation;
import jakarta.persistence.*;

@Entity
@Table(name = "InventoryLog")
public class InventoryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;

    @Enumerated(EnumType.STRING)
    private ClothesType clothesType;

    @Enumerated(EnumType.STRING)
    private InventoryOperation operation; // ADD, REMOVE, ADJUST

    private Integer quantity;
    private Integer previousQuantity;
    private Integer newQuantity;

    @ManyToOne
    @JoinColumn(name = "performed_by")
    private User performedBy;

    public InventoryLog() {}
    // Геттеры и сеттеры
}