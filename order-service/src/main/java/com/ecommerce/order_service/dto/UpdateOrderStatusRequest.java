package com.ecommerce.order_service.dto;

import com.ecommerce.order_service.entity.OrderStatus;

public record UpdateOrderStatusRequest(
        OrderStatus status
) {
}