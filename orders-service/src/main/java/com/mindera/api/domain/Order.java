package com.mindera.api.domain;

import com.mindera.api.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "orders")
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @Column(unique = true, nullable = false)
    private UUID cartId;
    @Column
    private Status orderStatus;
}
