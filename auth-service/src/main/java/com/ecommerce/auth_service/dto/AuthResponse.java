package com.ecommerce.auth_service.dto;

public record AuthResponse(
        String token,
        String email,
        String role
) {
}
