package com.example.orderservice.service.impl;

import com.example.orderservice.client.UserClient;
import com.example.orderservice.client.dto.UserResponse;
import com.example.orderservice.constant.OrderStatus;
import com.example.orderservice.entity.OrderEntity;
import com.example.orderservice.exception.CustomerNotFoundException;
import com.example.orderservice.exception.InvalidOrderStatusException;
import com.example.orderservice.exception.OrderAlreadyExistsException;
import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.model.request.OrderChangeStatusRequest;
import com.example.orderservice.model.request.OrderRequest;
import com.example.orderservice.model.request.OrderUpdateRequest;
import com.example.orderservice.model.response.OrderResponse;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;


import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserClient userClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        log.info("OrderServiceImpl.createOrder.start");
        UserResponse userResponse;
        try {
            userResponse = userClient.getUserById(request.getUserId());
        } catch (FeignException.FeignClientException e) {
            log.error("OrderServiceImpl.createOrder.error userId: {}",request.getUserId());
            throw new CustomerNotFoundException(
                    "User not found id: " + request.getUserId()
            );
        }
        if (userResponse.statusId() != 1) {
            log.error("OrderServiceImpl.createOrder.error userId: {}, status {}",request.getUserId(),userResponse.statusId());
            throw new CustomerNotFoundException(
                    "User is not active id: " + request.getUserId()
            );
        }
        OrderEntity entity = orderMapper.toEntity(request);
        var dbEntity = orderRepository.save(entity);
        log.info("OrderServiceImpl.createOrder.success");
        return orderMapper.toResponse(dbEntity);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        log.info("OrderServiceImpl.getOrderById.start id{}",id);
        OrderEntity orderEntity = orderRepository.findById(id).orElseThrow(
                () -> {
                    log.error("OrderServiceImpl.getOrderById.error id{}",id);
                    return new OrderNotFoundException("Order not found by id: " + id);
                });
        log.info("OrderServiceImpl.getOrderById.success id{}",id);
        return orderMapper.toResponse(orderEntity);
    }

    @Override
    public Page<OrderResponse> getAllOrder(Pageable pageable) {
        log.info("OrderServiceImpl.getAllOrder.start ");
        return orderRepository.findAll(pageable)
                .map(orderMapper::toResponse);
    }

    @Override
    public Page<OrderResponse> getAllOrderByUserId(Long id, Pageable pageable) {
        log.info("OrderServiceImpl.getAllOrderByUserId.start ");
        return orderRepository.findByUserId(id, pageable)
                .map(orderMapper::toResponse);
    }

    @Override
    public OrderResponse updateOrderById(Long id, OrderUpdateRequest request) {
        log.info("OrderServiceImpl.updateOrderById.start id{}",id);
        OrderEntity entity = orderRepository.findById(id).orElseThrow(() ->
        {
            log.error("OrderServiceImpl.updateOrderById.error.id{} ,body{}",id,request);
          return   new OrderNotFoundException("Order not found id: " + id);
        });
        if (entity.getStatus() != OrderStatus.NEW) {
            log.error("OrderServiceImpl.updateOrderById.error.id{}, status{}",id,entity.getStatus());
            throw new InvalidOrderStatusException("Only NEW orders can be updated");
        }
        entity.setProductName(request.getProductName());
        entity.setQuantity(request.getQuantity());
        entity.setUnitPrice(request.getUnitPrice());
        entity.setTotalPrice(
                request.getUnitPrice()
                        .multiply(BigDecimal.valueOf(request.getQuantity()))
        );
        orderRepository.save(entity);
        log.info("OrderServiceImpl.updateOrderById.success id{}",id);
        return orderMapper.toResponse(entity);
    }

    @Override
    public void deleteById(Long id) {
        log.info("OrderServiceImpl.deleteById.start id {}", id);
        OrderEntity entity = orderRepository.findById(id).orElseThrow(() ->
        {
            log.error("OrderServiceImpl.deleteById.start id {}", id);
       return   new OrderNotFoundException("Order not found by id: " + id);
    });
        entity.setStatus(OrderStatus.CANCELED);
        orderRepository.save(entity);
        log.info("OrderServiceImpl.deleteById.success id{}",id);

    }

    @Override
    public String changeStatusById(Long id, OrderChangeStatusRequest request) {
        log.info("OrderServiceImpl.changeStatusById.start id{} and status{}",id,request.getStatus());

        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("OrderServiceImpl.changeStatusById.error id{} ",id);
           return new OrderNotFoundException("Order not found by id: " + id);
        });
        if (orderEntity.getStatus() != OrderStatus.NEW) {
            throw new InvalidOrderStatusException("Only NEW status order can changed");
        }
        if (request.getStatus() == OrderStatus.NEW) {
            throw new OrderAlreadyExistsException("Order already has a NEW status");
        }
        orderEntity.setStatus(request.getStatus());
        orderRepository.save(orderEntity);
        log.info("OrderServiceImpl.changeStatusById.success id{} and status{}",id,request.getStatus());
        return orderEntity.getStatus().toString();
    }
}
