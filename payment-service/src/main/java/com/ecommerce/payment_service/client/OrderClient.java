package com.ecommerce.payment_service.client;

import com.ecommerce.payment_service.dto.OrderResponse;
import com.ecommerce.payment_service.dto.UpdateOrderStatusRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "order-service")
public interface OrderClient {

    @GetMapping("/api/orders/{id}")
    OrderResponse getOrderById(@PathVariable Long id,
                               @RequestHeader("Authorization")
                               String token);

    @PutMapping("/api/orders/{id}/status")
    void updateOrderStatus(
            @PathVariable Long id,
            @RequestBody
            UpdateOrderStatusRequest request,
            @RequestHeader("Authorization")
            String token
    );
}
