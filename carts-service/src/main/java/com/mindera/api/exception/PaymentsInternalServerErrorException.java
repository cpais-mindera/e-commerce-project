package com.mindera.api.exception;

public class PaymentsInternalServerErrorException extends RuntimeException {

    public PaymentsInternalServerErrorException() {
        super("Internal Server Error getting Payment");
    }
}
