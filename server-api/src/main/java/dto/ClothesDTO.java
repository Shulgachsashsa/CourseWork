package dto;

import jakarta.persistence.Column;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ClothesDTO implements Serializable {

    private Integer counter = 0;
    private String clothesType;
    private Double price;

    public ClothesDTO(Integer counter, String clothesType, Double price) {
        this.counter = counter;
        this.clothesType = clothesType;
        this.price = price;
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
