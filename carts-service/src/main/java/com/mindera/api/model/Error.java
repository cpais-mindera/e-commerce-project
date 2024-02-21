package com.mindera.api.model;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Error {

    private String message;
    private Integer errorCode;

    public static ResponseEntity<Error> createErrorHandler(HttpStatus httpStatus, Throwable ex) {
        Error error = new Error();
        error.setErrorCode(httpStatus.value());
        error.setMessage(ex.getMessage());
        return ResponseEntity.status(httpStatus).body(error);
    }
}
