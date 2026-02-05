package com.example.creditcard.service;

import com.example.creditcard.domain.CreditCard;
import com.example.creditcard.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService {

    private final CardRepository cardRepository;

    public CreditCard getCardById(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new NoSuchElementException("Card not found with id: " + cardId));
    }

    public CreditCard saveCard(CreditCard card) {
        return cardRepository.save(card);
    }
}
