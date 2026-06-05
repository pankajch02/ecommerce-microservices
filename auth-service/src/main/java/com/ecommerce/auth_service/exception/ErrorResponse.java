package com.ecommerce.auth_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String message
) {
}
