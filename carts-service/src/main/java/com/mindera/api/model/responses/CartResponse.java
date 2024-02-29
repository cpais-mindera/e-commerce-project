package com.mindera.api.model.responses;

import com.mindera.api.domain.*;
import com.mindera.api.enums.CartStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CartResponse {

    private UUID id;
    private Address address;
    private List<CartProduct> cartProducts;
    private List<CartPayment> paymentMethod;
    private Long user;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private Long discount;
    private CartStatus cartStatus;

    public CartResponse(Cart cart) {
        this.id = cart.getId();
        this.address = cart.getAddress();
        this.cartProducts = cart.getCartProducts();
        this.paymentMethod = cart.getPaymentMethod();
        this.user = cart.getUserId();
        this.createdAt = cart.getCreatedAt();
        this.lastModifiedAt = cart.getLastModifiedAt();
        this.discount = cart.getDiscountId();
        this.cartStatus = cart.getCartStatus();
    }

}
