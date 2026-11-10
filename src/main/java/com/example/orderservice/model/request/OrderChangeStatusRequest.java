package com.example.orderservice.model.request;

import com.example.orderservice.constant.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderChangeStatusRequest {
    @NotNull
    private OrderStatus status;
}
