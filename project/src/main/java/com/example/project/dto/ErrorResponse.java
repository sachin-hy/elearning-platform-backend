package com.example.project.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public record ErrorResponse(String message, HttpStatus status, LocalDateTime timestamp) {

}
