package com.example.creditcard.service;

import com.example.creditcard.domain.CreditCard;
import com.example.creditcard.domain.CardTransaction;
import com.example.creditcard.domain.TransactionType;
import com.example.creditcard.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionService Tests")
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CardService cardService;

    @InjectMocks
    private TransactionService transactionService;

    private CreditCard testCard;
    private CardTransaction testTransaction;

    @BeforeEach
    void setUp() {
        testCard = CreditCard.builder()
                .id(1L)
                .cardNumber("4532015112830366")
                .creditLimit(new BigDecimal("10000.00"))
                .availableLimit(new BigDecimal("10000.00"))
                .build();

        testTransaction = CardTransaction.builder()
                .id(1L)
                .card(testCard)
                .amount(new BigDecimal("100.00"))
                .type(TransactionType.AUTHORIZED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should authorize transaction successfully")
    void testAuthorizeTransactionSuccess() {
        // Arrange
        BigDecimal amount = new BigDecimal("100.00");
        when(cardService.getCardById(1L)).thenReturn(testCard);
        when(cardService.saveCard(any(CreditCard.class))).thenReturn(testCard);
        when(transactionRepository.save(any(CardTransaction.class))).thenReturn(testTransaction);

        // Act
        CardTransaction result = transactionService.authorizeTransaction(1L, amount);

        // Assert
        assertNotNull(result);
        assertEquals(TransactionType.AUTHORIZED, result.getType());
        assertEquals(amount, result.getAmount());
        assertEquals(testCard.getId(), result.getCard().getId());
        verify(cardService, times(1)).getCardById(1L);
        verify(cardService, times(1)).saveCard(any(CreditCard.class));
        verify(transactionRepository, times(1)).save(any(CardTransaction.class));
    }

    @Test
    @DisplayName("Should reduce available limit when authorizing transaction")
    void testAuthorizeTransactionReducesLimit() {
        // Arrange
        BigDecimal amount = new BigDecimal("500.00");
        BigDecimal expectedLimit = testCard.getAvailableLimit().subtract(amount);

        when(cardService.getCardById(1L)).thenReturn(testCard);
        when(cardService.saveCard(any(CreditCard.class))).thenAnswer(invocation -> {
            CreditCard card = invocation.getArgument(0);
            return card;
        });
        when(transactionRepository.save(any(CardTransaction.class))).thenReturn(testTransaction);

        // Act
        transactionService.authorizeTransaction(1L, amount);

        // Assert
        verify(cardService).saveCard(argThat(card -> 
            card.getAvailableLimit().equals(expectedLimit)
        ));
    }

    @Test
    @DisplayName("Should throw exception when insufficient available limit")
    void testAuthorizeTransactionInsufficientLimit() {
        // Arrange
        BigDecimal amount = new BigDecimal("15000.00"); // Greater than available limit
        when(cardService.getCardById(1L)).thenReturn(testCard);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> transactionService.authorizeTransaction(1L, amount));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should capture authorized transaction successfully")
    void testCaptureTransactionSuccess() {
        // Arrange
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(testTransaction));
        when(transactionRepository.save(any(CardTransaction.class))).thenReturn(testTransaction);

        // Act
        CardTransaction result = transactionService.captureTransaction(1L);

        // Assert
        assertNotNull(result);
        assertEquals(TransactionType.CAPTURED, result.getType());
        verify(transactionRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).save(any(CardTransaction.class));
    }

    @Test
    @DisplayName("Should throw exception when capturing non-existent transaction")
    void testCaptureTransactionNotFound() {
        // Arrange
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, 
            () -> transactionService.captureTransaction(999L));
    }

    @Test
    @DisplayName("Should throw exception when capturing non-authorized transaction")
    void testCaptureNonAuthorizedTransaction() {
        // Arrange
        CardTransaction capturedTransaction = CardTransaction.builder()
                .id(2L)
                .card(testCard)
                .amount(new BigDecimal("100.00"))
                .type(TransactionType.CAPTURED)
                .createdAt(LocalDateTime.now())
                .build();

        when(transactionRepository.findById(2L)).thenReturn(Optional.of(capturedTransaction));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> transactionService.captureTransaction(2L));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return total captured amount for card")
    void testGetTotalCapturedAmount() {
        // Arrange
        BigDecimal expectedTotal = new BigDecimal("500.00");
        when(transactionRepository.sumByCardIdAndType(1L, TransactionType.CAPTURED))
                .thenReturn(expectedTotal);

        // Act
        BigDecimal result = transactionService.getTotalCapturedAmount(1L);

        // Assert
        assertEquals(expectedTotal, result);
        verify(transactionRepository, times(1)).sumByCardIdAndType(1L, TransactionType.CAPTURED);
    }

    @Test
    @DisplayName("Should return zero when no captured transactions exist")
    void testGetTotalCapturedAmountZero() {
        // Arrange
        when(transactionRepository.sumByCardIdAndType(1L, TransactionType.CAPTURED))
                .thenReturn(BigDecimal.ZERO);

        // Act
        BigDecimal result = transactionService.getTotalCapturedAmount(1L);

        // Assert
        assertEquals(BigDecimal.ZERO, result);
    }
}
