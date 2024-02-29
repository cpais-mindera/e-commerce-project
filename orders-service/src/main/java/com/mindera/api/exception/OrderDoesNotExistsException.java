package com.mindera.api.exception;

import java.text.MessageFormat;
import java.util.UUID;

public class OrderDoesNotExistsException extends RuntimeException {

    public OrderDoesNotExistsException(Long orderId) {
        super(MessageFormat.format("There is no order with ID: {0}", orderId));
    }
}
