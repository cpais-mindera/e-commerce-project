package com.mindera.api.controller;

import com.mindera.api.exception.*;
import com.mindera.api.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mindera.api.model.Error.createErrorHandler;

@RestController
@ControllerAdvice
public abstract class ExceptionsController {

    @ExceptionHandler(CartDoesNotExistsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleCartDoesNotExistsException(CartDoesNotExistsException ex) {
        return createErrorHandler(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(OrderCantSwitchToThatOrderStatus.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleOrderCantSwitchToThatOrderStatus(OrderCantSwitchToThatOrderStatus ex) {
        return createErrorHandler(HttpStatus.NOT_ACCEPTABLE, ex);
    }

    @ExceptionHandler(OrderDoesNotExistsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleOrderDoesNotExistsException(OrderDoesNotExistsException ex) {
        return createErrorHandler(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(OrderDuplicateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleOrderDuplicateException(OrderDuplicateException ex) {
        return createErrorHandler(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(OrderNotNullPropertyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleOrderNotNullPropertyException(OrderNotNullPropertyException ex) {
        return createErrorHandler(HttpStatus.PRECONDITION_REQUIRED, ex);
    }

    @ExceptionHandler(UserInternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleUserInternalServerErrorException(UserInternalServerErrorException ex) {
        return createErrorHandler(HttpStatus.BAD_GATEWAY, ex);
    }
}
