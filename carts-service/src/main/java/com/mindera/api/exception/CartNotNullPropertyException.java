package com.mindera.api.exception;

import java.text.MessageFormat;

public class CartNotNullPropertyException extends RuntimeException {

    public CartNotNullPropertyException(String key) {
        super(MessageFormat.format("Cart requires to have {0}.", key));
    }
}
