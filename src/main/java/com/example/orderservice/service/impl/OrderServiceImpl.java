package com.example.orderservice.service.impl;

import com.example.orderservice.client.UserClient;
import com.example.orderservice.client.dto.UserResponse;
import com.example.orderservice.entity.OrderEntity;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.model.request.OrderRequest;
import com.example.orderservice.model.response.OrderResponse;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl  implements OrderService {

    private final UserClient userClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        UserResponse userResponse;
        try {
            userResponse =userClient.getUserById(request.getUserId());
        }catch (FeignException.FeignClientException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found"
            );
        }
        if (userResponse.statusId()!=1){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User is not active"
            );
        }
        OrderEntity entity = orderMapper.toEntity(request);
        var dbEntity = orderRepository.save(entity);
        return orderMapper.toResponse(dbEntity);
    }
}
