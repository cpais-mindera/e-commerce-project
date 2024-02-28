package com.mindera.api.model;

import com.mindera.api.domain.Promotion;
import com.mindera.api.enums.Category;
import com.mindera.api.enums.PromotionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionDTO {

    private Long id;
    private String name;
    private String description;
    private Double discount;
    private UUID productId;
    private Category category;
    private boolean applyToAllProducts;
    private PromotionStatus promotionStatus;

    public PromotionDTO(Promotion promotion) {
        this.id = promotion.getId();
        this.name = promotion.getName();
        this.description = promotion.getDescription();
        this.discount = promotion.getDiscount();
        this.productId = promotion.getProductId();
        this.category = promotion.getCategory();
        this.applyToAllProducts = promotion.isApplyToAllProducts();
        this.promotionStatus = promotion.getPromotionStatus();

    }
}
