package com.mindera.api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
@Entity
@Table(name = "cart_products")
public class CartProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "cart_id")
    private UUID cartId;

    private List<UUID> productListIds;


}
