package com.mindera.api.exception;

public class CartAlreadyConvertedToOrderException extends RuntimeException {

    public CartAlreadyConvertedToOrderException() {
        super("Cart already have payment and is converted to order");
    }
}
