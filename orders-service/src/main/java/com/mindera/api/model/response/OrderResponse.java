package com.mindera.api.model.response;

import com.mindera.api.domain.Order;
import com.mindera.api.enums.Status;
import com.mindera.api.model.UserDTO;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long orderId;
    private UUID cartId;
    private Status orderStatus;

    public OrderResponse(Order order) {
        this.orderId = order.getOrderId();
        this.cartId = order.getCartId();
        this.orderStatus = order.getOrderStatus();
    }
}
