package com.mindera.api.exception;

import java.text.MessageFormat;

public class OrderNotNullPropertyException extends RuntimeException {

    public OrderNotNullPropertyException(String key) {
        super(MessageFormat.format("Order requires to have {0}.", key));
    }
}
