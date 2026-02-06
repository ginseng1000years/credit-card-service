package com.example.creditcard.controller;

import com.example.creditcard.domain.CreditCard;
import com.example.creditcard.service.CardService;
import com.example.creditcard.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
@DisplayName("CardController Tests")
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @MockBean
    private TransactionService transactionService;

    private CreditCard testCard;

    @BeforeEach
    void setUp() {
        testCard = CreditCard.builder()
                .id(1L)
                .cardNumber("4532015112830366")
                .creditLimit(new BigDecimal("10000.00"))
                .availableLimit(new BigDecimal("9900.00"))
                .build();
    }

    @Test
    @DisplayName("Should return card summary successfully")
    void testGetCardSummarySuccess() throws Exception {
        // Arrange
        when(cardService.getCardById(1L)).thenReturn(testCard);
        when(transactionService.getTotalCapturedAmount(1L)).thenReturn(new BigDecimal("100.00"));

        // Act & Assert
        mockMvc.perform(get("/cards/1/summary")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardId").value(1))
                .andExpect(jsonPath("$.cardNumber").value("4532****0366"))
                .andExpect(jsonPath("$.creditLimit").value(10000.00))
                .andExpect(jsonPath("$.availableLimit").value(9900.00))
                .andExpect(jsonPath("$.totalCapturedAmount").value(100.00));
    }

    @Test
    @DisplayName("Should return 404 when card not found")
    void testGetCardSummaryNotFound() throws Exception {
        // Arrange
        when(cardService.getCardById(anyLong())).thenThrow(new java.util.NoSuchElementException("Card not found"));

        // Act & Assert
        mockMvc.perform(get("/cards/999/summary")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return zero total captured amount when no transactions captured")
    void testGetCardSummaryNoTransactions() throws Exception {
        // Arrange
        when(cardService.getCardById(1L)).thenReturn(testCard);
        when(transactionService.getTotalCapturedAmount(1L)).thenReturn(BigDecimal.ZERO);

        // Act & Assert
        mockMvc.perform(get("/cards/1/summary")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCapturedAmount").value(0.00));
    }

    @Test
    @DisplayName("Should mask card number in response for security")
    void testCardNumberMaskedInResponse() throws Exception {
        // Arrange
        when(cardService.getCardById(1L)).thenReturn(testCard);
        when(transactionService.getTotalCapturedAmount(1L)).thenReturn(BigDecimal.ZERO);

        // Act & Assert - Verify card number is masked (format: 4532****0366)
        mockMvc.perform(get("/cards/1/summary")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value("4532****0366"));
    }
}
