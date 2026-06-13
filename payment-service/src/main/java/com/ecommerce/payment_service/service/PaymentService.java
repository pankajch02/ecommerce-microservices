package com.ecommerce.payment_service.service;

import com.ecommerce.payment_service.dto.CreatePaymentRequest;
import com.ecommerce.payment_service.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse createPayment(
            CreatePaymentRequest request);

    List<PaymentResponse> getMyPayments();

    PaymentResponse getPaymentById(
            Long paymentId);
}
