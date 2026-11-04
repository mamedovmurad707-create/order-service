package com.example.orderservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ResilienceEventLogger {

    private final RetryRegistry retryRegistry;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @PostConstruct
    void registerListeners() {
        retryRegistry.getAllRetries().forEach(this::attach);
        retryRegistry.getEventPublisher().onEntryAdded(e -> attach(e.getAddedEntry()));

        circuitBreakerRegistry.getAllCircuitBreakers().forEach(this::attach);
        circuitBreakerRegistry.getEventPublisher().onEntryAdded(e -> attach(e.getAddedEntry()));
    }

    private void attach(Retry retry) {
        retry.getEventPublisher()
                .onRetry(e -> log.warn("Retry [{}] attempt #{} after {} -> {}",
                        e.getName(), e.getNumberOfRetryAttempts(), e.getWaitInterval(),
                        String.valueOf(e.getLastThrowable())))
                .onError(e -> log.error("Retry [{}] exhausted after {} retries -> {}",
                        e.getName(), e.getNumberOfRetryAttempts(),
                        String.valueOf(e.getLastThrowable())))
                .onSuccess(e -> log.info("Retry [{}] succeeded after {} retries",
                        e.getName(), e.getNumberOfRetryAttempts()));
    }

    private void attach(CircuitBreaker cb) {
        cb.getEventPublisher()
                .onStateTransition(e -> log.warn("CircuitBreaker [{}] state {} -> {}",
                        e.getCircuitBreakerName(),
                        e.getStateTransition().getFromState(),
                        e.getStateTransition().getToState()));
    }
}
