package com.example.creditcard.service;

import com.example.creditcard.domain.CardTransaction;
import com.example.creditcard.domain.CreditCard;
import com.example.creditcard.domain.TransactionType;
import com.example.creditcard.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardService cardService;

    /**
     * Authorize a transaction with pessimistic locking.
     * 
     * Uses pessimistic write lock to prevent race conditions where multiple
     * concurrent requests could exceed the credit limit. Only one authorization
     * can proceed at a time for the same card.
     *
     * @param cardId the credit card ID
     * @param amount the transaction amount
     * @return the authorized transaction
     * @throws IllegalArgumentException if amount exceeds available limit
     * @throws NoSuchElementException if card not found
     */
    public CardTransaction authorizeTransaction(Long cardId, BigDecimal amount) {
        // Load card with pessimistic lock to prevent concurrent authorization race condition
        CreditCard card = cardService.getCardByIdForUpdate(cardId);

        if (card.getAvailableLimit().compareTo(amount) < 0) {
            log.warn("Authorization failed for cardId: {} - Insufficient available limit", cardId);
            throw new IllegalArgumentException("Insufficient available limit");
        }

        // Reserve the amount (reduce available limit)
        card.setAvailableLimit(card.getAvailableLimit().subtract(amount));
        cardService.saveCard(card);
        log.debug("Reserved amount {} for cardId: {}, new available limit: {}", 
            amount, cardId, card.getAvailableLimit());

        CardTransaction transaction = CardTransaction.builder()
                .card(card)
                .amount(amount)
                .type(TransactionType.AUTHORIZED)
                .build();

        CardTransaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction authorized - transactionId: {}, cardId: {}, amount: {}", 
            savedTransaction.getId(), cardId, amount);
        
        return savedTransaction;
    }

    /**
     * Capture a previously authorized transaction.
     * 
     * This finalizes the transaction. The available limit was already reduced
     * during authorization, so this operation only updates the transaction status.
     *
     * @param transactionId the transaction ID to capture
     * @return the captured transaction
     * @throws IllegalArgumentException if transaction is not in AUTHORIZED state
     * @throws NoSuchElementException if transaction not found
     */
    public CardTransaction captureTransaction(Long transactionId) {
        CardTransaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found with id: " + transactionId));

        if (transaction.getType() != TransactionType.AUTHORIZED) {
            log.warn("Capture failed for transactionId: {} - Transaction is not in AUTHORIZED state", transactionId);
            throw new IllegalArgumentException("Only AUTHORIZED transactions can be captured");
        }

        transaction.setType(TransactionType.CAPTURED);
        CardTransaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction captured - transactionId: {}, cardId: {}, amount: {}", 
            transactionId, transaction.getCard().getId(), transaction.getAmount());
        
        return savedTransaction;
    }

    /**
     * Get total captured amount for a card.
     * 
     * @param cardId the credit card ID
     * @return the sum of all captured transaction amounts
     */
    public BigDecimal getTotalCapturedAmount(Long cardId) {
        BigDecimal total = transactionRepository.sumByCardIdAndType(cardId, TransactionType.CAPTURED);
        return total != null ? total : BigDecimal.ZERO;
    }
}
