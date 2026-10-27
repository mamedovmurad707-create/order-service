package com.example.orderservice.repository;

import com.example.orderservice.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {
    Page<OrderEntity> findByUserId(Long id, Pageable pageable);
}
