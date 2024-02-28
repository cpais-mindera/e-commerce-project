package com.mindera.api.model;

import com.mindera.api.domain.Product;
import com.mindera.api.enums.Category;
import com.mindera.api.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private UUID id;
    private String description;
    private Double defaultPrice;
    private String name;
    private String colour;
    private Integer stock;
    private Category category;
    private ProductStatus productStatus;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.description = product.getDescription();
        this.defaultPrice = product.getDefaultPrice();
        this.name = product.getName();
        this.colour = product.getColour();
        this.stock = product.getStock();
        this.category = product.getCategory();
        this.productStatus = product.getProductStatus();
    }
}
