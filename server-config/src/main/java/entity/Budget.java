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

}