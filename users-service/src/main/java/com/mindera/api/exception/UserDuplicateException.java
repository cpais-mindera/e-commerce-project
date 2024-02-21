package com.mindera.api.exception;

import java.text.MessageFormat;

public class UserDuplicateException extends RuntimeException {

    private static final String EMAIL = "users_email_key";
    private static final String USERNAME = "users_username_key";
    private static final String VAT_NUMBER = "users_vatNumber_key";

    public UserDuplicateException(String key) {
        super(MessageFormat.format("User with key {0} already exists.", mapDuplicateKeyError(key)));
    }

    public static String mapDuplicateKeyError(String key) {
        return switch (key) {
            case EMAIL -> "email";
            case USERNAME -> "username";
            case VAT_NUMBER -> "vatNumber";
            default -> key;
        };
    }
}
