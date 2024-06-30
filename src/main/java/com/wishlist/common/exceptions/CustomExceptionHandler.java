package com.wishlist.common.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);


    }

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

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

}