package com.mindera.api.exception;

import java.text.MessageFormat;

public class PromotionNotNullPropertyException extends RuntimeException {

    public PromotionNotNullPropertyException(String key) {
        super(MessageFormat.format("Promotion requires to have {0}.", key));
    }
}
