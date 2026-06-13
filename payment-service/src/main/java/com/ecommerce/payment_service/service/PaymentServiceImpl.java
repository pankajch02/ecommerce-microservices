package com.ecommerce.payment_service.service;

import com.ecommerce.payment_service.client.OrderClient;
import com.ecommerce.payment_service.config.CurrentUserUtil;
import com.ecommerce.payment_service.dto.CreatePaymentRequest;
import com.ecommerce.payment_service.dto.OrderResponse;
import com.ecommerce.payment_service.dto.PaymentResponse;
import com.ecommerce.payment_service.dto.UpdateOrderStatusRequest;
import com.ecommerce.payment_service.entity.OrderStatus;
import com.ecommerce.payment_service.entity.Payment;
import com.ecommerce.payment_service.entity.PaymentStatus;
import com.ecommerce.payment_service.exception.PaymentAlreadyExistsException;
import com.ecommerce.payment_service.exception.PaymentNotFoundException;
import com.ecommerce.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService{
    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;

    private final CurrentUserUtil currentUserUtil;

    @Override
    @Transactional
    public PaymentResponse createPayment(
            CreatePaymentRequest request) {

        paymentRepository
                .findByOrderId(
                        request.orderId())
                .ifPresent(payment -> {
                    throw new PaymentAlreadyExistsException(
                            "Payment already exists for this order");
                });

        log.info(
                "Initiating payment for order={}",
                request.orderId());

        String userEmail =
                currentUserUtil.getCurrentUserEmail();

        String token =
                ((ServletRequestAttributes)
                        RequestContextHolder
                                .getRequestAttributes())
                        .getRequest()
                        .getHeader("Authorization");

        OrderResponse order =
                orderClient.getOrderById(
                        request.orderId()
                ,token);

        Payment payment =
                Payment.builder()
                        .orderId(order.orderId())
                        .userEmail(userEmail)
                        .amount(order.totalAmount())
                        .paymentMethod(
                                request.paymentMethod())
                        .paymentStatus(
                                PaymentStatus.PENDING)
                        .createdAt(
                                LocalDateTime.now())
                        .transactionId(
                                UUID.randomUUID()
                                        .toString())
                        .build();

        boolean paymentSuccess =
                processPayment();

        if (paymentSuccess) {
            log.info(
                    "Payment successful for order={}",
                    request.orderId());

            payment.setPaymentStatus(
                    PaymentStatus.SUCCESS);

            orderClient.updateOrderStatus(
                    order.orderId(),
                    new UpdateOrderStatusRequest(
                        OrderStatus.PAID),
                    token);

        } else {
            log.warn(
                    "Payment failed for order={}",
                    request.orderId());

            payment.setPaymentStatus(
                    PaymentStatus.FAILED);

            orderClient.updateOrderStatus(
                    order.orderId(),
                    new UpdateOrderStatusRequest(
                            OrderStatus.CANCELLED)
            ,token);
        }

        payment = paymentRepository.save(payment);

        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getPaymentStatus().name(),
                payment.getTransactionId()
        );
    }

    private boolean processPayment() {

        return Math.random() > 0.2;
    }

    @Override
    public List<PaymentResponse> getMyPayments() {

        String email =
                currentUserUtil
                        .getCurrentUserEmail();

        return paymentRepository
                .findByUserEmail(email)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public PaymentResponse getPaymentById(
            Long paymentId) {

        String email =
                currentUserUtil
                        .getCurrentUserEmail();

        Payment payment =
                paymentRepository
                        .findById(paymentId)
                        .orElseThrow(
                                () ->
                                        new PaymentNotFoundException(
                                                "Payment not found"));

        if(!payment.getUserEmail().equals(email)) {

            throw new PaymentNotFoundException(
                    "Payment not found");
        }

        return mapToResponse(payment);
    }

    private PaymentResponse mapToResponse(
            Payment payment) {

        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getPaymentStatus().name(),
                payment.getTransactionId()
        );
    }

}
