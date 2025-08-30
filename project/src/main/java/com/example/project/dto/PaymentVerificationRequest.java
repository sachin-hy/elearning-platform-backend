package com.example.project.dto;

import java.util.Map;

public record PaymentVerificationRequest(String razorpayOrderId,String razorpayPaymentId,String razorpaySignature) {

}
