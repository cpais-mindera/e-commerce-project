package com.mindera.api.exception;

import java.text.MessageFormat;
import java.util.UUID;

public class CartDoesNotExistsException extends RuntimeException {

    public CartDoesNotExistsException(UUID cartId) {
        super(MessageFormat.format("Cart ID: {0} does not exists!", cartId));
    }

    public CartDoesNotExistsException(Long userId) {
        super(MessageFormat.format("Cart for User Id: {0} does not exists!", userId));
    }
}
