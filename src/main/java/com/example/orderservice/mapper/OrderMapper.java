package com.example.orderservice.mapper;

import com.example.orderservice.entity.OrderEntity;
import com.example.orderservice.model.request.OrderRequest;
import com.example.orderservice.model.response.OrderResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderMapper {
    public OrderEntity toEntity(OrderRequest request){
        OrderEntity entity =new OrderEntity();
        entity.setUserId(request.getUserId());
        entity.setQuantity(request.getQuantity());
        entity.setProductName(request.getProductName());
        entity.setTotalPrice(
                request.getUnitPrice()
                        .multiply(BigDecimal.valueOf(request.getQuantity()))
        );
        entity.setUnitPrice(request.getUnitPrice());
        entity.setStatus("NEW");
        return entity;
    }

    public OrderResponse toResponse(OrderEntity entity){
        return new  OrderResponse(
                entity.getId(),
                entity.getUserId(),
                entity.getProductName(),
                entity.getQuantity(),
                entity.getUnitPrice(),
                entity.getTotalPrice(),
                entity.getStatus(),
                entity.getCreatedAt());
    }
}
