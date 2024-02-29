package com.mindera.api.exception;

import java.text.MessageFormat;
import java.util.UUID;

public class CartDoesNotExistsException extends RuntimeException {

    public CartDoesNotExistsException(UUID cartId) {
        super(MessageFormat.format("There is no order with Cart ID: {0}", cartId));
    }
}
