package com.ecommerce.order_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse>
    handleOrderNotFound(
            OrderNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new ErrorResponse(
                                LocalDateTime.now(),
                                404,
                                ex.getMessage()));
    }

    @ExceptionHandler(
            EmptyCartException.class)
    public ResponseEntity<ErrorResponse>
    handleEmptyCart(
            EmptyCartException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ErrorResponse(
                                LocalDateTime.now(),
                                400,
                                ex.getMessage()));
    }
}
