package com.example.creditcard.controller;

import com.example.creditcard.domain.CreditCard;
import com.example.creditcard.domain.CardTransaction;
import com.example.creditcard.domain.TransactionType;
import com.example.creditcard.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@DisplayName("TransactionController Tests")
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreditCard testCard;
    private CardTransaction authorizedTransaction;
    private CardTransaction capturedTransaction;

    @BeforeEach
    void setUp() {
        testCard = CreditCard.builder()
                .id(1L)
                .cardNumber("4532015112830366")
                .creditLimit(new BigDecimal("10000.00"))
                .availableLimit(new BigDecimal("9900.00"))
                .build();

        authorizedTransaction = CardTransaction.builder()
                .id(1L)
                .card(testCard)
                .amount(new BigDecimal("100.00"))
                .type(TransactionType.AUTHORIZED)
                .createdAt(LocalDateTime.now())
                .build();

        capturedTransaction = CardTransaction.builder()
                .id(1L)
                .card(testCard)
                .amount(new BigDecimal("100.00"))
                .type(TransactionType.CAPTURED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should authorize transaction successfully")
    void testAuthorizeTransactionSuccess() throws Exception {
        // Arrange
        TransactionController.AuthorizeRequest request = new TransactionController.AuthorizeRequest(1L, new BigDecimal("100.00"));
        when(transactionService.authorizeTransaction(1L, new BigDecimal("100.00")))
                .thenReturn(authorizedTransaction);

        // Act & Assert
        mockMvc.perform(post("/transactions/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value(1))
                .andExpect(jsonPath("$.cardId").value(1))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.type").value("AUTHORIZED"));
    }

    @Test
    @DisplayName("Should return 400 when authorization fails due to insufficient limit")
    void testAuthorizeTransactionInsufficientLimit() throws Exception {
        // Arrange
        TransactionController.AuthorizeRequest request = new TransactionController.AuthorizeRequest(1L, new BigDecimal("15000.00"));
        when(transactionService.authorizeTransaction(anyLong(), any()))
                .thenThrow(new IllegalArgumentException("Insufficient available limit"));

        // Act & Assert
        mockMvc.perform(post("/transactions/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should capture transaction successfully")
    void testCaptureTransactionSuccess() throws Exception {
        // Arrange
        when(transactionService.captureTransaction(1L))
                .thenReturn(capturedTransaction);

        // Act & Assert
        mockMvc.perform(post("/transactions/capture/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(1))
                .andExpect(jsonPath("$.cardId").value(1))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.type").value("CAPTURED"));
    }

    @Test
    @DisplayName("Should return 404 when capturing non-existent transaction")
    void testCaptureTransactionNotFound() throws Exception {
        // Arrange
        when(transactionService.captureTransaction(anyLong()))
                .thenThrow(new java.util.NoSuchElementException("Transaction not found"));

        // Act & Assert
        mockMvc.perform(post("/transactions/capture/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 when capturing non-authorized transaction")
    void testCaptureNonAuthorizedTransaction() throws Exception {
        // Arrange
        when(transactionService.captureTransaction(1L))
                .thenThrow(new IllegalArgumentException("Only AUTHORIZED transactions can be captured"));

        // Act & Assert
        mockMvc.perform(post("/transactions/capture/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle different transaction amounts")
    void testAuthorizeWithDifferentAmounts() throws Exception {
        // Arrange
        BigDecimal amount = new BigDecimal("500.00");
        CardTransaction transaction = CardTransaction.builder()
                .id(2L)
                .card(testCard)
                .amount(amount)
                .type(TransactionType.AUTHORIZED)
                .createdAt(LocalDateTime.now())
                .build();

        TransactionController.AuthorizeRequest request = new TransactionController.AuthorizeRequest(1L, amount);
        when(transactionService.authorizeTransaction(1L, amount))
                .thenReturn(transaction);

        // Act & Assert
        mockMvc.perform(post("/transactions/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(500.00));
    }
}
