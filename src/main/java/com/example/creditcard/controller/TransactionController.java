package com.example.creditcard.controller;

import com.example.creditcard.domain.CardTransaction;
import com.example.creditcard.service.TransactionService;
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

    @PostMapping("/authorize")
    public ResponseEntity<Map<String, Object>> authorize(@RequestBody AuthorizeRequest request) {
        CardTransaction transaction = transactionService.authorizeTransaction(request.getCardId(), request.getAmount());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("transactionId", transaction.getId());
        response.put("cardId", transaction.getCard().getId());
        response.put("amount", transaction.getAmount());
        response.put("type", transaction.getType());
        response.put("createdAt", transaction.getCreatedAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

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

    public static class AuthorizeRequest {
        private Long cardId;
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
