package com.mindera.api.controller;

import com.mindera.api.exception.*;
import com.mindera.api.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mindera.api.model.Error.createErrorHandler;

public abstract class BaseController {

    // Exception Handlers
    @ExceptionHandler(PromotionDoesNotExistsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handlePromotionDoesNotExistsException(PromotionDoesNotExistsException ex) {
        return createErrorHandler(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(PromotionDuplicateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handlePromotionDuplicateException(PromotionDuplicateException ex) {
        return createErrorHandler(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(PromotionNotNullPropertyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handlePromotionNotNullPropertyException(PromotionNotNullPropertyException ex) {
        return createErrorHandler(HttpStatus.PRECONDITION_REQUIRED, ex);
    }

    @ExceptionHandler(UserDoesNotHavePermissionsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleUserDoesNotHavePermissionsException(UserDoesNotHavePermissionsException ex) {
        return createErrorHandler(HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(UserInternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleUserInternalServerErrorException(UserInternalServerErrorException ex) {
        return createErrorHandler(HttpStatus.UNAUTHORIZED, ex);
    }
}
