package com.example.creditcard.controller;

import com.example.creditcard.domain.CardTransaction;
import com.example.creditcard.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Authorize a credit card transaction.
     * 
     * Reserves the transaction amount from the card's available limit.
     * Authorization must complete before capture.
     *
     * @param request the authorization request containing cardId and amount
     * @return the created transaction with AUTHORIZED status
     */
    @PostMapping("/authorize")
    public ResponseEntity<Map<String, Object>> authorize(@Valid @RequestBody AuthorizeRequest request) {
        CardTransaction transaction = transactionService.authorizeTransaction(request.getCardId(), request.getAmount());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("transactionId", transaction.getId());
        response.put("cardId", transaction.getCard().getId());
        response.put("amount", transaction.getAmount());
        response.put("type", transaction.getType());
        response.put("createdAt", transaction.getCreatedAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Capture a previously authorized transaction.
     * 
     * Finalizes the transaction, changing status from AUTHORIZED to CAPTURED.
     *
     * @param transactionId the ID of the transaction to capture
     * @return the captured transaction with CAPTURED status
     */
    @PostMapping("/capture/{transactionId}")
    public ResponseEntity<Map<String, Object>> capture(@PathVariable Long transactionId) {
        CardTransaction transaction = transactionService.captureTransaction(transactionId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("transactionId", transaction.getId());
        response.put("cardId", transaction.getCard().getId());
        response.put("amount", transaction.getAmount());
        response.put("type", transaction.getType());
        response.put("createdAt", transaction.getCreatedAt());

        return ResponseEntity.ok(response);
    }

    /**
     * Request DTO for transaction authorization.
     * 
     * All fields are validated:
     * - cardId must be positive
     * - amount must be positive and at least 0.01
     */
    public static class AuthorizeRequest {
        @NotNull(message = "cardId is required")
        @Positive(message = "cardId must be positive")
        private Long cardId;

        @NotNull(message = "amount is required")
        @DecimalMin(value = "0.01", message = "amount must be at least 0.01")
        private BigDecimal amount;

        public AuthorizeRequest() {
        }

        public AuthorizeRequest(Long cardId, BigDecimal amount) {
            this.cardId = cardId;
            this.amount = amount;
        }

        public Long getCardId() {
            return cardId;
        }

        public void setCardId(Long cardId) {
            this.cardId = cardId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }
}
