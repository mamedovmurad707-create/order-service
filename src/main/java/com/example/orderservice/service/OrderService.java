package com.example.orderservice.service;

import com.example.orderservice.model.request.OrderRequest;
import com.example.orderservice.model.response.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
}
