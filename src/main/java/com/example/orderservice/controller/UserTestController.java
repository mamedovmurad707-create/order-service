package com.example.orderservice.controller;

import com.example.orderservice.client.UserClient;
import com.example.orderservice.client.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/test/user")
@RequiredArgsConstructor
public class UserTestController {
    private final UserClient client;
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id){
       return client.getUserById(id);
    }
}
