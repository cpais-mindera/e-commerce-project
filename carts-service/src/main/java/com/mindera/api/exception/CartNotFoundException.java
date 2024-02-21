package com.mindera.api.exception;

import java.text.MessageFormat;
import java.util.UUID;

public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(UUID cartId) {
        super(MessageFormat.format("Cart ID: {0} does not exists!", cartId));
    }
}
