package com.mindera.api.exception;

import java.text.MessageFormat;

public class UserNotNullPropertyException extends RuntimeException {

    public UserNotNullPropertyException(String key) {
        super(MessageFormat.format("User requires to have {0}.", key));
    }
}
