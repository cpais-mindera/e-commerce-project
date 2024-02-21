package com.mindera.api.exception;

public class ProductsInternalServerErrorException extends RuntimeException {

    public ProductsInternalServerErrorException() {
        super("Internal Server Error getting Product");
    }
}
