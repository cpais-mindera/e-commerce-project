package com.mindera.api.controller;

import com.mindera.api.exception.*;
import com.mindera.api.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.mindera.api.model.Error.createErrorHandler;

@RestController
@ControllerAdvice
public abstract class BaseController {

    @ExceptionHandler(CartAlreadyConvertedToOrderException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleCartAlreadyConvertedToOrderException(CartAlreadyConvertedToOrderException ex) {
        return createErrorHandler(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(CartAlreadyExistsForUserException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleCartAlreadyExistsForUserException(CartAlreadyExistsForUserException ex) {
        return createErrorHandler(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(CartDoesNotExistsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleCartDoesNotExistsException(CartDoesNotExistsException ex) {
        return createErrorHandler(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(CartDuplicateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleCartDuplicateException(CartDuplicateException ex) {
        return createErrorHandler(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(CartNotNullPropertyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleCartNotNullPropertyException(CartNotNullPropertyException ex) {
        return createErrorHandler(HttpStatus.PRECONDITION_REQUIRED, ex);
    }

    @ExceptionHandler(DiscountsInternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleDiscountsInternalServerErrorException(DiscountsInternalServerErrorException ex) {
        return createErrorHandler(HttpStatus.BAD_GATEWAY, ex);
    }

    @ExceptionHandler(NeedToHaveProductsOrAddressInYourCartException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleNeedToHaveProductsOrAddressInYourCartException(NeedToHaveProductsOrAddressInYourCartException ex) {
        return createErrorHandler(HttpStatus.PRECONDITION_REQUIRED, ex);
    }

    @ExceptionHandler(PaymentsInternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handlePaymentsInternalServerErrorException(PaymentsInternalServerErrorException ex) {
        return createErrorHandler(HttpStatus.BAD_GATEWAY, ex);
    }

    @ExceptionHandler(ProductDoesNotExistsInCartException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleProductDoesNotExistsInCartException(ProductDoesNotExistsInCartException ex) {
        return createErrorHandler(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(ProductsInternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleUserDoesNotHavePermissionsException(ProductsInternalServerErrorException ex) {
        return createErrorHandler(HttpStatus.BAD_GATEWAY, ex);
    }

    @ExceptionHandler(UserInternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleUserInternalServerErrorException(UserInternalServerErrorException ex) {
        return createErrorHandler(HttpStatus.BAD_GATEWAY, ex);
    }
}
