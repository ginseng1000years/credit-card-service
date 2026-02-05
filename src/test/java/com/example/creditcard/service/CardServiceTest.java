package com.example.creditcard.service;

import com.example.creditcard.domain.CreditCard;
import com.example.creditcard.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CardService Tests")
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    private CreditCard testCard;

    @BeforeEach
    void setUp() {
        testCard = CreditCard.builder()
                .id(1L)
                .cardNumber("4532015112830366")
                .creditLimit(new BigDecimal("10000.00"))
                .availableLimit(new BigDecimal("10000.00"))
                .build();
    }

    @Test
    @DisplayName("Should return card when card exists")
    void testGetCardByIdSuccess() {
        // Arrange
        when(cardRepository.findById(1L)).thenReturn(Optional.of(testCard));

        // Act
        CreditCard result = cardService.getCardById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("4532015112830366", result.getCardNumber());
        assertEquals(new BigDecimal("10000.00"), result.getCreditLimit());
        verify(cardRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when card not found")
    void testGetCardByIdNotFound() {
        // Arrange
        when(cardRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> cardService.getCardById(999L));
        verify(cardRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should save card successfully")
    void testSaveCardSuccess() {
        // Arrange
        when(cardRepository.save(any(CreditCard.class))).thenReturn(testCard);

        // Act
        CreditCard result = cardService.saveCard(testCard);

        // Assert
        assertNotNull(result);
        assertEquals(testCard.getId(), result.getId());
        assertEquals(testCard.getCardNumber(), result.getCardNumber());
        verify(cardRepository, times(1)).save(testCard);
    }

    @Test
    @DisplayName("Should save card with correct data")
    void testSaveCardWithData() {
        // Arrange
        CreditCard newCard = CreditCard.builder()
                .cardNumber("5432109876543210")
                .creditLimit(new BigDecimal("20000.00"))
                .availableLimit(new BigDecimal("20000.00"))
                .build();

        when(cardRepository.save(any(CreditCard.class))).thenReturn(newCard);

        // Act
        CreditCard result = cardService.saveCard(newCard);

        // Assert
        assertNotNull(result);
        assertEquals("5432109876543210", result.getCardNumber());
        assertEquals(new BigDecimal("20000.00"), result.getCreditLimit());
    }
}
