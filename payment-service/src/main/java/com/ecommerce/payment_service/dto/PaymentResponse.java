package com.ecommerce.payment_service.dto;

public record PaymentResponse(

        Long paymentId,

        Long orderId,

        String paymentStatus,

        String transactionId

) {
}
