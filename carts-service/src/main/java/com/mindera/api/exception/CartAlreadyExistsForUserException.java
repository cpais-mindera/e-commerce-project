package com.mindera.api.exception;

import java.text.MessageFormat;
import java.util.UUID;

public class CartAlreadyExistsForUserException extends RuntimeException {

    public CartAlreadyExistsForUserException(Long userId) {
        super(MessageFormat.format("Cart already exists for UserId: {0}", userId));
    }
}
