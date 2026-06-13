package com.ecommerce.payment_service.dto;

public record CreatePaymentRequest(

        Long orderId,

        String paymentMethod

) {
}
