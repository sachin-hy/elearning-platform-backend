package com.example.project.dto;

public record RequestPasswordUpdateDto(
    String oldPassword,
    String newPassword,
    String confirmedNewPassword
) {}
