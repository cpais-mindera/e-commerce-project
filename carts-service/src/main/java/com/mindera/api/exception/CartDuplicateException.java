package com.mindera.api.exception;

import java.text.MessageFormat;

public class CartDuplicateException extends RuntimeException {

    private static final String NAME = "carts_name_key";
    private static final String DESCRIPTION = "carts_description_key";


    public CartDuplicateException(String key) {
        super(MessageFormat.format("Cart with key {0} already exists.", mapDuplicateKeyError(key)));
    }

    public static String mapDuplicateKeyError(String key) {
        return switch (key) {
            case NAME -> "name";
            case DESCRIPTION -> "description";
            default -> key;
        };
    }
}
