package com.mindera.api.exception;

import java.text.MessageFormat;
import java.util.UUID;

public class ProductDoesNotExistsInCartException extends RuntimeException {

    public ProductDoesNotExistsInCartException(UUID productId, UUID cartId) {
        super(MessageFormat.format("Product {0} does not exists in cart {1}.", productId, cartId));
    }
}
