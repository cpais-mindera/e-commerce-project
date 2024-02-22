package com.mindera.api.domain;

import com.mindera.api.enums.Category;
import com.mindera.api.enums.Size;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
@Entity
@Table(name = "cart_items")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column
    private String description;
    @Column
    private double defaultPrice;
    @Column
    private double discountedPrice;
    @Column
    private String name;
    @Column
    private String colour;
    @Column
    private Size size;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public Product(String description, double defaultPrice, double discountedPrice, String name, String colour, Size size, Cart cart) {
        this.description = description;
        this.defaultPrice = defaultPrice;
        this.discountedPrice = discountedPrice;
        this.name = name;
        this.colour = colour;
        this.size = size;
        this.cart = cart;
    }

    public Product(String description, double defaultPrice, String name) {
        this.description = description;
        this.defaultPrice = defaultPrice;
        this.name = name;
    }
}
