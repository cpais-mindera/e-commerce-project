package com.mindera.api.exception;

import java.text.MessageFormat;
import java.util.UUID;

public class ProductDoesNotExistsException extends RuntimeException {

    public ProductDoesNotExistsException(UUID uuid) {
        super(MessageFormat.format("Product with UUID: {0} doesn't exist.", uuid));
    }
}
