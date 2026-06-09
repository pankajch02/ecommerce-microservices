package com.ecommerce.cart_service.service;

import com.ecommerce.cart_service.dto.AddToCartRequest;
import com.ecommerce.cart_service.dto.CartResponse;



public interface CartService {

    void addToCart(
            AddToCartRequest request
    );

    CartResponse getCart();

    void updateQuantity(
            Long productId,
            Integer quantity
    );

    void removeItem(Long productId);

    void clearCart();


}
