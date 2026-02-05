package com.example.creditcard.controller;

import com.example.creditcard.domain.CreditCard;
import com.example.creditcard.service.CardService;
import com.example.creditcard.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final TransactionService transactionService;

    @GetMapping("/{cardId}/summary")
    public ResponseEntity<Map<String, Object>> getCardSummary(@PathVariable Long cardId) {
        CreditCard card = cardService.getCardById(cardId);
        BigDecimal totalCapturedAmount = transactionService.getTotalCapturedAmount(cardId);

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("cardId", card.getId());
        summary.put("cardNumber", card.getCardNumber());
        summary.put("creditLimit", card.getCreditLimit());
        summary.put("availableLimit", card.getAvailableLimit());
        summary.put("totalCapturedAmount", totalCapturedAmount);

        return ResponseEntity.ok(summary);
    }
}
