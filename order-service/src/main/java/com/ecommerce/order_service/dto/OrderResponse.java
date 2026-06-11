package com.ecommerce.order_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(

        Long orderId,

        String userEmail,

        BigDecimal totalAmount,

        String status,

        LocalDateTime createdAt,

        List<OrderItemResponse> items

) {
}
