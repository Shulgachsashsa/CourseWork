package entity;
import enums.ClothesType;
import enums.RequestStatus;
import enums.RequestType;
import jakarta.persistence.*;

@Entity
@Table(name = "Request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private RequestType type; // MONEY или CLOTHES

    @Column(name = "amount")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "clothes_type")
    private ClothesType clothesType; // JEANS, T_SHIRT и т.д.

    @Column(name = "clothes_quantity")
    private Integer clothesQuantity;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status; // PENDING, APPROVED и т.д.

    @OneToOne(mappedBy = "request", cascade = CascadeType.ALL)
    private Transaction transaction;

    public Request() {}
    // Геттеры и сеттеры
}