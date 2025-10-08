package com.example.project.service.Interface;

import com.example.project.dto.PaymentVerificationRequest;
import com.razorpay.Order;

public interface RazorpayServiceInterface {

    public Order createRazorpayOrder(Long courseid, int coursePrice, String email) throws Exception;
    public boolean verifyPaymentSignature(PaymentVerificationRequest request);
    public String getKeyId();

}
