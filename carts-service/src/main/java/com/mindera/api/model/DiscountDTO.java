package com.mindera.api.model;

import lombok.Data;

@Data
public class DiscountDTO {

    private Long id;
    private String name;
    private String description;
    private Double discount;

}
