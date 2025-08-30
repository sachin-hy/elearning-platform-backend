package com.example.project.dto;

public record OrderResponse(String orderId, String currency, Double amount, String keyId) {

}
