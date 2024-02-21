package com.mindera.api.exception;

public class UserInternalServerErrorException extends RuntimeException {

    public UserInternalServerErrorException() {
        super("Internal Server Error getting Users");
    }
}
