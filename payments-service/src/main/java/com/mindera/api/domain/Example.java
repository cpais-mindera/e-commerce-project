package com.mindera.api.domain;

import com.mindera.api.enums.Category;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "promotions")
@ToString
public class Example {

    /*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String description;

    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "1.0", inclusive = true)
    private Double discount;

    @Column
    private UUID productId;

    @Column
    private Category category;

    @Column
    private boolean applyToAllProducts;

    @Column
    private PromotionStatus promotionStatus;


    public static Example patchPromotion(Example promotion, Example promotionDatabase) {
        if (promotion.getName() != null) {
            promotionDatabase.setName(promotion.getName());
        }

        if (promotion.getDescription() != null) {
            promotionDatabase.setDescription(promotion.getDescription());
        }

        if (promotion.getDiscount() != null) {
            promotionDatabase.setDiscount(promotion.getDiscount());
        }

        if (promotion.getProductId() != null) {
            promotionDatabase.setProductId(promotion.getProductId());
        }

        if (promotion.getCategory() != null) {
            promotionDatabase.setCategory(promotion.getCategory());
        }

        promotionDatabase.setApplyToAllProducts(promotion.isApplyToAllProducts());

        if (promotion.getPromotionStatus() != null) {
            promotionDatabase.setPromotionStatus(promotion.getPromotionStatus());
        }

        return promotionDatabase;
    }

     */
}
