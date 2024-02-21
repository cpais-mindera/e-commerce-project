package com.mindera.api.exception;

import java.text.MessageFormat;

public class PromotionDoesNotExistsException extends RuntimeException {

    public PromotionDoesNotExistsException(Long id) {
        super(MessageFormat.format("Promotion with ID: {0} does not exists!", id));
    }
}
