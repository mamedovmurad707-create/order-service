package com.example.orderservice.model.request;

import com.example.orderservice.constant.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderChangeStatusRequest {
    @NotBlank
    private OrderStatus status;
}
