package com.mindera.api.exception;

public class InvalidHeaderAuthorizationException extends RuntimeException {

    public InvalidHeaderAuthorizationException() {
        super("Invalid header authorization token.");
    }
}
