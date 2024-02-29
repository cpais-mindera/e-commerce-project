package com.mindera.api.controller;

import com.mindera.api.exception.*;
import com.mindera.api.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.mindera.api.model.Error.createErrorHandler;

public abstract class BaseController {

    @ExceptionHandler(InvalidHeaderAuthorizationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleInvalidHeaderAuthorization(InvalidHeaderAuthorizationException ex) {
        return createErrorHandler(HttpStatus.NOT_ACCEPTABLE, ex);
    }

    @ExceptionHandler(UserDoesNotExistsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleUserNotFound(UserDoesNotExistsException ex) {
        return createErrorHandler(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(UserDoesNotHavePermissionsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleUserDoesNotHavePermissions(UserDoesNotHavePermissionsException ex) {
        return createErrorHandler(HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(UserNotNullPropertyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleUserDoesNotHaveVatNumber(UserNotNullPropertyException ex) {
        return createErrorHandler(HttpStatus.PRECONDITION_FAILED, ex);
    }

    @ExceptionHandler(UserDuplicateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleUserDuplicateException(UserDuplicateException ex) {
        return createErrorHandler(HttpStatus.CONFLICT, ex);
    }
}
