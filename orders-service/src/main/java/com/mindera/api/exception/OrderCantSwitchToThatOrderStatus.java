package com.mindera.api.exception;

import com.mindera.api.enums.Status;

import java.text.MessageFormat;

public class OrderCantSwitchToThatOrderStatus extends RuntimeException {

    public OrderCantSwitchToThatOrderStatus(Long orderId, Status statusFrom, Status statusTo) {
        super(MessageFormat.format("Order {0} can NOT be changed from Status: {1} to Status: {2}",
                orderId,
                statusFrom,
                statusTo));
    }
}
