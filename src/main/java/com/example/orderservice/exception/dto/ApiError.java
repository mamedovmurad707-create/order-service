package com.example.orderservice.exception.dto;

import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class ApiError {
    Instant timestamp;
    int status;
    String error;
    String message;
    String path;
    Map<String,String> validationErrors;
}
