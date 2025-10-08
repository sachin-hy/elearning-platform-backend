package com.example.project.service.Interface;

import com.example.project.dto.OrderResponse;
import com.example.project.dto.PaymentVerificationRequest;
import com.example.project.dto.PaymentVerificationResponse;

public interface PaymentServiceInterface {

    public OrderResponse createOrder(String cid, String email);
    public PaymentVerificationResponse verifyPayment(PaymentVerificationRequest request);

}
