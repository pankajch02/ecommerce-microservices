package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.dto.OrderResponse;
import com.ecommerce.order_service.dto.UpdateOrderStatusRequest;
import com.ecommerce.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(

            @RequestHeader("Authorization")
            String token) {

        return orderService
                .placeOrder(token);
    }

    @GetMapping
    public List<OrderResponse>
    getMyOrders() {

        return orderService
                .getMyOrders();
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(
            @PathVariable Long id) {

        return orderService
                .getOrderById(id);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String>
    updateStatus(

            @PathVariable Long id,

            @RequestBody
            UpdateOrderStatusRequest request) {

        orderService.updateOrderStatus(
                id,
                request.status());

        return ResponseEntity.ok(
                "Order updated");
    }
}
