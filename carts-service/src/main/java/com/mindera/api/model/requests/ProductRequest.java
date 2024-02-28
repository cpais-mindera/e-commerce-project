package com.mindera.api.model.requests;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductRequest {

    private UUID productId;
    private Integer quantity;

}
