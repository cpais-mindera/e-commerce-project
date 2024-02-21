package com.mindera.api.exception;

import java.text.MessageFormat;

public class ProductDuplicateException extends RuntimeException {

    private static final String NAME = "products_name_key";

    public ProductDuplicateException(String key) {
        super(MessageFormat.format("Product with key {0} already exists.", mapDuplicateKeyError(key)));
    }

    public static String mapDuplicateKeyError(String key) {
        return switch (key) {
            case NAME -> "name";
            default -> key;
        };
    }
}
