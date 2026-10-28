package com.example.orderservice.model.request;

import com.example.orderservice.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderChangeStatusRequest {
    private OrderStatus status;
}
