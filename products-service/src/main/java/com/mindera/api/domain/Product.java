package com.mindera.api.domain;

import com.mindera.api.enums.Category;
import com.mindera.api.enums.Size;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "products")
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Double defaultPrice;
    @Column
    private Double discountedPrice;
    @Column(unique = true)
    private String name;
    @Column(nullable = false)
    private String colour;
    @Column
    private Size size;
    @Column
    private Integer stock;
    @Column
    @Enumerated(EnumType.STRING)
    private Category category;

    public Product(String description, Double defaultPrice, Double discountedPrice, String name, String colour, Size size, Integer stock, Category category) {
        this.description = description;
        this.defaultPrice = defaultPrice;
        this.discountedPrice = discountedPrice;
        this.name = name;
        this.colour = colour;
        this.size = size;
        this.stock = stock;
        this.category = category;
    }

    public Product(String description, Double defaultPrice, String name) {
        this.description = description;
        this.defaultPrice = defaultPrice;
        this.name = name;
    }

    public static Product patchProduct(Product product, Product productDatabase) {
        if (product.getDescription() != null) {
            productDatabase.setDescription(product.getDescription());
        }

        if (product.getDefaultPrice() != null) {
            productDatabase.setDefaultPrice(product.getDefaultPrice());
        }

        if (product.getDiscountedPrice() != null) {
            productDatabase.setDiscountedPrice(product.getDiscountedPrice());
        }

        if (product.getName() != null) {
            productDatabase.setName(product.getName());
        }

        if (product.getColour() != null) {
            productDatabase.setColour(product.getColour());
        }

        if (product.getSize() != null) {
            productDatabase.setSize(product.getSize());
        }

        if (product.getStock() != null) {
            productDatabase.setStock(product.getStock());
        }

        if (product.getCategory() != null) {
            productDatabase.setCategory(product.getCategory());
        }

        return productDatabase;
    }
}
