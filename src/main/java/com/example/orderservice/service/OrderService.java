package com.example.orderservice.service;


import com.example.orderservice.model.request.OrderChangeStatusRequest;
import com.example.orderservice.model.request.OrderRequest;
import com.example.orderservice.model.request.OrderUpdateRequest;
import com.example.orderservice.model.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrderById(Long id);
    Page<OrderResponse> getAllOrder(Pageable pageable);
    Page<OrderResponse> getAllOrderByUserId(Long id ,Pageable pageable);
    OrderResponse updateOrderById(Long id , OrderUpdateRequest request);
    void  deleteById(Long id);
    String changeStatusById(Long id, OrderChangeStatusRequest request);
}
