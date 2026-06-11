package com.ecommerce.order_service.service;

import com.ecommerce.order_service.client.CartClient;
import com.ecommerce.order_service.config.CurrentUserUtil;
import com.ecommerce.order_service.dto.CartItemResponse;
import com.ecommerce.order_service.dto.CartResponse;
import com.ecommerce.order_service.dto.OrderItemResponse;
import com.ecommerce.order_service.dto.OrderResponse;
import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.entity.OrderItem;
import com.ecommerce.order_service.entity.OrderStatus;
import com.ecommerce.order_service.exception.EmptyCartException;
import com.ecommerce.order_service.exception.OrderNotFoundException;
import com.ecommerce.order_service.repository.OrderItemRepository;
import com.ecommerce.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl
        implements OrderService {

    private final CartClient cartClient;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final CurrentUserUtil currentUserUtil;

    @Override
    public OrderResponse placeOrder(
            String token) {
        CartResponse cart =
                cartClient.getCart(token);

        if(cart.items().isEmpty()) {

            throw new EmptyCartException(
                    "Cart is empty");
        }

        Order order =
                Order.builder()
                        .userEmail(
                                cart.userEmail())
                        .totalAmount(
                                cart.totalAmount())
                        .status(
                                OrderStatus.CREATED)
                        .createdAt(
                                LocalDateTime.now())
                        .build();

        order = orderRepository.save(order);

        for(CartItemResponse item
                : cart.items()) {

            OrderItem orderItem =
                    OrderItem.builder()
                            .productId(
                                    item.productId())
                            .productName(
                                    item.productName())
                            .price(
                                    item.price())
                            .quantity(
                                    item.quantity())
                            .order(order)
                            .build();

             orderItemRepository.save(orderItem);
        }

        cartClient.clearCart(token);




        return mapToResponse(order);

    }

    @Override
    public OrderResponse getOrderById(
            Long orderId) {

        String email =
                currentUserUtil
                        .getCurrentUserEmail();

        Order order =
                orderRepository
                        .findByIdAndUserEmail(
                                orderId,
                                email)
                        .orElseThrow(
                                () ->
                                        new OrderNotFoundException(
                                                "Order not found"));

        return mapToResponse(order);
    }

    @Override
    public List<OrderResponse> getMyOrders() {

        String email =
                currentUserUtil
                        .getCurrentUserEmail();

        return orderRepository
                .findByUserEmail(email)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void updateOrderStatus(
            Long orderId,
            OrderStatus status) {

        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(
                                () ->
                                        new OrderNotFoundException(
                                                "Order not found"));

        order.setStatus(status);

        orderRepository.save(order);

        log.info(
                "Order {} updated to {}",
                orderId,
                status);
    }

    private OrderResponse mapToResponse(
            Order order) {

        List<OrderItemResponse> items =
                orderItemRepository
                        .findByOrderId(
                                order.getId())
                        .stream()
                        .map(item ->
                                new OrderItemResponse(
                                        item.getProductId(),
                                        item.getProductName(),
                                        item.getPrice(),
                                        item.getQuantity()))
                        .toList();

        return new OrderResponse(
                order.getId(),
                order.getUserEmail(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getCreatedAt(),
                items);
    }




}
