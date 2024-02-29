package com.mindera.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "cart_payment")
public class CartPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column
    private double amount;

    @Embedded
    private PaymentMethod paymentMethod;

    @Override
    public String toString() {
        return "CartPayment{" +
                "id=" + id +
                ", paymentMethod=" + paymentMethod +
                '}';
    }
}
