package com.mindera.api.model;

import com.mindera.api.domain.Address;
import com.mindera.api.domain.Cart;
import com.mindera.api.domain.PaymentMethod;
import lombok.Data;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class CartDTO {

    private UUID id;

    private double totalPrice;

    private Address address;

    private List<ProductDTO> cartProducts;

    private PaymentMethod paymentMethod;

    private UserDTO user;

    private Date createdAt;

    private DiscountDTO discount;

    public CartDTO(Cart cart) {
        this.id = cart.getId();
        this.totalPrice = cart.getTotalPrice();
        this.address = cart.getAddress();
        this.cartProducts = cart.getCartProducts();
        this.paymentMethod = cart.getPaymentMethod();
        this.user = cart.getUserId();
        this.createdAt = cart.getCreatedAt();
        this.discount = cart.getDiscountId();

    }

}
