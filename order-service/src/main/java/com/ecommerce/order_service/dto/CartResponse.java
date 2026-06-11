package com.ecommerce.order_service.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(

        Long cartId,

        String userEmail,

        List<CartItemResponse> items,

        BigDecimal totalAmount

) {
}