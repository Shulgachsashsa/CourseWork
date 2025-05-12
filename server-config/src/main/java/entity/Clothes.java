package entity;

import enums.ClothesType;
import jakarta.persistence.*;

@Entity
@Table(name = "Clothes")
public class Clothes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "counter", nullable = false)
    private Integer counter = 0;

    @Column(name = "type", nullable = false, length = 100)
    private String clothesType;

    @Column(name = "price_for_1", nullable = false)
    private Double price;

    public Clothes() {}

    public Clothes(Long id, Integer counter, String clothesType) {
        this.id = id;
        this.counter = counter;
        this.clothesType = clothesType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public String getClothesType() {
        return clothesType;
    }

    public void setClothesType(String clothesType) {
        this.clothesType = clothesType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}