package com.mindera.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscountDTO {

    private Long id;
    private String name;
    private String description;
    private Double discount;

}