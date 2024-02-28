package com.mindera.api.exception;

public class DiscountsInternalServerErrorException extends RuntimeException {

    public DiscountsInternalServerErrorException() {
        super("Internal Server Error getting Discount");
    }
}
