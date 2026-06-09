package com.ecommerce.cart_service.dto;

import java.math.BigDecimal;

public record CartItemResponse(
        Long productId,

        String productName,

        BigDecimal price,

        Integer quantity
) {
}
