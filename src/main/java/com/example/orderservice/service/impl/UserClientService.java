package com.example.orderservice.service.impl;

import com.example.orderservice.client.UserClient;
import com.example.orderservice.client.dto.UserResponse;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserClientService {
    private final UserClient userClient;

    // Aspect sırası: Retry (xaric) -> CircuitBreaker (daxil) -> Feign çağırışı.
    // Fallback Retry-dadır ki, bütün cəhdlər bitəndən sonra (və ya CB açıq olanda) bir dəfə işləsin.
    @Retry(name = "userServiceRetry", fallbackMethod = "getUserByIdFallback")
    @CircuitBreaker(name = "userServiceCircuitBreaker")
    public UserResponse getUserById(Long userId) {
        return userClient.getUserById(userId);
    }

    private UserResponse getUserByIdFallback(Long userId, Throwable exception) {
        // 4xx (məs. 404 user tapılmadı) sistem xətası deyil, olduğu kimi yuxarı ötürülür
        if (exception instanceof FeignException.FeignClientException clientError) {
            throw clientError;
        }
        log.error("User service is temporarily unavailable. userId={}", userId, exception);
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Sistemimiz müvəqqəti olaraq işləmir. Zəhmət olmasa, bir az sonra yenidən cəhd edin."
        );
    }
}
