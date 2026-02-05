package com.example.creditcard.service;

import com.example.creditcard.domain.CardTransaction;
import com.example.creditcard.domain.CreditCard;
import com.example.creditcard.domain.TransactionType;
import com.example.creditcard.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardService cardService;

    public CardTransaction authorizeTransaction(Long cardId, BigDecimal amount) {
        CreditCard card = cardService.getCardById(cardId);

        if (card.getAvailableLimit().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient available limit. Available: " + card.getAvailableLimit() + ", Requested: " + amount);
        }

        card.setAvailableLimit(card.getAvailableLimit().subtract(amount));
        cardService.saveCard(card);

        CardTransaction transaction = CardTransaction.builder()
                .card(card)
                .amount(amount)
                .type(TransactionType.AUTHORIZED)
                .build();

        return transactionRepository.save(transaction);
    }

    public CardTransaction captureTransaction(Long transactionId) {
        CardTransaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found with id: " + transactionId));

        if (transaction.getType() != TransactionType.AUTHORIZED) {
            throw new IllegalArgumentException("Only AUTHORIZED transactions can be captured");
        }

        transaction.setType(TransactionType.CAPTURED);
        return transactionRepository.save(transaction);
    }

    public BigDecimal getTotalCapturedAmount(Long cardId) {
        return transactionRepository.sumByCardIdAndType(cardId, TransactionType.CAPTURED);
    }
}
