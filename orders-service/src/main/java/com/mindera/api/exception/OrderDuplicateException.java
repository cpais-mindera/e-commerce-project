package com.mindera.api.exception;

import java.text.MessageFormat;

public class OrderDuplicateException extends RuntimeException {

    private static final String CART_ID = "orders_cartId_key";

    public OrderDuplicateException(String key) {
        super(MessageFormat.format("Order with key {0} already exists.", mapDuplicateKeyError(key)));
    }

    public static String mapDuplicateKeyError(String key) {
        return switch (key) {
            case CART_ID -> "cartId";
            default -> key;
        };
    }
}
