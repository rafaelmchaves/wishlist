package com.wishlist.common.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = { CustomException.class })
    public ResponseEntity<Object> handle(CustomException exception, WebRequest request) {
        ExceptionMetadata exceptionMetadata = exception.getClass().getAnnotation(ExceptionMetadata.class);

        if (Objects.isNull(exceptionMetadata)) {
            throw exception;
        }

        log.error("Error - Code: " + exception.getCode() + " Message: " + exception.getMessage() + (exception.getCause() != null ? " - " + exception.getCause() : ""));
        final var errorMessage = new ErrorMessage(exception.getMessage(), exception.getCode());
        HttpStatus httpStatus = exceptionMetadata.httpStatus();
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), httpStatus);
    }

}