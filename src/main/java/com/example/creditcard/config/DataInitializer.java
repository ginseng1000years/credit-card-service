package com.example.creditcard.config;

import com.example.creditcard.domain.CreditCard;
import com.example.creditcard.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final CardRepository cardRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");
        
        // Check if data already exists
        if (cardRepository.count() == 0) {
            log.info("No cards found, initializing test data...");
            
            CreditCard creditCard = CreditCard.builder()
                    .cardNumber("4532015112830366")
                    .creditLimit(new BigDecimal("10000.00"))
                    .availableLimit(new BigDecimal("10000.00"))
                    .build();
            
            cardRepository.save(creditCard);
            log.info("Test credit card created successfully: {}", creditCard.getCardNumber());
        } else {
            log.info("Data already exists, skipping initialization");
        }
    }
}
