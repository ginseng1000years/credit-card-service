package com.example.creditcard.service;

import com.example.creditcard.domain.CreditCard;
import com.example.creditcard.repository.CardRepository;
import com.example.creditcard.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for concurrent transaction authorization.
 * 
 * These tests verify that pessimistic locking prevents race conditions
 * where multiple concurrent authorization requests could exceed the credit limit.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("TransactionService Concurrency Tests")
class TransactionServiceConcurrencyTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    private CreditCard testCard;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        cardRepository.deleteAll();

        testCard = CreditCard.builder()
                .cardNumber("4532015112830366")
                .creditLimit(new BigDecimal("10000.00"))
                .availableLimit(new BigDecimal("100.00"))
                .build();
        testCard = cardRepository.save(testCard);
    }

    @Test
    @DisplayName("Should prevent overbooking with concurrent authorization requests")
    void testConcurrentAuthorizationPreventsOverbooking() throws InterruptedException {
        // Arrange
        BigDecimal requestAmount = new BigDecimal("80.00");
        int concurrentRequests = 2;

        ExecutorService executor = Executors.newFixedThreadPool(concurrentRequests);
        CountDownLatch latch = new CountDownLatch(concurrentRequests);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        // Act: Send 2 concurrent requests for $80 each on a card with $100 available
        for (int i = 0; i < concurrentRequests; i++) {
            executor.submit(() -> {
                try {
                    transactionService.authorizeTransaction(testCard.getId(), requestAmount);
                    successCount.incrementAndGet();
                } catch (IllegalArgumentException e) {
                    // Expected: second request should fail with insufficient limit
                    failureCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        // Assert
        // Only 1 of 2 requests should succeed (the other should be rejected)
        assertEquals(1, successCount.get(), 
            "Only 1 of 2 concurrent authorization requests should succeed");
        assertEquals(1, failureCount.get(), 
            "Exactly 1 request should fail with insufficient limit");

        // Verify final state
        CreditCard updatedCard = cardRepository.findById(testCard.getId()).get();
        assertEquals(new BigDecimal("20.00"), updatedCard.getAvailableLimit(),
            "Available limit should be exactly $20.00 ($100 - $80)");
    }

    @Test
    @DisplayName("Should handle multiple concurrent requests maintaining limit invariant")
    void testMultipleConcurrentRequestsMaintainLimitInvariant() throws InterruptedException {
        // Arrange
        BigDecimal initialLimit = new BigDecimal("500.00");
        testCard.setAvailableLimit(initialLimit);
        testCard = cardRepository.save(testCard);

        BigDecimal requestAmount = new BigDecimal("100.00");
        int concurrentRequests = 6;

        ExecutorService executor = Executors.newFixedThreadPool(3);
        CountDownLatch latch = new CountDownLatch(concurrentRequests);
        AtomicInteger successCount = new AtomicInteger(0);

        // Act: Send 6 concurrent requests for $100 each on a card with $500 available
        // Only 5 should succeed, 1 should fail
        for (int i = 0; i < concurrentRequests; i++) {
            executor.submit(() -> {
                try {
                    transactionService.authorizeTransaction(testCard.getId(), requestAmount);
                    successCount.incrementAndGet();
                } catch (IllegalArgumentException e) {
                    // Expected: some requests will fail
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        // Assert
        assertEquals(5, successCount.get(), 
            "Exactly 5 of 6 concurrent requests should succeed");

        CreditCard updatedCard = cardRepository.findById(testCard.getId()).get();
        assertEquals(new BigDecimal("0.00"), updatedCard.getAvailableLimit(),
            "Available limit should be $0.00 after 5 successful $100 authorizations");

        // Verify transaction count
        long transactionCount = transactionRepository.count();
        assertEquals(5, transactionCount, 
            "Exactly 5 transactions should be created");
    }
}
