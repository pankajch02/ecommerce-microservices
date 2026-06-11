package com.ecommerce.order_service.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,

        int status,

        String message
) {
}
