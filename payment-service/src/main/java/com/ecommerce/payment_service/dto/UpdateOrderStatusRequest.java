package com.ecommerce.payment_service.dto;

import com.ecommerce.payment_service.entity.OrderStatus;

public record UpdateOrderStatusRequest(
        OrderStatus status
) {
}
