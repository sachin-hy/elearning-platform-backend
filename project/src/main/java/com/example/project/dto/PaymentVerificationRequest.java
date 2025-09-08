package com.example.project.dto;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;

public record PaymentVerificationRequest(
		@NotBlank(message = "The OrderId can Not be Null")
		String razorpayOrderId,
		@NotBlank(message = "PaymentId Can Not Be Null")
		String razorpayPaymentId,
		@NotBlank(message = "The Signature can not be null")
		String razorpaySignature) {

}
