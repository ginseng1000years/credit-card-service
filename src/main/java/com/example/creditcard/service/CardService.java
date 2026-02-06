package com.example.creditcard.service;

import com.example.creditcard.domain.CreditCard;
import com.example.creditcard.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CardService {

    private final CardRepository cardRepository;

    /**
     * Get a card by ID. Standard read operation without locking.
     *
     * @param cardId the card ID
     * @return the credit card
     * @throws NoSuchElementException if card not found
     */
    public CreditCard getCardById(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new NoSuchElementException("Card not found with id: " + cardId));
    }

    /**
     * Get a card by ID with pessimistic write lock for updates.
     * 
     * Use this when you need to modify the card's available limit to prevent
     * race conditions during concurrent authorization requests.
     *
     * @param cardId the card ID
     * @return the locked credit card
     * @throws NoSuchElementException if card not found
     */
    public CreditCard getCardByIdForUpdate(Long cardId) {
        return cardRepository.findByIdForUpdate(cardId)
                .orElseThrow(() -> new NoSuchElementException("Card not found with id: " + cardId));
    }

    /**
     * Save a card. Used after modifying available limit or other fields.
     *
     * @param card the card to save
     * @return the saved card
     */
    public CreditCard saveCard(CreditCard card) {
        return cardRepository.save(card);
    }
}
