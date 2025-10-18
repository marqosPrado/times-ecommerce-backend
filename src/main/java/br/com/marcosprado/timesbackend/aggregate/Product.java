package br.com.marcosprado.timesbackend.aggregate;

import br.com.marcosprado.timesbackend.aggregate.purchase_order.OrderItem;
import br.com.marcosprado.timesbackend.enums.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "PRODUCTS")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "brand", length = 50,  nullable = false)
    private String brand;

    @Column(name = "title", nullable = false)
    private String title;

    @Column()
    private BigDecimal price;

    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "line", nullable = false)
    private String line;

    @Column(name = "style", nullable = false)
    private String style;

    @Column(name = "mechanism", nullable = false)
    private String mechanism;

    @Column(name = "box_material", nullable = false)
    private String boxMaterial;

    @Column(name = "box_format", nullable = false)
    private String boxFormat;

    @Column(name = "dial", nullable = false)
    private String dial;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageAggregate> images;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<OrderItem> orderItems;

    public Product() {}

    public Product(
            Long id,
            String brand,
            String title,
            BigDecimal price,
            Gender gender,
            String line,
            String style,
            String mechanism,
            String boxMaterial,
            String boxFormat,
            String dial,
            List<ImageAggregate> images
    ) {
        this.id = id;
        this.brand = brand;
        this.title = title;
        this.price = price;
        this.gender = gender;
        this.line = line;
        this.style = style;
        this.mechanism = mechanism;
        this.boxMaterial = boxMaterial;
        this.boxFormat = boxFormat;
        this.dial = dial;
        this.images = images;
        this.orderItems = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getMechanism() {
        return mechanism;
    }

    public void setMechanism(String mechanism) {
        this.mechanism = mechanism;
    }

    public String getBoxMaterial() {
        return boxMaterial;
    }

    public void setBoxMaterial(String boxMaterial) {
        this.boxMaterial = boxMaterial;
    }

    public String getBoxFormat() {
        return boxFormat;
    }

    public void setBoxFormat(String boxFormat) {
        this.boxFormat = boxFormat;
    }

    public String getDial() {
        return dial;
    }

    public void setDial(String dial) {
        this.dial = dial;
    }

    public List<ImageAggregate> getImages() {
        return images;
    }

    public void setImages(List<ImageAggregate> images) {
        this.images = images;
    }
}
