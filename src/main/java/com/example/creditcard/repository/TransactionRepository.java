package com.example.creditcard.repository;

import com.example.creditcard.domain.CardTransaction;
import com.example.creditcard.domain.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface TransactionRepository extends JpaRepository<CardTransaction, Long> {

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM CardTransaction t WHERE t.card.id = :cardId AND t.type = :type")
    BigDecimal sumByCardIdAndType(Long cardId, TransactionType type);
}
