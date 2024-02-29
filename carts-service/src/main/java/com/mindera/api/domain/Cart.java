package com.mindera.api.domain;

import com.mindera.api.enums.CartStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private double totalPrice;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartPayment> paymentMethod;

    @Column
    private Long userId;

    @Column
    private Long discountId;

    @Column
    @Enumerated(value = EnumType.STRING)
    private CartStatus cartStatus;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime lastModifiedAt;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartProduct> cartProducts;

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", totalPrice=" + totalPrice +
                ", address=" + address +
                ", paymentMethod=" + paymentMethod +
                ", userId=" + userId +
                ", discountId=" + discountId +
                ", cartStatus=" + cartStatus +
                ", createdAt=" + createdAt +
                ", lastModifiedAt=" + lastModifiedAt +
                ", cartProducts=" + cartProducts +
                '}';
    }
}
