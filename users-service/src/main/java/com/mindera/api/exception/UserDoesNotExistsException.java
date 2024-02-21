package com.mindera.api.exception;

public class UserDoesNotExistsException extends RuntimeException {

    public UserDoesNotExistsException(Long id) {
        super("User with ID: " + id + " doesn't exists.");
    }

    public UserDoesNotExistsException() {
        super("User doesn't exists.");
    }

    public UserDoesNotExistsException(String vatNumber) {
        super("User with VAT Number: " + vatNumber + " doesn't exists.");
    }
}
