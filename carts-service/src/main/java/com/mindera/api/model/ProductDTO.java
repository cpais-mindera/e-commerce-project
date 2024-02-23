package com.mindera.api.model;

import com.mindera.api.enums.Category;
import com.mindera.api.enums.Size;
import lombok.Data;

@Data
public class ProductDTO {

    private String description;
    private Double defaultPrice;
    private Double discountedPrice;
    private String name;
    private String colour;
    private Size size;
    private Category category;

}
