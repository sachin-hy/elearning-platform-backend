package com.example.project.dto;

public record ResetPasswordDto(
    String password,
    String confirmPassword,
    String token
) {}
