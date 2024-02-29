package com.mindera.api.model.responses;

import com.mindera.api.domain.Address;
import com.mindera.api.domain.Cart;
import com.mindera.api.domain.CartPayment;
import com.mindera.api.domain.PaymentMethod;
import com.mindera.api.model.DiscountDTO;
import com.mindera.api.model.ProductDTO;
import com.mindera.api.model.UserDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CartFullResponse {

    private UUID id;
    private double totalPrice;
    private Address address;
    private List<ProductDTO> cartProducts;
    private List<CartPayment> paymentMethod;
    private UserDTO user;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private DiscountDTO discount;

    public CartFullResponse(Cart cart, List<ProductDTO> cartProducts, UserDTO user, DiscountDTO discount) {
        this.id = cart.getId();
        this.totalPrice = cart.getTotalPrice();
        this.address = cart.getAddress();
        this.cartProducts = cartProducts;
        this.paymentMethod = cart.getPaymentMethod();
        this.user = user;
        this.createdAt = cart.getCreatedAt();
        this.lastModifiedAt = cart.getLastModifiedAt();
        this.discount = discount;
    }

}
