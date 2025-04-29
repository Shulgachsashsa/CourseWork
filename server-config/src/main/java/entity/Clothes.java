package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Clothes")
public class Clothes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jeans", nullable = false)
    private Integer jeans = 0;

    @Column(name = "t_shirt", nullable = false)
    private Integer tShirt = 0;

    @Column(name = "hoodie", nullable = false)
    private Integer hoodie = 0;

    @Column(name = "zip_hoodie", nullable = false)
    private Integer zipHoodie = 0;

    public Clothes(Long id, Integer jeans, Integer tShirt, Integer hoodie, Integer zipHoodie) {
        this.id = id;
        this.jeans = jeans;
        this.tShirt = tShirt;
        this.hoodie = hoodie;
        this.zipHoodie = zipHoodie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getJeans() {
        return jeans;
    }

    public void setJeans(Integer jeans) {
        this.jeans = jeans;
    }

    public Integer gettShirt() {
        return tShirt;
    }

    public void settShirt(Integer tShirt) {
        this.tShirt = tShirt;
    }

    public Integer getHoodie() {
        return hoodie;
    }

    public void setHoodie(Integer hoodie) {
        this.hoodie = hoodie;
    }

    public Integer getZipHoodie() {
        return zipHoodie;
    }

    public void setZipHoodie(Integer zipHoodie) {
        this.zipHoodie = zipHoodie;
    }

    public Clothes() {}
}