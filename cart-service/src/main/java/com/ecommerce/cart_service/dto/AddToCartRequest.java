package com.ecommerce.cart_service.dto;

public record AddToCartRequest(
        Long productId,
        Integer quantity
) {
}
