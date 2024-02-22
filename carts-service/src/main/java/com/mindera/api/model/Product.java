package com.mindera.api.model;

import com.mindera.api.enums.Category;
import com.mindera.api.enums.Size;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class Product {

    private UUID id;
    private String description;
    private Double defaultPrice;
    private Double discountedPrice;
    private String name;
    private String colour;
    private Size size;
    private Integer stock;
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
