package com.mindera.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindera.api.enums.Category;
import com.mindera.api.enums.Size;
import lombok.Data;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {

    private UUID productId;
    private String description;
    private Integer quantity;
    private Double defaultPrice;
    private Double discountedPrice;
    private String name;
    private String colour;
    private Size size;
    private Category category;

}
