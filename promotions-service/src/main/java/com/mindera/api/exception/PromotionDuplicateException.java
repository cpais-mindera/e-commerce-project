package com.mindera.api.exception;

import java.text.MessageFormat;

public class PromotionDuplicateException extends RuntimeException {

    private static final String NAME = "promotions_name_key";
    private static final String DESCRIPTION = "promotions_description_key";


    public PromotionDuplicateException(String key) {
        super(MessageFormat.format("Promotion with key {0} already exists.", mapDuplicateKeyError(key)));
    }

    public static String mapDuplicateKeyError(String key) {
        return switch (key) {
            case NAME -> "name";
            case DESCRIPTION -> "description";
            default -> key;
        };
    }
}
