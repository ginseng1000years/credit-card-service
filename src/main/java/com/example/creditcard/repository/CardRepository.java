package com.example.creditcard.repository;

import com.example.creditcard.domain.CreditCard;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<CreditCard, Long> {

    /**
     * Find a card by ID with pessimistic write lock.
     * This prevents race conditions during authorization by ensuring
     * only one transaction can update the available limit at a time.
     *
     * @param id the card ID
     * @return Optional containing the locked card
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CreditCard c WHERE c.id = :id")
    Optional<CreditCard> findByIdForUpdate(Long id);
}
