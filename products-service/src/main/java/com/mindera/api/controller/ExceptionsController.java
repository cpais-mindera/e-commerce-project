package com.mindera.api.controller;

import com.mindera.api.domain.Product;
import com.mindera.api.exception.*;
import com.mindera.api.model.Error;
import com.mindera.api.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.mindera.api.model.Error.createErrorHandler;


@RestController
@ControllerAdvice
@RequestMapping("/products")
public abstract class ExceptionsController {

    // Exception Handlers
    @ExceptionHandler(ProductDoesNotExistsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleProductDoesNotExistsException(ProductDoesNotExistsException ex) {
        return createErrorHandler(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(ProductDuplicateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleProductDuplicateException(ProductDuplicateException ex) {
        return createErrorHandler(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(ProductNotNullPropertyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Error> handleProductNotNullPropertyException(ProductNotNullPropertyException ex) {
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
