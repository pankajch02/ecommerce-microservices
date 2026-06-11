package com.ecommerce.order_service.service;


import com.ecommerce.order_service.dto.OrderResponse;
import com.ecommerce.order_service.entity.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(
            String token);

    List<OrderResponse> getMyOrders();
    OrderResponse getOrderById(
            Long orderId);


    void updateOrderStatus(
            Long orderId,
            OrderStatus status);
}
