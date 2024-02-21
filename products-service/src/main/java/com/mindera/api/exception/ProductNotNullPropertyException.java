package com.mindera.api.exception;

import java.text.MessageFormat;

public class ProductNotNullPropertyException extends RuntimeException {

    public ProductNotNullPropertyException(String key) {
        super(MessageFormat.format("Products requires to have {0}.", key));
    }
}
