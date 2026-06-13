package com.ecommerce.payment_service.dto;

import com.ecommerce.payment_service.entity.OrderStatus;

import java.math.BigDecimal;

public record OrderResponse(

        Long orderId,

        String userEmail,

        BigDecimal totalAmount,

        OrderStatus status

) {
}
