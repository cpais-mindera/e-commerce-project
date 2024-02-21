package com.mindera.api.exception;

public class UserDoesNotHavePermissionsException extends RuntimeException {

    public UserDoesNotHavePermissionsException() {
        super("User does not have permissions to perform that action.");
    }
}
