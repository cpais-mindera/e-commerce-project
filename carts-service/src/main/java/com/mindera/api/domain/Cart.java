package com.mindera.api.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "promotions")
@ToString
public class Cart {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column
    private double totalPrice;

    @Embedded
    private Address address;

    @Column
    private List<Product> products;

    @Embedded
    private PaymentMethod paymentMethod;

    @Column
    private Long userId;

    @Column
    @CreatedDate
    private Date createdAt;


    /*
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
