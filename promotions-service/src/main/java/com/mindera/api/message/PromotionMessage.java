package com.mindera.api.message;

import com.mindera.api.enums.Category;
import com.mindera.api.enums.EventType;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PromotionMessage {

    private EventType eventType;
    private String name;
    private UUID productId;
    private double discount;
    private Category category;
    private boolean applyToAllProducts;
}
