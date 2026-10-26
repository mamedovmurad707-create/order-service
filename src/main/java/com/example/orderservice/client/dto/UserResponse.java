package com.example.orderservice.client.dto;

public record UserResponse(
        Long id,
        String name,
        String phone,
        String email,
        Integer age,
        Integer statusId
) {
}
