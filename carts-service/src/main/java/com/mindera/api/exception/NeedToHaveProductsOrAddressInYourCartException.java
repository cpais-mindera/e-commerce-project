package com.mindera.api.exception;

public class NeedToHaveProductsOrAddressInYourCartException extends RuntimeException {

    public NeedToHaveProductsOrAddressInYourCartException() {
        super("Need to have Products or Address in your Cart before proceeding with payment");
    }
}
