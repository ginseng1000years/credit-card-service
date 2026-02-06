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

    /**
     * Get a card summary including credit information and captured transaction totals.
     * 
     * Card numbers are masked for security (PCI-DSS compliance).
     *
     * @param cardId the card ID
     * @return a map containing card details and total captured amount
     */
    @GetMapping("/{cardId}/summary")
    public ResponseEntity<Map<String, Object>> getCardSummary(@PathVariable Long cardId) {
        CreditCard card = cardService.getCardById(cardId);
        BigDecimal totalCapturedAmount = transactionService.getTotalCapturedAmount(cardId);

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("cardId", card.getId());
        summary.put("cardNumber", maskCardNumber(card.getCardNumber()));
        summary.put("creditLimit", card.getCreditLimit());
        summary.put("availableLimit", card.getAvailableLimit());
        summary.put("totalCapturedAmount", totalCapturedAmount);

        return ResponseEntity.ok(summary);
    }

    /**
     * Mask a card number for security purposes.
     * 
     * Keeps the first 4 and last 4 digits, masks the middle.
     * Example: 4532015112830366 → 4532****0366
     *
     * @param cardNumber the full card number
     * @return the masked card number
     */
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 8) {
            return "****"; // Fallback for invalid input
        }
        // Replace 8 middle digits with 4 asterisks
        // For 16-digit card: XXXX(8 middle digits)XXXX → XXXX****XXXX
        return cardNumber.substring(0, 4) + "****" + cardNumber.substring(12);
    }
}

