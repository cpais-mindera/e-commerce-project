package com.mindera.api.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "carts")
@ToString
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private double totalPrice;

    @Embedded
    private Address address;

    @OneToOne(mappedBy = "cart")
    private List<CartProducts> cartProducts;

    @Embedded
    private PaymentMethod paymentMethod;

    @Column
    private Long userId;

    @Column
    private Long discountId;

    @Column
    @CreatedDate
    private Date createdAt;

}
