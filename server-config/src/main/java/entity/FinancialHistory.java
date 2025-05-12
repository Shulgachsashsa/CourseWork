package entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_history")
public class FinancialHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long requestId; // Ссылка на запрос

    @Column(nullable = false)
    private Double amountChange; // Изменение бюджета (может быть + или -)

    @Column(nullable = false)
    private Double newBalance; // Итоговый бюджет после операции

    @Column(nullable = false)
    private LocalDateTime operationDate;

    @Column(nullable = false)
    private Long processedBy; // Кто обработал операцию

    @Column(length = 500)
    private String comment; // Комментарий к операции

    // Геттеры и сеттеры
}