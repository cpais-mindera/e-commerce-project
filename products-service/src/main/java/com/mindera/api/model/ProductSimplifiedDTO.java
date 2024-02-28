package com.mindera.api.model;

import com.mindera.api.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSimplifiedDTO {

    private UUID id;
    private Double defaultPrice;
    private String name;

    public ProductSimplifiedDTO(Product product) {
        this.id = product.getId();
        this.defaultPrice = product.getDefaultPrice();
        this.name = product.getName();
    }
}
