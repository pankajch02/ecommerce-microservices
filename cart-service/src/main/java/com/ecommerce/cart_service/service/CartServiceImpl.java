package com.ecommerce.cart_service.service;

import com.ecommerce.cart_service.client.ProductClient;
import com.ecommerce.cart_service.config.CurrentUserUtil;
import com.ecommerce.cart_service.dto.AddToCartRequest;
import com.ecommerce.cart_service.dto.CartItemResponse;
import com.ecommerce.cart_service.dto.CartResponse;
import com.ecommerce.cart_service.dto.ProductResponse;
import com.ecommerce.cart_service.entity.Cart;
import com.ecommerce.cart_service.entity.CartItem;
import com.ecommerce.cart_service.repository.CartItemRepository;
import com.ecommerce.cart_service.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductClient productClient;

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final CurrentUserUtil currentUserUtil;

    public void addToCart(
            AddToCartRequest request
    ){

        String email = currentUserUtil.getCurrentUserEmail();

        Cart cart = cartRepository
                .findByUserEmail(email)
                .orElseGet(
                        () -> cartRepository.save(
                                Cart.builder()
                                        .userEmail(email)
                                        .build()
                        )
                );

        ProductResponse product = productClient
                .getProduct(request.productId());

        Optional<CartItem> existingItem =
                cartItemRepository.findByCartIdAndProductId(
                        cart.getId(),
                        request.productId()
                );
        if(existingItem.isPresent()){

            CartItem item = existingItem.get();

            item.setQuantity(
                    item.getQuantity()
                    +request.quantity()
            );

            cartItemRepository.save(item);

            return;
        }

        CartItem item =
                CartItem.builder()
                        .productId(product.id())
                        .productName(product.name())
                        .price(product.price())
                        .quantity(request.quantity())
                        .cart(cart)
                        .build();

        cartItemRepository.save(item);
    }

    public CartResponse getCart(){

        String email = currentUserUtil.getCurrentUserEmail();

        Cart cart = cartRepository.findByUserEmail(email).
                orElseThrow(() -> new RuntimeException(
                        "Cart not found"
                ));

        List<CartItem> items = cartItemRepository
                .findByCartId(cart.getId());

        List<CartItemResponse> itemResponses =
                items.stream()
                        .map(item ->
                                new CartItemResponse(
                                    item.getProductId(),
                                    item.getProductName(),
                                    item.getPrice(),
                                    item.getQuantity()
                                )).toList();

        BigDecimal totalAmount = items.stream()
                .map(item -> item.getPrice()
                        .multiply(
                                BigDecimal.valueOf(
                                        item.getQuantity()
                                )
                        ))
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        return new CartResponse(
                cart.getId(),
                cart.getUserEmail(),
                itemResponses,
                totalAmount
        );
    }

    public void updateQuantity(Long productId, Integer quantity){

        String email = currentUserUtil.getCurrentUserEmail();

        Cart cart = cartRepository.findByUserEmail(email)
                .orElseThrow(()-> new RuntimeException("Cart not found"));

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(
                        cart.getId(),
                        productId
                ).orElseThrow(
                        () -> new RuntimeException(
                                "Item not found"
                        )
                );

        item.setQuantity(quantity);

        cartItemRepository.save(item);

    }

    public void removeItem(Long productId){

        String email = currentUserUtil
                .getCurrentUserEmail();

        Cart cart = cartRepository.findByUserEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Cart not found"
                        )
                );

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(
                        cart.getId(),
                        productId
                ).orElseThrow(
                        () -> new RuntimeException(
                                "Item not found"
                        )
                );

        cartItemRepository.delete(item);
    }

    public void clearCart() {
        String email = currentUserUtil
                .getCurrentUserEmail();

        Cart cart = cartRepository
                .findByUserEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Cart not found"
                        )
                );

        cartItemRepository.
                deleteAllByCartId(
                        cart.getId()
                );
    }

}
