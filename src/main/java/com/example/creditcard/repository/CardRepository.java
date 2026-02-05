package com.example.creditcard.repository;

import com.example.creditcard.domain.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<CreditCard, Long> {
}
