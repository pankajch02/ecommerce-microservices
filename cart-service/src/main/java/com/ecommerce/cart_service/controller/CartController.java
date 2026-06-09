package com.ecommerce.cart_service.controller;

import com.ecommerce.cart_service.dto.AddToCartRequest;
import com.ecommerce.cart_service.dto.CartResponse;
import com.ecommerce.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestBody
            AddToCartRequest request
    ){
        cartService.addToCart(request);

        return ResponseEntity.ok(
                "Product added to cart"
        );
    }

    @GetMapping
    public CartResponse getCart(){
        return cartService.getCart();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<String> updateQuantity(
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ){
        cartService.updateQuantity(
                productId,
                quantity
        );

        return ResponseEntity.ok(
                "Quantity updated"
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeItem(
            @PathVariable Long productId
    ){
        cartService.removeItem(productId);

        return ResponseEntity.ok(
                "Item removed"
        );
    }

    @DeleteMapping
    public ResponseEntity<String> clearCart(){

        cartService.clearCart();

        return ResponseEntity.ok(
                "Cart cleared"
        );
    }
}
