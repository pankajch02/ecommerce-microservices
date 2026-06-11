package com.ecommerce.order_service.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long productId,

        String productName,

        BigDecimal price,

        Integer quantity
) {
}
